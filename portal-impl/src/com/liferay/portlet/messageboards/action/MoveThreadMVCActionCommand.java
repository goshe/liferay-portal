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

package com.liferay.portlet.messageboards.action;

import com.liferay.portal.kernel.portlet.bridges.mvc.BaseMVCActionCommand;
import com.liferay.portal.kernel.portlet.bridges.mvc.MVCActionCommand;
import com.liferay.portal.kernel.servlet.SessionErrors;
import com.liferay.portal.kernel.spring.osgi.OSGiBeanProperties;
import com.liferay.portal.kernel.util.ObjectValuePair;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.security.auth.PrincipalException;
import com.liferay.portal.service.ServiceContext;
import com.liferay.portal.service.ServiceContextFactory;
import com.liferay.portal.theme.ThemeDisplay;
import com.liferay.portal.util.PortletKeys;
import com.liferay.portal.util.WebKeys;
import com.liferay.portlet.ActionResponseImpl;
import com.liferay.portlet.messageboards.LockedThreadException;
import com.liferay.portlet.messageboards.MBGroupServiceSettings;
import com.liferay.portlet.messageboards.MessageBodyException;
import com.liferay.portlet.messageboards.MessageSubjectException;
import com.liferay.portlet.messageboards.NoSuchThreadException;
import com.liferay.portlet.messageboards.RequiredMessageException;
import com.liferay.portlet.messageboards.model.MBMessage;
import com.liferay.portlet.messageboards.model.MBThread;
import com.liferay.portlet.messageboards.model.MBThreadConstants;
import com.liferay.portlet.messageboards.service.MBMessageServiceUtil;
import com.liferay.portlet.messageboards.service.MBThreadLocalServiceUtil;
import com.liferay.portlet.messageboards.service.MBThreadServiceUtil;

import java.io.InputStream;

import java.util.Collections;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.PortletURL;

/**
 * @author Jorge Ferrer
 */
@OSGiBeanProperties(
	property = {
		"javax.portlet.name=" + PortletKeys.MESSAGE_BOARDS,
		"javax.portlet.name=" + PortletKeys.MESSAGE_BOARDS_ADMIN,
		"mvc.command.name=/message_boards/move_thread"
	},
	service = MVCActionCommand.class
)
public class MoveThreadMVCActionCommand extends BaseMVCActionCommand {

	@Override
	public void doProcessAction(
			ActionRequest actionRequest, ActionResponse actionResponse)
		throws Exception {

		try {
			moveThread(actionRequest, actionResponse);
		}
		catch (LockedThreadException | PrincipalException |
				RequiredMessageException e) {

			SessionErrors.add(actionRequest, e.getClass());

			actionResponse.setRenderParameter(
				"mvcPath", "/html/portlet/message_boards/error.jsp");
		}
		catch (MessageBodyException | MessageSubjectException |
				NoSuchThreadException e) {

			SessionErrors.add(actionRequest, e.getClass());
		}
	}

	protected void moveThread(
			ActionRequest actionRequest, ActionResponse actionResponse)
		throws Exception {

		ThemeDisplay themeDisplay = (ThemeDisplay)actionRequest.getAttribute(
			WebKeys.THEME_DISPLAY);

		long categoryId = ParamUtil.getLong(actionRequest, "mbCategoryId");
		long threadId = ParamUtil.getLong(actionRequest, "threadId");

		MBThread thread = MBThreadLocalServiceUtil.getThread(threadId);

		MBThreadServiceUtil.moveThread(categoryId, threadId);

		boolean addExplanationPost = ParamUtil.getBoolean(
			actionRequest, "addExplanationPost");

		if (addExplanationPost) {
			String subject = ParamUtil.getString(actionRequest, "subject");
			String body = ParamUtil.getString(actionRequest, "body");

			MBGroupServiceSettings mbGroupServiceSettings =
				MBGroupServiceSettings.getInstance(
					themeDisplay.getScopeGroupId());

			ServiceContext serviceContext = ServiceContextFactory.getInstance(
				MBMessage.class.getName(), actionRequest);

			MBMessageServiceUtil.addMessage(
				thread.getRootMessageId(), subject, body,
				mbGroupServiceSettings.getMessageFormat(),
				Collections.<ObjectValuePair<String, InputStream>>emptyList(),
				false, MBThreadConstants.PRIORITY_NOT_GIVEN, false,
				serviceContext);
		}

		PortletURL portletURL =
			((ActionResponseImpl)actionResponse).createRenderURL();

		portletURL.setParameter(
			"mvcRenderCommandName", "/message_boards/view_message");
		portletURL.setParameter(
			"messageId", String.valueOf(thread.getRootMessageId()));

		actionResponse.sendRedirect(portletURL.toString());
	}

}