/**
 * Copyright (c) 2000-present Liferay, Inc. All rights reserved.
 *
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 */

package com.liferay.portlet;

import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.transaction.Propagation;
import com.liferay.portal.kernel.transaction.TransactionAttribute;
import com.liferay.portal.kernel.transaction.TransactionInvokerUtil;
import com.liferay.portal.kernel.util.HashUtil;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.service.PortalPreferencesLocalServiceUtil;
import com.liferay.portal.service.persistence.PortalPreferencesUtil;

import java.io.IOException;
import java.io.Serializable;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;

import javax.portlet.ReadOnlyException;

import org.hibernate.StaleObjectStateException;

/**
 * @author Brian Wing Shun Chan
 * @author Alexander Chow
 */
public class PortalPreferencesImpl
	extends BasePreferencesImpl
	implements Cloneable, PortalPreferences, Serializable {

	public static final TransactionAttribute SUPPORTS_TRANSACTION_ATTRIBUTE;

	static {
		TransactionAttribute.Builder builder =
			new TransactionAttribute.Builder();

		builder.setPropagation(Propagation.SUPPORTS);
		builder.setReadOnly(true);
		builder.setRollbackForClasses(
			PortalException.class, SystemException.class);

		SUPPORTS_TRANSACTION_ATTRIBUTE = builder.build();
	}

	public PortalPreferencesImpl() {
		this(0, 0, null, Collections.<String, Preference>emptyMap(), false);
	}

	public PortalPreferencesImpl(
		long ownerId, int ownerType, String xml,
		Map<String, Preference> preferences, boolean signedIn) {

		super(ownerId, ownerType, xml, preferences);

		_signedIn = signedIn;
	}

	@Override
	public PortalPreferencesImpl clone() {
		return new PortalPreferencesImpl(
			getOwnerId(), getOwnerType(), getOriginalXML(),
			new HashMap<>(getOriginalPreferences()), isSignedIn());
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}

		if (!(obj instanceof PortalPreferencesImpl)) {
			return false;
		}

		PortalPreferencesImpl portalPreferences = (PortalPreferencesImpl)obj;

		if ((getOwnerId() == portalPreferences.getOwnerId()) &&
			(getOwnerType() == portalPreferences.getOwnerType()) &&
			getPreferences().equals(portalPreferences.getPreferences())) {

			return true;
		}
		else {
			return false;
		}
	}

	@Override
	public long getUserId() {
		return _userId;
	}

	@Override
	public String getValue(String namespace, String key) {
		return getValue(namespace, key, null);
	}

	@Override
	public String getValue(String namespace, String key, String defaultValue) {
		key = _encodeKey(namespace, key);

		return super.getValue(key, defaultValue);
	}

	@Override
	public String[] getValues(String namespace, String key) {
		return getValues(namespace, key, null);
	}

	@Override
	public String[] getValues(
		String namespace, String key, String[] defaultValue) {

		key = _encodeKey(namespace, key);

		return super.getValues(key, defaultValue);
	}

	@Override
	public int hashCode() {
		int hashCode = HashUtil.hash(0, getOwnerId());

		hashCode = HashUtil.hash(hashCode, getOwnerType());
		hashCode = HashUtil.hash(hashCode, getPreferences());

		return hashCode;
	}

	@Override
	public boolean isSignedIn() {
		return _signedIn;
	}

	@Override
	public void reset(String key) throws ReadOnlyException {
		if (isReadOnly(key)) {
			throw new ReadOnlyException(key);
		}

		Map<String, Preference> modifiedPreferences = getModifiedPreferences();

		modifiedPreferences.remove(key);
	}

	@Override
	public void resetValues(final String namespace) {
		try {
			retryableStore(new Callable<Void>() {

				@Override
				public Void call() throws ReadOnlyException {
					Map<String, Preference> preferences = getPreferences();

					for (Map.Entry<String, Preference> entry :
							preferences.entrySet()) {

						String key = entry.getKey();

						if (key.startsWith(namespace) && !isReadOnly(key)) {
							reset(key);
						}
					}

					return null;
				}

			});
		}
		catch (Throwable t) {
			_log.error(t, t);
		}
	}

	@Override
	public void setSignedIn(boolean signedIn) {
		_signedIn = signedIn;
	}

	@Override
	public void setUserId(long userId) {
		_userId = userId;
	}

	@Override
	public void setValue(
		final String namespace, final String key, final String value) {

		if (Validator.isNull(key) || key.equals(_RANDOM_KEY)) {
			return;
		}

		try {
			Callable<Void> callable = new Callable<Void>() {

				@Override
				public Void call() throws ReadOnlyException {
					String encodedKey = _encodeKey(namespace, key);

					if (value != null) {
						PortalPreferencesImpl.super.setValue(encodedKey, value);
					}
					else {
						reset(encodedKey);
					}

					return null;
				}

			};

			if (_signedIn) {
				retryableStore(callable);
			}
			else {
				callable.call();
			}
		}
		catch (Throwable t) {
			_log.error(t, t);
		}
	}

	@Override
	public void setValues(
		final String namespace, final String key, final String[] values) {

		if (Validator.isNull(key) || key.equals(_RANDOM_KEY)) {
			return;
		}

		try {
			Callable<Void> callable = new Callable<Void>() {

				@Override
				public Void call() throws ReadOnlyException {
					String encodedKey = _encodeKey(namespace, key);

					if (values != null) {
						PortalPreferencesImpl.super.setValues(
							encodedKey, values);
					}
					else {
						reset(encodedKey);
					}

					return null;
				}

			};

			if (_signedIn) {
				retryableStore(callable);
			}
			else {
				callable.call();
			}
		}
		catch (Throwable t) {
			_log.error(t, t);
		}
	}

	@Override
	public void store() throws IOException {
		try {
			PortalPreferencesLocalServiceUtil.updatePreferences(
				getOwnerId(), getOwnerType(), this);
		}
		catch (SystemException se) {
			throw new IOException(se);
		}
	}

	protected boolean isCausedByStaleObjectException(Throwable t) {
		Throwable cause = t.getCause();

		while (t != cause) {
			if (t instanceof StaleObjectStateException) {
				return true;
			}

			if (cause == null) {
				return false;
			}

			t = cause;

			cause = t.getCause();
		}

		return false;
	}

	protected void retryableStore(Callable<?> callable) throws Throwable {
		while (true) {
			try {
				callable.call();

				store();

				return;
			}
			catch (Exception e) {
				if (isCausedByStaleObjectException(e)) {
					long ownerId = getOwnerId();
					int ownerType = getOwnerType();

					com.liferay.portal.model.PortalPreferences
						portalPreferences = reload(ownerId, ownerType);

					if (portalPreferences == null) {
						continue;
					}

					String preferencesXML = portalPreferences.getPreferences();

					PortalPreferencesImpl portalPreferencesImpl =
						(PortalPreferencesImpl)
							PortletPreferencesFactoryUtil.fromXML(
								ownerId, ownerType, preferencesXML);

					reset();

					setOriginalPreferences(
						portalPreferencesImpl.getOriginalPreferences());

					setOriginalXML(preferencesXML);
				}
				else {
					throw e;
				}
			}
		}
	}

	private String _encodeKey(String namespace, String key) {
		if (Validator.isNull(namespace)) {
			return key;
		}
		else {
			return namespace.concat(StringPool.POUND).concat(key);
		}
	}

	private com.liferay.portal.model.PortalPreferences reload(
			final long ownerId, final int ownerType)
		throws Throwable {

		return TransactionInvokerUtil.invoke(
			SUPPORTS_TRANSACTION_ATTRIBUTE,
			new Callable<com.liferay.portal.model.PortalPreferences>() {

				@Override
				public com.liferay.portal.model.PortalPreferences call() {
					return PortalPreferencesUtil.fetchByO_O(
						ownerId, ownerType, false);
				}

			});
	}

	private static final String _RANDOM_KEY = "r";

	private static final Log _log = LogFactoryUtil.getLog(
		PortalPreferencesImpl.class);

	private boolean _signedIn;
	private long _userId;

}