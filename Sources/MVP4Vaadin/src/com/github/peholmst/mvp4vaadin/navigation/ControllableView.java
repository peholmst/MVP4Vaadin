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

import java.util.Map;

import com.github.peholmst.mvp4vaadin.View;

/**
 * This is an extended version of the {@link View} interface that is designed to
 * be used together with a {@link ViewController}. Please see the JavaDocs for
 * the controller for more information.
 * 
 * @author Petter Holmström
 * @since 1.0
 */
@Deprecated
public interface ControllableView extends View {

	/**
	 * Gets the view controller that controls this view. Please note, that a
	 * view can only be controlled by one controller at a time.
	 * 
	 * @return the view controller, or <code>null</code> if the view is not
	 *         currently controlled.
	 */
	ViewController getViewController();

	/**
	 * This method is called by the view controller when the view is shown.
	 * 
	 * @param viewController
	 *            the view controller (must not be <code>null</code>).
	 * @param userData
	 *            a map of user-definable parameters (may be <code>null</code>).
	 * @param oldView
	 *            the view that was previously visible (may be <code>null</code>
	 *            if this view is the first view to be shown).
	 * @param direction
	 *            the direction of the navigation inside the stack (must not be
	 *            <code>null</code>).
	 */
	void showView(ViewController viewController, Map<String, Object> userData,
			ControllableView oldView, Direction direction);

	/**
	 * Enumeration that defines different hide operations for a view. Please see
	 * {@link ControllableView#hideView(ViewController, ControllableView, Direction)}
	 * for more information about what the different alternatives mean.
	 * 
	 * @author Petter Holmström
	 * @since 1.0
	 */
	enum HideOperation {
		PREVENT, ALLOW, ALLOW_WITHOUT_FORWARD_NAVIGATION
	}

	/**
	 * This method is called by the view controller before the view is hidden
	 * and another one is shown. The view can control the operation by returning
	 * a {@link HideOperation} constant:
	 * <ul>
	 * <li>{@link HideOperation#PREVENT PREVENT}: aborts the operation; the view
	 * remains visible.</li>
	 * <li>{@link HideOperation#ALLOW ALLOW}: allows the operation; the view is
	 * hidden but remains in the controller stack.</li>
	 * <li>{@link HideOperation#ALLOW_WITHOUT_FORWARD_NAVIGATION
	 * ALLOW_WITHOUT_FORWARD_NAVIGATION}: allows the operation; the view is
	 * hidden. If <code>direction</code> is {@link Direction#FORWARD FORWARD},
	 * the view remains in the stack. If <code>direction</code> is
	 * {@link Direction#BACKWARD BACKWARD}, the view and all the views on top of
	 * it will be removed from the stack.</li>
	 * </ul>
	 * <p>
	 * Please note, that when the controller is jumping past several views (as
	 * opposed to just moving one view forward or backward), this method will be
	 * called on all the views that are jumped even though none of them are
	 * actually visible to the user.
	 * 
	 * @param viewController
	 *            the view controller (must not be <code>null</code>).
	 * @param newView
	 *            the view that will be shown in place of this view (must not be
	 *            <code>null</code>).
	 * @param direction
	 *            the direction of the navigation inside the stack (must not be
	 *            <code>null</code>).
	 * @return the hide operation to use (never <code>null</code>).
	 */
	HideOperation hideView(ViewController viewController,
			ControllableView newView, Direction direction);

}
