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

import java.util.List;

import com.github.peholmst.mvp4vaadin.View;

/**
 * This interface defines a view navigation controller that controls navigation
 * between views by using a stack.
 * <p>
 * When the application starts, the stack normally contains only one view, the
 * "home" or "start" view. As the user navigates deeper into the application,
 * each view is added to the stack, making a trail of where in the view
 * hierarchy the user currently resides (this makes it easy to create breadcrumb
 * components).
 * <p>
 * The user can also navigate backwards in the stack, towards the home view. It
 * is possible to either navigate to the previous view, or to any view in the
 * stack. A view may also prevent the controller from navigating away from it,
 * e.g. if it contains unsaved data.
 * <p>
 * Views can control how they are handled by the controller by adapting the
 * {@link NavigationControllerCallback} interface.
 * 
 * @author Petter Holmström
 * @since 1.0
 */
public interface NavigationController extends java.io.Serializable {

	/**
	 * Enumeration defining the result of the
	 * {@link NavigationController#navigate(NavigationRequest)} and
	 * {@link NavigationController#clear()} methods.
	 * 
	 * @author Petter Holmström
	 * @since 1.0
	 */
	enum NavigationResult {
		/**
		 * The navigation request was prevented. The current view was not
		 * changed.
		 */
		PREVENTED,

		/**
		 * The navigation request succeeded. The current view was changed to the
		 * view addressed by the navigation request. If the clear operation was
		 * invoked, the view stack is now empty.
		 */
		SUCCEEDED,

		/**
		 * The navigation request was interrupted. The current view did change,
		 * but not to the view addressed by the navigation request.
		 */
		INTERRUPTED
	}

	/**
	 * Attempts to navigate to the specified view. If the view is already in the
	 * stack, the controller will attempt to detach all the views on top of the
	 * specified view until the view becomes the top-most one (i.e. the current
	 * view). If the view is not in the stack, the controller will add it to the
	 * top of the stack, making it the top-most one.
	 * 
	 * @param request
	 *            the request indicating to which view the controller should
	 *            navigate (must not be <code>null</code>).
	 * @return the result of the navigation (see {@link NavigationRequest} for
	 *         more information).
	 */
	NavigationResult navigate(NavigationRequest request);

	/**
	 * Attempts to navigate to the previous view in the stack. Calling this
	 * method when there is only one view in the stack will clear the stack.
	 * 
	 * @return true if navigation succeeded, false if the current view remained
	 *         unchanged or if the stack was empty.
	 */
	boolean navigateBack();

	/**
	 * Returns the view stack. The current view is always the top-most view in
	 * the stack. If there are no views attached to the controller, the stack is
	 * empty.
	 * 
	 * @return an unmodifiable list representing the view stack where the
	 *         bottom-most view is at index 0.
	 */
	List<View> getViewStack();

	/**
	 * Returns the current view, or <code>null</code> if the stack is empty.
	 */
	View getCurrentView();

	/**
	 * Returns the first view, or <code>null</code> if the stack is empty.
	 */
	View getFirstView();

	/**
	 * Returns whether the stack is empty or not.
	 */
	boolean isEmpty();

	/**
	 * Returns whether the stack contains more than one element. This method can
	 * be useful when constructing breadcrumbs for example.
	 */
	boolean containsMoreThanOneElement();

	/**
	 * Attempts to remove all views from the stack.
	 * 
	 * @return {@link NavigationResult#SUCCEEDED} if the stack was cleared,
	 *         {@link NavigationResult#PREVENTED} if the current view did not
	 *         allow the controller to detach it, or
	 *         {@link NavigationResult#INTERRUPTED} if one of the other views
	 *         did not allow the controller to detach it.
	 */
	NavigationResult clear();

	/**
	 * Registers the specified listener to be notified of events occurring in
	 * the navigation controller. A listener can be registered several times and
	 * will be notified once for each registration. If the listener is
	 * <code>null</code>, nothing happens.
	 */
	void addListener(NavigationControllerListener listener);

	/**
	 * Unregisters a listener previously registered using
	 * {@link #addListener(NavigationControllerListener)}. If the listener was
	 * registered multiple times, it will be notified one time less after this
	 * method invocation. If the listener is <code>null</code> or was never
	 * added, nothing happens.
	 */
	void removeListener(NavigationControllerListener listener);
}
