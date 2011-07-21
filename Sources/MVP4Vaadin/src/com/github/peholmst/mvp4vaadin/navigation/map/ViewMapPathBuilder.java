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

import java.util.List;

import com.github.peholmst.mvp4vaadin.View;
import com.github.peholmst.mvp4vaadin.navigation.NavigationRequestBuilder;

/**
 * A path builder that uses a {@link ViewMap} to look up the views to be added
 * to the path. Before any views can be added, the view map must be set by
 * calling {@link #setViewMap(ViewMap)}.
 * 
 * @author Petter Holmström
 * @since 1.0
 */
public class ViewMapPathBuilder extends NavigationRequestBuilder.PathBuilder {

	private ViewMap viewMap;

	/**
	 * Creates a new <code>ViewMapPathBuilder</code>.
	 * 
	 * @param requestBuilder
	 *            the owning request builder.
	 */
	public ViewMapPathBuilder(NavigationRequestBuilder<?> requestBuilder) {
		super(requestBuilder);
	}

	/**
	 * Creates a new <code>ViewMapPathBuilder</code>.
	 * 
	 * @param requestBuilder
	 *            the owning request builder.
	 * @param initialPath
	 *            the initial path.
	 */
	public ViewMapPathBuilder(NavigationRequestBuilder<?> requestBuilder,
			List<View> initialPath) {
		super(requestBuilder, initialPath);
	}

	/**
	 * Sets the view map to use. If a map has already been set, an
	 * {@link IllegalStateException} is thrown.
	 */
	public ViewMapPathBuilder setViewMap(ViewMap viewMap) {
		if (viewMap != null) {
			throw new IllegalStateException("A ViewMap has already been set");
		}
		this.viewMap = viewMap;
		return this;
	}

	/**
	 * Returns the view map or throws an {@link IllegalStateException} if none
	 * has been set.
	 */
	protected ViewMap getViewMap() {
		if (viewMap == null) {
			throw new IllegalStateException("No ViewMap has been set");
		}
		return viewMap;
	}

	/**
	 * Adds the view identified by the specified ID to the path.
	 * 
	 * @see ViewMap#getViewById(String)
	 */
	public ViewMapPathBuilder addViewToPath(String viewId) {
		getPath().add(getViewMap().getViewById(viewId));
		return this;
	}

	/**
	 * Adds the view of the specified class to the path.
	 * 
	 * @see ViewMap#getViewByClass(Class)
	 */
	public ViewMapPathBuilder addViewToPath(Class<? extends View> viewClass) {
		getPath().add(getViewMap().getViewByClass(viewClass));
		return this;
	}

	/**
	 * Adds the views identified by the specified IDs to the path.
	 * 
	 * @see ViewMap#getViewById(String)
	 */
	public ViewMapPathBuilder addViewsToPath(String... viewIds) {
		for (String viewId : viewIds) {
			getPath().add(getViewMap().getViewById(viewId));
		}
		return this;
	}

	/**
	 * Adds the views of the specified classes to the path.
	 * 
	 * @see ViewMap#getViewByClass(Class)
	 */
	public ViewMapPathBuilder addViewsToPath(
			Class<? extends View>... viewClasses) {
		for (Class<? extends View> viewClass : viewClasses) {
			getPath().add(getViewMap().getViewByClass(viewClass));
		}
		return this;
	}
}
