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

package com.liferay.wiki.web.display.context.util;

import com.liferay.portal.kernel.display.context.util.BaseStrutsRequestHelper;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.settings.GroupServiceSettingsLocator;
import com.liferay.portal.kernel.settings.ParameterMapSettingsLocator;
import com.liferay.portal.kernel.settings.PortletInstanceSettingsLocator;
import com.liferay.portal.kernel.settings.SettingsFactory;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.wiki.constants.WikiConstants;
import com.liferay.wiki.constants.WikiWebKeys;
import com.liferay.wiki.model.WikiPage;
import com.liferay.wiki.settings.WikiGroupServiceSettings;
import com.liferay.wiki.web.settings.WikiPortletInstanceSettings;
import com.liferay.wiki.web.util.WikiWebComponentProvider;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Iván Zaera
 */
public class WikiRequestHelper extends BaseStrutsRequestHelper {

	public WikiRequestHelper(HttpServletRequest request) {
		super(request);
	}

	public long getCategoryId() {
		if (_categoryId == null) {
			_categoryId = ParamUtil.getLong(getRequest(), "categoryId", 0);
		}

		return _categoryId;
	}

	public WikiGroupServiceSettings getWikiGroupServiceSettings() {
		try {
			if (_wikiGroupServiceSettings == null) {
				String portletResource = getPortletResource();

				WikiWebComponentProvider wikiWebComponentProvider =
					WikiWebComponentProvider.getWikiWebComponentProvider();

				SettingsFactory settingsFactory =
					wikiWebComponentProvider.getSettingsFactory();

				if (Validator.isNotNull(portletResource)) {
					_wikiGroupServiceSettings = settingsFactory.getSettings(
						WikiGroupServiceSettings.class,
						new ParameterMapSettingsLocator(
							getRequest().getParameterMap(),
							new GroupServiceSettingsLocator(
								getSiteGroupId(), WikiConstants.SERVICE_NAME)));
				}
				else {
					_wikiGroupServiceSettings = settingsFactory.getSettings(
						WikiGroupServiceSettings.class,
						new GroupServiceSettingsLocator(
							getSiteGroupId(), WikiConstants.SERVICE_NAME));
				}
			}

			return _wikiGroupServiceSettings;
		}
		catch (PortalException pe) {
			throw new SystemException(pe);
		}
	}

	public WikiPage getWikiPage() {
		if (_wikiPage == null) {
			HttpServletRequest request = getRequest();

			_wikiPage = (WikiPage)request.getAttribute(WikiWebKeys.WIKI_PAGE);
		}

		return _wikiPage;
	}

	public WikiPortletInstanceSettings getWikiPortletInstanceSettings() {
		try {
			if (_wikiPortletInstanceSettings == null) {
				String portletResource = getPortletResource();

				WikiWebComponentProvider wikiWebComponentProvider =
					WikiWebComponentProvider.getWikiWebComponentProvider();

				SettingsFactory settingsFactory =
					wikiWebComponentProvider.getSettingsFactory();

				if (Validator.isNotNull(portletResource)) {
					_wikiPortletInstanceSettings = settingsFactory.getSettings(
						WikiPortletInstanceSettings.class,
						new ParameterMapSettingsLocator(
							getRequest().getParameterMap(),
							new PortletInstanceSettingsLocator(
								getLayout(), getResourcePortletId())));
				}
				else {
					_wikiPortletInstanceSettings = settingsFactory.getSettings(
						WikiPortletInstanceSettings.class,
						new PortletInstanceSettingsLocator(
							getLayout(), getPortletId()));
				}
			}

			return _wikiPortletInstanceSettings;
		}
		catch (PortalException pe) {
			throw new SystemException(pe);
		}
	}

	private Long _categoryId;
	private WikiGroupServiceSettings _wikiGroupServiceSettings;
	private WikiPage _wikiPage;
	private WikiPortletInstanceSettings _wikiPortletInstanceSettings;

}