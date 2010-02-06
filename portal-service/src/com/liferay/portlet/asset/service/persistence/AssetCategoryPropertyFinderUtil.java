/**
 * Copyright (c) 2000-2010 Liferay, Inc. All rights reserved.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.liferay.portlet.asset.service.persistence;

import com.liferay.portal.kernel.bean.PortalBeanLocatorUtil;

/**
 * <a href="AssetCategoryPropertyFinderUtil.java.html"><b><i>View Source</i></b>
 * </a>
 *
 * @author Brian Wing Shun Chan
 */
public class AssetCategoryPropertyFinderUtil {
	public static int countByG_K(long groupId, java.lang.String key)
		throws com.liferay.portal.SystemException {
		return getFinder().countByG_K(groupId, key);
	}

	public static java.util.List<com.liferay.portlet.asset.model.AssetCategoryProperty> findByG_K(
		long groupId, java.lang.String key)
		throws com.liferay.portal.SystemException {
		return getFinder().findByG_K(groupId, key);
	}

	public static java.util.List<com.liferay.portlet.asset.model.AssetCategoryProperty> findByG_K(
		long groupId, java.lang.String key, int start, int end)
		throws com.liferay.portal.SystemException {
		return getFinder().findByG_K(groupId, key, start, end);
	}

	public static AssetCategoryPropertyFinder getFinder() {
		if (_finder == null) {
			_finder = (AssetCategoryPropertyFinder)PortalBeanLocatorUtil.locate(AssetCategoryPropertyFinder.class.getName());
		}

		return _finder;
	}

	public void setFinder(AssetCategoryPropertyFinder finder) {
		_finder = finder;
	}

	private static AssetCategoryPropertyFinder _finder;
}