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

package com.liferay.portlet.shopping.service.persistence;

import com.liferay.portal.service.persistence.BasePersistence;

import com.liferay.portlet.shopping.model.ShoppingItemField;

/**
 * <a href="ShoppingItemFieldPersistence.java.html"><b><i>View Source</i></b></a>
 *
 * <p>
 * ServiceBuilder generated this class. Modifications in this class will be
 * overwritten the next time is generated.
 * </p>
 *
 * @author    Brian Wing Shun Chan
 * @see       ShoppingItemFieldPersistenceImpl
 * @see       ShoppingItemFieldUtil
 * @generated
 */
public interface ShoppingItemFieldPersistence extends BasePersistence<ShoppingItemField> {
	public void cacheResult(
		com.liferay.portlet.shopping.model.ShoppingItemField shoppingItemField);

	public void cacheResult(
		java.util.List<com.liferay.portlet.shopping.model.ShoppingItemField> shoppingItemFields);

	public com.liferay.portlet.shopping.model.ShoppingItemField create(
		long itemFieldId);

	public com.liferay.portlet.shopping.model.ShoppingItemField remove(
		long itemFieldId)
		throws com.liferay.portal.SystemException,
			com.liferay.portlet.shopping.NoSuchItemFieldException;

	public com.liferay.portlet.shopping.model.ShoppingItemField updateImpl(
		com.liferay.portlet.shopping.model.ShoppingItemField shoppingItemField,
		boolean merge) throws com.liferay.portal.SystemException;

	public com.liferay.portlet.shopping.model.ShoppingItemField findByPrimaryKey(
		long itemFieldId)
		throws com.liferay.portal.SystemException,
			com.liferay.portlet.shopping.NoSuchItemFieldException;

	public com.liferay.portlet.shopping.model.ShoppingItemField fetchByPrimaryKey(
		long itemFieldId) throws com.liferay.portal.SystemException;

	public java.util.List<com.liferay.portlet.shopping.model.ShoppingItemField> findByItemId(
		long itemId) throws com.liferay.portal.SystemException;

	public java.util.List<com.liferay.portlet.shopping.model.ShoppingItemField> findByItemId(
		long itemId, int start, int end)
		throws com.liferay.portal.SystemException;

	public java.util.List<com.liferay.portlet.shopping.model.ShoppingItemField> findByItemId(
		long itemId, int start, int end,
		com.liferay.portal.kernel.util.OrderByComparator obc)
		throws com.liferay.portal.SystemException;

	public com.liferay.portlet.shopping.model.ShoppingItemField findByItemId_First(
		long itemId, com.liferay.portal.kernel.util.OrderByComparator obc)
		throws com.liferay.portal.SystemException,
			com.liferay.portlet.shopping.NoSuchItemFieldException;

	public com.liferay.portlet.shopping.model.ShoppingItemField findByItemId_Last(
		long itemId, com.liferay.portal.kernel.util.OrderByComparator obc)
		throws com.liferay.portal.SystemException,
			com.liferay.portlet.shopping.NoSuchItemFieldException;

	public com.liferay.portlet.shopping.model.ShoppingItemField[] findByItemId_PrevAndNext(
		long itemFieldId, long itemId,
		com.liferay.portal.kernel.util.OrderByComparator obc)
		throws com.liferay.portal.SystemException,
			com.liferay.portlet.shopping.NoSuchItemFieldException;

	public java.util.List<com.liferay.portlet.shopping.model.ShoppingItemField> findAll()
		throws com.liferay.portal.SystemException;

	public java.util.List<com.liferay.portlet.shopping.model.ShoppingItemField> findAll(
		int start, int end) throws com.liferay.portal.SystemException;

	public java.util.List<com.liferay.portlet.shopping.model.ShoppingItemField> findAll(
		int start, int end, com.liferay.portal.kernel.util.OrderByComparator obc)
		throws com.liferay.portal.SystemException;

	public void removeByItemId(long itemId)
		throws com.liferay.portal.SystemException;

	public void removeAll() throws com.liferay.portal.SystemException;

	public int countByItemId(long itemId)
		throws com.liferay.portal.SystemException;

	public int countAll() throws com.liferay.portal.SystemException;
}