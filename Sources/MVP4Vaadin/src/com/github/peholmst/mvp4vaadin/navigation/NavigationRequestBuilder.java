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

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.github.peholmst.mvp4vaadin.View;

/**
 * This class implements a builder for creating new {@link NavigationRequest}
 * instances. This is how it is to be used:
 * <ul>
 * <li>Create a new builder instance by calling {@link #newInstance()}.</li>
 * <li>Set any params you may have using {@link #setParam(String, Object)} or
 * {@link #setParams(Map)}.</li>
 * <li>Create a path builder by calling one of the <code>startWith...</code>
 * methods.</li>
 * <li>Add additional views to the path using
 * {@link PathBuilder#addViewToPath(View)} or
 * {@link PathBuilder#addViewsToPath(View...)}.</li>
 * <li>Create the request by calling {@link PathBuilder#buildRequest()}.</li>
 * </ul>
 * 
 * @author Petter Holmström
 * @since 1.0
 */
public final class NavigationRequestBuilder {

	private NavigationRequestBuilder() {
	}

	private PathBuilder pathBuilder;

	private final HashMap<String, Object> params = new HashMap<String, Object>();

	/**
	 * A path builder is used to construct the path of the
	 * {@link NavigationRequest}. The request itself is built by calling the
	 * {@link #buildRequest()} method.
	 * 
	 * @author Petter Holmström
	 * @since 1.0
	 */
	public final class PathBuilder {

		private final LinkedList<View> path = new LinkedList<View>();

		private PathBuilder() {
		}

		private PathBuilder(List<View> initialPath) {
			path.addAll(initialPath);
		}

		/**
		 * Adds the specified view to the path.
		 */
		public PathBuilder addViewToPath(View view) {
			path.add(view);
			return this;
		}

		/**
		 * Adds the specified views to the path.
		 */
		public PathBuilder addViewsToPath(View... views) {
			path.addAll(Arrays.asList(views));
			return this;
		}

		/**
		 * Builds a {@link NavigationRequest} instance for the current path and
		 * returns it.
		 * 
		 * @throws IllegalStateException
		 *             if the path is empty.
		 */
		@SuppressWarnings("unchecked")
		public NavigationRequest buildRequest() throws IllegalStateException {
			if (path.isEmpty()) {
				throw new IllegalStateException(
						"The path must contain at least one view");
			}
			final List<View> copyOfPath = Collections
					.unmodifiableList((List<View>) path.clone());
			final Map<String, Object> copyOfParams = Collections
					.unmodifiableMap((Map<String, Object>) params.clone());
			return new NavigationRequest() {

				private static final long serialVersionUID = -6273102646598049858L;

				@Override
				public List<View> getPath() {
					return copyOfPath;
				}

				@Override
				public Map<String, Object> getParams() {
					return copyOfParams;
				}
			};
		}
	}

	/**
	 * Sets the value of a single parameter to be passed to the view.
	 */
	public NavigationRequestBuilder setParam(String paramName, Object paramValue) {
		params.put(paramName, paramValue);
		return this;
	}

	/**
	 * Sets the values of multiple parameters to be passed to the view.
	 */
	public NavigationRequestBuilder setParams(Map<String, Object> params) {
		params.putAll(params);
		return this;
	}

	/**
	 * Returns a {@link PathBuilder} that starts from the previous view (i.e.
	 * the view behind the current view) of the specified view controller. This
	 * path can be used to perform a "go back" navigation.
	 * 
	 * @throws IllegalStateException
	 *             if there are less than two views in the controller's stack,
	 *             or if another path builder has already been created.
	 */
	public PathBuilder startWithPathToPreviousView(
			NavigationController controller) throws IllegalStateException {
		if (controller.getViewStack().size() < 2) {
			throw new IllegalStateException(
					"Not enough views in controller to start from the previous view");
		}
		verifyPathBulderNotSet();
		pathBuilder = new PathBuilder(controller.getViewStack().subList(0,
				controller.getViewStack().size() - 2));
		return pathBuilder;
	}

	/**
	 * Returns a {@link PathBuilder} that starts from the first view of the
	 * specified view controller. This path can be used to perform a "go home"
	 * navigation.
	 * 
	 * @throws IllegalStateException
	 *             if the controller's stack is empty, or if another path
	 *             builder has already been created.
	 */
	public PathBuilder startWithPathToFirstView(NavigationController controller)
			throws IllegalStateException {
		if (controller.isEmpty()) {
			throw new IllegalStateException(
					"Controller is empty, cannot start from the first view");
		}
		verifyPathBulderNotSet();
		pathBuilder = new PathBuilder(controller.getViewStack().subList(0, 1));
		return pathBuilder;
	}

	/**
	 * Returns a {@link PathBuilder} that starts from the current view of the
	 * specified view controller. This path can be used when adding a new view
	 * to the stack. If the controller is empty, this call has the same effect
	 * as using {@link #startWithEmptyPath()}.
	 * 
	 * @throws IllegalStateException
	 *             if another path builder has already been created.
	 */
	public PathBuilder startWithPathToCurrentView(
			NavigationController controller) throws IllegalStateException {
		verifyPathBulderNotSet();
		pathBuilder = new PathBuilder(controller.getViewStack());
		return pathBuilder;
	}

	/**
	 * Returns a {@link PathBuilder} that starts with an empty path. At least
	 * one view has to be added before the navigation request can be built.
	 * 
	 * @throws IllegalStateException
	 *             if another path builder has already been created.
	 */
	public PathBuilder startWithEmptyPath() throws IllegalStateException {
		verifyPathBulderNotSet();
		pathBuilder = new PathBuilder();
		return pathBuilder;
	}

	private void verifyPathBulderNotSet() throws IllegalStateException {
		if (pathBuilder != null) {
			throw new IllegalStateException(
					"A pathBuilder has already been created");
		}
	}

	/**
	 * Returns a new navigation request builder instance.
	 */
	public static NavigationRequestBuilder newInstance() {
		return new NavigationRequestBuilder();
	}

}
