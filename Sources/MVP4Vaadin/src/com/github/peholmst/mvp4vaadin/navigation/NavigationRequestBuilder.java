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

import java.lang.reflect.Constructor;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.github.peholmst.mvp4vaadin.View;

/**
 * This class implements a builder for creating new {@link NavigationRequest}
 * instances. This is how it is to be used, using the default path builder:
 * <ul>
 * <li>Create a new builder instance by calling {@link #newInstance()}.</li>
 * <li>Set any params you may have using {@link #setParam(String, Object)} or
 * {@link #setParams(Map)}.</li>
 * <li>Create a path builder by calling one of the <code>startWith...</code>
 * methods.</li>
 * <li>Add additional views to the path using
 * {@link DefaultPathBuilder#addViewToPath(View)} or
 * {@link DefaultPathBuilder#addViewsToPath(View...)}.</li>
 * <li>Create the request by calling {@link DefaultPathBuilder#buildRequest()}.</li>
 * </ul>
 * <p>
 * Other path builders can be plugged in by using the
 * {@link #newInstance(Class)} factory method.
 * 
 * @author Petter Holmström
 * @since 1.0
 */
public final class NavigationRequestBuilder<P extends NavigationRequestBuilder.PathBuilder> {

	private final Class<P> pathBuilderClass;

	private P pathBuilder;

	private final HashMap<String, Object> params = new HashMap<String, Object>();

	private NavigationRequestBuilder(Class<P> pathBuilderClass) {
		this.pathBuilderClass = pathBuilderClass;
	}

	private P createPathBuilder() {
		try {
			final Constructor<P> constructor = pathBuilderClass
					.getConstructor(NavigationRequestBuilder.class);
			return constructor.newInstance(this);
		} catch (Exception e) {
			throw new RuntimeException("Could not create path builder", e);
		}
	}

	private P createPathBuilder(List<View> initialPath) {
		try {
			final Constructor<P> constructor = pathBuilderClass.getConstructor(
					NavigationRequestBuilder.class, List.class);
			return constructor.newInstance(this, initialPath);
		} catch (Exception e) {
			throw new RuntimeException("Could not create path builder", e);
		}
	}

	/**
	 * Base class for a path builder that builds the
	 * {@link NavigationRequest#getPath() path} of a {@link NavigationRequest}.
	 * 
	 * @author Petter Holmström
	 * @since 1.0
	 */
	public static abstract class PathBuilder {

		private final NavigationRequestBuilder<?> requestBuilder;
		private final LinkedList<View> path = new LinkedList<View>();

		/**
		 * Creates a new <code>PathBuilder</code>.
		 * 
		 * @param requestBuilder
		 *            the owning request builder.
		 */
		public PathBuilder(NavigationRequestBuilder<?> requestBuilder) {
			this.requestBuilder = requestBuilder;
		}

		/**
		 * Creates a new <code>PathBuilder</code>.
		 * 
		 * @param requestBuilder
		 *            the owning request builder.
		 * @param initialPath
		 *            the initial path.
		 */
		public PathBuilder(NavigationRequestBuilder<?> requestBuilder,
				List<View> initialPath) {
			this(requestBuilder);
			path.addAll(initialPath);
		}

		/**
		 * Returns the path to which views can be added.
		 */
		protected final LinkedList<View> getPath() {
			return path;
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
			if (getPath().isEmpty()) {
				throw new IllegalStateException(
						"The path must contain at least one view");
			}
			final List<View> copyOfPath = Collections
					.unmodifiableList((List<View>) getPath().clone());
			final Map<String, Object> copyOfParams = Collections
					.unmodifiableMap((Map<String, Object>) requestBuilder.params
							.clone());
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
	 * A path builder is used to construct the path of the
	 * {@link NavigationRequest}. The request itself is built by calling the
	 * {@link #buildRequest()} method.
	 * 
	 * @author Petter Holmström
	 * @since 1.0
	 */
	public static final class DefaultPathBuilder extends PathBuilder {

		public DefaultPathBuilder(NavigationRequestBuilder<?> requestBuilder) {
			super(requestBuilder);
		}

		public DefaultPathBuilder(NavigationRequestBuilder<?> requestBuilder,
				List<View> initialPath) {
			super(requestBuilder, initialPath);
		}

		/**
		 * Adds the specified view to the path.
		 */
		public DefaultPathBuilder addViewToPath(View view) {
			getPath().add(view);
			return this;
		}

		/**
		 * Adds the specified views to the path.
		 */
		public DefaultPathBuilder addViewsToPath(View... views) {
			getPath().addAll(Arrays.asList(views));
			return this;
		}
	}

	/**
	 * Sets the value of a single parameter to be passed to the view.
	 */
	public NavigationRequestBuilder<P> setParam(String paramName,
			Object paramValue) {
		params.put(paramName, paramValue);
		return this;
	}

	/**
	 * Sets the values of multiple parameters to be passed to the view.
	 */
	public NavigationRequestBuilder<P> setParams(Map<String, Object> params) {
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
	public P startWithPathToPreviousView(NavigationController controller)
			throws IllegalStateException {
		if (controller.getViewStack().size() < 2) {
			throw new IllegalStateException(
					"Not enough views in controller to start from the previous view");
		}
		verifyPathBuilderNotSet();
		pathBuilder = createPathBuilder(controller.getViewStack().subList(0,
				controller.getViewStack().size() - 1));
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
	public P startWithPathToFirstView(NavigationController controller)
			throws IllegalStateException {
		if (controller.isEmpty()) {
			throw new IllegalStateException(
					"Controller is empty, cannot start from the first view");
		}
		verifyPathBuilderNotSet();
		pathBuilder = createPathBuilder(controller.getViewStack().subList(0, 1));
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
	public P startWithPathToCurrentView(NavigationController controller)
			throws IllegalStateException {
		verifyPathBuilderNotSet();
		pathBuilder = createPathBuilder(controller.getViewStack());
		return pathBuilder;
	}

	/**
	 * Returns a {@link PathBuilder} that starts from the path to the specified
	 * view.
	 * 
	 * @throws IllegalStateException
	 *             if another path builder has already been created or the
	 *             specified view cannot be found in the controller.
	 */
	public P startWithPathToView(NavigationController controller, View view)
			throws IllegalStateException {
		verifyPathBuilderNotSet();
		LinkedList<View> path = new LinkedList<View>();
		boolean found = false;
		for (View viewInPath : controller.getViewStack()) {
			path.add(viewInPath);
			if (viewInPath.equals(view)) {
				found = true;
				break;
			}
		}
		if (!found) {
			throw new IllegalStateException("View not found in controller");
		}
		pathBuilder = createPathBuilder(path);
		return pathBuilder;
	}

	/**
	 * Returns a {@link PathBuilder} that starts with an empty path. At least
	 * one view has to be added before the navigation request can be built.
	 * 
	 * @throws IllegalStateException
	 *             if another path builder has already been created.
	 */
	public P startWithEmptyPath() throws IllegalStateException {
		verifyPathBuilderNotSet();
		pathBuilder = createPathBuilder();
		return pathBuilder;
	}

	private void verifyPathBuilderNotSet() throws IllegalStateException {
		if (pathBuilder != null) {
			throw new IllegalStateException(
					"A pathBuilder has already been created");
		}
	}

	/**
	 * Returns a new navigation request builder instance that uses the default
	 * path builder.
	 */
	public static NavigationRequestBuilder<DefaultPathBuilder> newInstance() {
		return new NavigationRequestBuilder<DefaultPathBuilder>(
				DefaultPathBuilder.class);
	}

	/**
	 * Returns a new navigation request builder instance that uses a path
	 * builder of the specified class.
	 */
	public static <P extends PathBuilder> NavigationRequestBuilder<P> newInstance(
			Class<P> pathBuilderClass) {
		return new NavigationRequestBuilder<P>(pathBuilderClass);
	}
}
