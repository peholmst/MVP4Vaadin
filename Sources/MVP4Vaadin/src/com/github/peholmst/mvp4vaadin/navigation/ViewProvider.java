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

/**
 * A View Provider is basically a way to map {@link ControllableView}-instances
 * to String IDs. By using a View Provider together with a
 * {@link ViewController}, views can navigate between each other by using String
 * IDs instead of class instances, which reduces the coupling of your
 * application.
 * 
 * @see InitializingViewProvider
 * 
 * @author Petter Holmström
 * @since 1.0
 */
@Deprecated
public interface ViewProvider extends java.io.Serializable {

	/**
	 * Gets the view with the specified ID. If the ID is null, <code>null</code>
	 * is returned.
	 * 
	 * @param viewId
	 *            the ID of the view to fetch.
	 * @return the view, or <code>null</code> if not found.
	 */
	ControllableView getView(String viewId);

	/**
	 * Gets the view whose ID is the name of the specified class. If the class
	 * is null, <code>null</code> is returned.
	 * 
	 * @param <T>
	 *            the type of the view to fetch.
	 * @param viewClass
	 *            the class of the view to fetch.
	 * @return the view, or <code>null</code> if not found.
	 */
	<T extends ControllableView> T getView(Class<T> viewClass);

	/**
	 * Gets the ID of the specified view. If the view is <code>null</code>,
	 * <code>null</code> is returned.
	 * 
	 * @param view
	 *            the view whose ID should be fetched.
	 * @return the ID, or <code>null</code> if not found.
	 */
	String getViewId(ControllableView view);
}
