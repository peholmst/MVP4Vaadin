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
import java.util.Map;

/**
 * This interface defines a View Controller that controls navigation between
 * views by using a stack.
 * <p>
 * When the application starts, the stack normally contains only one view, the
 * "home" or "start" view. As the user navigates deeper into the applications,
 * each view is added to the stack, making a trail of where in the view
 * hierarchy the user currently resides (this makes it easy to create breadcrumb
 * components).
 * <p>
 * The user can also navigate backwards in the stack, towards the home view. It
 * is possible to either navigate to the previous view, or to any view in the
 * stack. A view may also prevent the controller from navigating away from it,
 * e.g. if it contains unsaved data.
 * <p>
 * View implementation can request navigation to other views by using one of the
 * <code>goToView(...)</code> methods.
 * 
 * @author Petter Holmström
 * @since 1.0
 */
public interface ViewController extends java.io.Serializable {
	/**
	 * Gets the current view, if available. This is the top element in the stack
	 * returned by {@link #getTrail()}. If the stack is empty, <code>null</code>
	 * is returned.
	 * 
	 * @return the current view, or <code>null</code> if the controller does not
	 *         contain any views.
	 */
	ControllableView getCurrentView();

	/**
	 * Gets the first view, if available. This is the bottom element in the
	 * stack returned by {@link #getTrail()}. If the stack is empty,
	 * <code>null</code> is returned.
	 * 
	 * @return the first view, or <code>null</code> if the controller does not
	 *         manage any views.
	 */
	ControllableView getFirstView();

	/**
	 * Attempts to close the current view, remove it from the stack and go back
	 * to the previous view. If the current view aborts the operation or if
	 * there are no views to go back to (i.e. the current view is the first
	 * view), this method returns false.
	 * 
	 * @see ControllableView#okToClose()
	 * @see #getTrail()
	 * @return true if the current view was changed as a result of this method
	 *         call, false if not (or if there are no views at all).
	 */
	boolean goBack();

	/**
	 * Attempts to go back to the first view, closing all the other views on the
	 * stack. If any of the views abort the operation, or if the first view is
	 * already the current view, or if there are no views at all on the stack,
	 * this method returns false.
	 * 
	 * @see ControllableView#okToClose()
	 * @see #getTrail()
	 * @return true if the current view was changed as a result of this method
	 *         call, false if not (or if there are no views at all).
	 */
	boolean goToFirstView();

	/**
	 * If set, this method returns the view provider that can be used to
	 * navigate between views. Using a view provider makes it easy to
	 * preinitialize views and let views navigate between each other without
	 * needing direct dependencies on each other's classes.
	 * 
	 * @see #setViewProvider(ViewProvider)
	 * @see #goToView(String)
	 * @see #goToView(String, String, Object)
	 * @see #goToView(String, Map)
	 * 
	 * @return the view provider, or <code>null</code> if none has been
	 *         specified.
	 */
	ViewProvider getViewProvider();

	/**
	 * Sets or clears the view provider for this controller.
	 * 
	 * @see #getViewProvider()
	 * 
	 * @param viewProvider
	 *            the view provider to set, may be <code>null</code>.
	 */
	void setViewProvider(ViewProvider viewProvider);

	/**
	 * Same as calling {@link #goToView(ControllableView, Map) goToView(view,
	 * null)}.
	 * 
	 * @param view
	 *            the view to go to (must not be <code>null</code>).
	 * @return true if the current view was changed as a result of this method
	 *         call, false if not (or if there are no views at all).
	 */
	boolean goToView(ControllableView view);

	/**
	 * Does the same as {@link #goToView(ControllableView)}, but fetches the
	 * view from the {@link #getViewProvider() view provider}. Remember to set a
	 * view provider before calling this method!
	 * 
	 * @see #setViewProvider()
	 * @see ViewProvider#getView(String)
	 * 
	 * @param viewId
	 *            the ID of the view to go to (must not be <code>null</code>).
	 * @return true if the current view was changed as a result of this method
	 *         call, false if not (or if there are no views at all).
	 * @throws IllegalStateException
	 *             if no view provider has been specified.
	 * @throws IllegalArgumentException
	 *             if there is no view with the specified view ID.
	 */
	boolean goToView(String viewId) throws IllegalStateException,
			IllegalArgumentException;

	/**
	 * Same as calling {@link #goToView(ControllableView, Map)}, but with the
	 * user data map containing a single key-value pair.
	 * 
	 * @param view
	 *            the view to go to (must not be <code>null</code>).
	 * @param userDataKey
	 *            the key of the user data property.
	 * @param userDataValue
	 *            the value of the user data property.
	 * @return true if the current view was changed as a result of this method
	 *         call, false if not (or if there are no views at all).
	 */
	boolean goToView(ControllableView view, String userDataKey,
			Object userDataValue);

	/**
	 * Does the same as {@link #goToView(ControllableView, String, Object)}, but
	 * fetches the view from the {@link #getViewProvider() view provider}.
	 * Remember to set a view provider before calling this method!
	 * 
	 * @see #setViewProvider()
	 * @see ViewProvider#getView(String)
	 * 
	 * @param viewId
	 *            the ID of the view to go to (must not be <code>null</code>).
	 * @param userDataKey
	 *            the key of the user data property.
	 * @param userDataValue
	 *            the value of the user data property.
	 * @return true if the current view was changed as a result of this method
	 *         call, false if not (or if there are no views at all).
	 * @throws IllegalStateException
	 *             if no view provider has been specified.
	 * @throws IllegalArgumentException
	 *             if there is no view with the specified view ID.
	 */
	boolean goToView(String viewId, String userDataKey, Object userDataValue)
			throws IllegalStateException, IllegalArgumentException;

	/**
	 * Attempts to go to the specified view. If the view is not already on the
	 * stack, it is added to the top of the stack and made the current view. Any
	 * user defined data is passed to the view via the
	 * {@link ControllableView#showView(Map)} method. If the view is on the
	 * stack, all the views on top of the view are closed. In this case,
	 * {@link ControllableView#showView(Map)} will not be called.
	 * <p>
	 * This method will return true if the current view has changed. This may
	 * not necessarily mean that the current view has changed to the specified
	 * view, though. If any of the views in the stack refuses to close, the
	 * current view will change to that view. This method only returns false if
	 * the current view did not change at all.
	 * 
	 * @see ControllableView#showView(Map)
	 * @see ControllableView#okToClose()
	 * @see #getTrail()
	 * @param view
	 *            the view to go to (must not be <code>null</code>).
	 * @param userData
	 *            a map with user defined properties that is passed to the view,
	 *            may be <code>null</code>.
	 * @return true if the current view was changed as a result of this method
	 *         call, false if not (or if there are no views at all).
	 */
	boolean goToView(ControllableView view, Map<String, Object> userData);

	/**
	 * Does the same as {@link #goToView(ControllableView, Map)}, but fetches
	 * the view from the {@link #getViewProvider() view provider}. Remember to
	 * set a view provider before calling this method!
	 * 
	 * @see #setViewProvider()
	 * @see ViewProvider#getView(String)
	 * 
	 * @param viewId
	 *            the ID of the view to go to (must not be <code>null</code>).
	 * @param userData
	 *            a map with user defined properties that is passed to the view,
	 *            may be <code>null</code>.
	 * @return true if the current view was changed as a result of this method
	 *         call, false if not (or if there are no views at all).
	 * @throws IllegalStateException
	 *             if no view provider has been specified.
	 * @throws IllegalArgumentException
	 *             if there is no view with the specified view ID.
	 */
	boolean goToView(String viewId, Map<String, Object> userData)
			throws IllegalStateException, IllegalArgumentException;

	/**
	 * Gets the stack of open views, with the topmost view being the current
	 * view.
	 * 
	 * @return an unmodifiable list representing the stack, with the bottom most
	 *         element at index 0.
	 */
	List<ControllableView> getTrail();

	/**
	 * Registers a listener to be notified when the current view is changed. A
	 * listener can be registered several times and will be notified once for
	 * each registration. If the listener is <code>null</code>, nothing happens.
	 * 
	 * @param listener
	 *            the listener to add.
	 */
	void addListener(ViewControllerListener listener);

	/**
	 * Unregisters a listener previously registered using
	 * {@link #addListener(ViewControllerListener)}. If the listener was
	 * registered multiple times, it will be notified one time less after this
	 * method invocation. If the listener is <code>null</code> or was never
	 * added, nothing happens.
	 * 
	 * @param listener
	 *            the listener to remove.
	 */
	void removeListener(ViewControllerListener listener);

}
