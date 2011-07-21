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

import com.github.peholmst.mvp4vaadin.View;
import com.github.peholmst.mvp4vaadin.navigation.NavigationController;
import com.github.peholmst.mvp4vaadin.navigation.NavigationRequest;

/**
 * This interface defines a map between string IDs and {@link View} instances.
 * It is also possible to look up views by their classes.
 * <p>
 * The purpose of this map is to decouple view implementations from each other.
 * For example, if view A wants to navigate to view B using a
 * {@link NavigationController}, it only needs to know the ID of view B to be
 * able to build a {@link NavigationRequest} (using the
 * {@link ViewMapNavigationRequestBuilder}).
 * 
 * @author Petter Holmström
 * @since 1.0
 */
public interface ViewMap extends java.io.Serializable {

	/**
	 * Returns the view identified by the specified ID.
	 * 
	 * @throws NoSuchViewException
	 *             if no such view could be found.
	 */
	View getViewById(String viewId) throws NoSuchViewException;

	/**
	 * Returns whether there exists a view in the map with the specified ID.
	 */
	boolean containsView(String viewId);

	/**
	 * Returns the view of the specified class.
	 * 
	 * @throws NoSuchViewException
	 *             if no such view could be found, or if there were several
	 *             views of the same class in the map.
	 */
	<V extends View> V getViewByClass(Class<V> viewClass)
			throws NoSuchViewException;

	/**
	 * Returns whether there exists <em>exactly one</em> view in the map of the
	 * specified class.
	 */
	boolean containsView(Class<? extends View> viewClass);
}
