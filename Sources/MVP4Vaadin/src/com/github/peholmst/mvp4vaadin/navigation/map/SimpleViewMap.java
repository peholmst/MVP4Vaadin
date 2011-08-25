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
package com.github.peholmst.mvp4vaadin.navigation.map;

import java.util.HashMap;

import com.github.peholmst.mvp4vaadin.View;

/**
 * A simple implementation of the {@link ViewMap} interface. This class is not
 * thread safe.
 * 
 * @author Petter Holmström
 * @since 1.0
 */
public class SimpleViewMap implements ViewMap {

	private static final long serialVersionUID = 88500836324623029L;

	private final HashMap<String, View> viewMap = new HashMap<String, View>();

	@Override
	public View getViewById(String viewId) throws NoSuchViewException {
		View view = viewMap.get(viewId);
		if (view == null) {
			throw new NoSuchViewException(viewId);
		}
		return view;
	}

	@Override
	public boolean containsView(String viewId) {
		return viewMap.containsKey(viewId);
	}

	@Override
	public <V extends View> V getViewByClass(Class<V> viewClass)
			throws NoSuchViewException {
		View found = null;
		for (View v : viewMap.values()) {
			if (viewClass.isAssignableFrom(v.getClass())) {
				if (found != null) {
					// More than one class
					throw new NoSuchViewException(
							"There are more than one view of " + viewClass);
				}
				found = v;
			}
		}
		if (found == null) {
			throw new NoSuchViewException("No view found");
		}
		return viewClass.cast(found);
	}

	@Override
	public boolean containsView(Class<? extends View> viewClass) {
		try {
			getViewByClass(viewClass);
			return true;
		} catch (NoSuchViewException e) {
			return false;
		}
	}

	/**
	 * Registers the specified view with the specified view ID. If another view
	 * has been previously registered with the same ID, it will get replaced.
	 */
	public void registerView(String viewId, View view) {
		viewMap.put(viewId, view);
	}

	/**
	 * Unregisters the view with the specified view ID. If no such view exists,
	 * nothing happens.
	 */
	public void unregisterView(String viewId) {
		viewMap.remove(viewId);
	}

}
