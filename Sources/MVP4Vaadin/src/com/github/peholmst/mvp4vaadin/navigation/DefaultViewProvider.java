/*
 * Copyright (c) 2011 Petter Holmström
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.github.peholmst.mvp4vaadin.navigation;

import java.util.HashMap;
import java.util.Map;

/**
 * This is the default implementation of the {@link ViewProvider} interface. It
 * does not require any special initialization, just create a new instance and
 * start using it.
 * 
 * @author Petter Holmström
 * @since 1.0
 * @param <V>
 *            the super interface of the views provided by this view provider.
 *            In most cases this is <code>ControllableView</code>, but you are
 *            free to use your own custom interface.
 */
public class DefaultViewProvider<V extends ControllableView> implements
		ViewProvider<V> {

	private static final long serialVersionUID = -3278782991118186145L;

	// Protected access to make unit testing easier
	protected Map<String, V> viewMap = new HashMap<String, V>();

	/**
	 * Adds the specified view to the view provider, using the view's class name
	 * as its ID. If another view with the same ID already exists, it will be
	 * replaced.
	 * 
	 * @param view
	 *            the view to add (must not be <code>null</code>).
	 * @throws IllegalStateException
	 *             if the view has not been initialized.
	 */
	public void addPreinitializedView(V view) throws IllegalStateException {
		if (view == null) {
			throw new IllegalArgumentException("null view");
		}
		if (!view.isInitialized()) {
			throw new IllegalStateException("view not initialized");
		}
		viewMap.put(view.getClass().getName(), view);
	}

	/**
	 * Adds the specified view to the view provider, using the specified view
	 * ID. If another view with the same ID already exists, it will be replaced.
	 * 
	 * @param view
	 *            the view to add (must not be <code>null</code>).
	 * @param viewId
	 *            the ID of the view (must not be <code>null</code> nor empty).
	 * @throws IllegalStateException
	 *             if the view has not been initialized.
	 */
	public void addPreinitializedView(V view, String viewId)
			throws IllegalStateException {
		if (view == null) {
			throw new IllegalArgumentException("null view");
		}
		if (viewId == null || viewId.isEmpty()) {
			throw new IllegalArgumentException("null or empty viewId");
		}
		if (!view.isInitialized()) {
			throw new IllegalStateException("view not initialized");
		}
		viewMap.put(viewId, view);
	}

	@Override
	public V getView(String viewId) {
		if (viewId == null) {
			return null;
		}
		return viewMap.get(viewId);
	}

	@Override
	public <T extends V> T getView(Class<T> viewClass) {
		if (viewClass == null) {
			return null;
		}
		ControllableView view = viewMap.get(viewClass.getName());
		return view != null ? viewClass.cast(view) : null;
	}

	@Override
	public String getViewId(V view) {
		if (view == null) {
			return null;
		}
		for (Map.Entry<String, V> entry : viewMap.entrySet()) {
			if (entry.getValue().equals(view)) {
				return entry.getKey();
			}
		}
		return null;
	}

}
