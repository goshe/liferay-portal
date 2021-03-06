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

package com.liferay.portlet.mobiledevicerules.service.permission;

import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.security.auth.PrincipalException;
import com.liferay.portal.security.permission.PermissionChecker;
import com.liferay.portal.util.PortletKeys;
import com.liferay.portlet.exportimport.staging.permission.StagingPermissionUtil;
import com.liferay.portlet.mobiledevicerules.model.MDRRuleGroupInstance;
import com.liferay.portlet.mobiledevicerules.service.MDRRuleGroupInstanceLocalServiceUtil;

/**
 * @author Michael C. Han
 */
public class MDRRuleGroupInstancePermissionImpl
	implements MDRRuleGroupInstancePermission {

	@Override
	public void check(
			PermissionChecker permissionChecker, long ruleGroupInstanceId,
			String actionId)
		throws PortalException {

		if (!contains(permissionChecker, ruleGroupInstanceId, actionId)) {
			throw new PrincipalException.MustHavePermission(
				permissionChecker, MDRRuleGroupInstance.class.getName(),
				ruleGroupInstanceId, actionId);
		}
	}

	@Override
	public void check(
			PermissionChecker permissionChecker,
			MDRRuleGroupInstance ruleGroupInstance, String actionId)
		throws PortalException {

		if (!contains(permissionChecker, ruleGroupInstance, actionId)) {
			throw new PrincipalException.MustHavePermission(
				permissionChecker, MDRRuleGroupInstance.class.getName(),
				ruleGroupInstance.getRuleGroupInstanceId(), actionId);
		}
	}

	@Override
	public boolean contains(
			PermissionChecker permissionChecker, long ruleGroupInstanceId,
			String actionId)
		throws PortalException {

		MDRRuleGroupInstance ruleGroupInstance =
			MDRRuleGroupInstanceLocalServiceUtil.getMDRRuleGroupInstance(
				ruleGroupInstanceId);

		return contains(permissionChecker, ruleGroupInstance, actionId);
	}

	@Override
	public boolean contains(
		PermissionChecker permissionChecker,
		MDRRuleGroupInstance ruleGroupInstance, String actionId) {

		Boolean hasPermission = StagingPermissionUtil.hasPermission(
			permissionChecker, ruleGroupInstance.getGroupId(),
			MDRRuleGroupInstance.class.getName(),
			ruleGroupInstance.getRuleGroupInstanceId(),
			PortletKeys.MOBILE_DEVICE_SITE_ADMIN, actionId);

		if (hasPermission != null) {
			return hasPermission.booleanValue();
		}

		return permissionChecker.hasPermission(
			ruleGroupInstance.getGroupId(),
			MDRRuleGroupInstance.class.getName(),
			ruleGroupInstance.getRuleGroupInstanceId(), actionId);
	}

}