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
public interface ControllableView extends View {

	/**
	 * Returns the view controller to which the view is attached, or
	 * <code>null</code> if there is none. Please note, that a view can only be
	 * controlled by one controller at a time.
	 * 
	 * @see #attachedToController(ViewController)
	 * @see #detachedFromController(ViewController)
	 */
	ViewController getViewController();

	/**
	 * Returns whether the view is the current view of its controller. If the
	 * view is not attached to a controller, this method always returns false.
	 */
	boolean isCurrentViewOfViewController();

	/**
	 * This method is called when the view is attached to the specified view
	 * controller (i.e. added to the stack).
	 */
	void attachedToController(ViewController controller);

	/**
	 * This method is called when the view is detached from the specified view
	 * controller (i.e. removed from the stack).
	 */
	void detachedFromController(ViewController controller);

	/**
	 * This method is called when the controller has navigated to the view from
	 * a view lower down in the stack (or when the stack is empty). When this
	 * method is called, the view is the current view of the controller.
	 * 
	 * @param userData
	 *            a map of user-definable parameters (may be <code>null</code>).
	 * @param fromView
	 *            the view from which the controller navigated to this view (may
	 *            be <code>null</code> if this view is the first view to be
	 *            shown).
	 */
	void navigatedUpToView(Map<String, Object> userData,
			ControllableView fromView);

	/**
	 * This method is called when the controller has navigated to the view from
	 * a view higher up in the stack. When this method is called, the view is
	 * the current view of the controller.
	 * 
	 * @param fromView
	 *            the view from which the controller navigated to this view
	 *            (never <code>null</code>).
	 */
	void navigatedDownToView(ControllableView fromView);

	/**
	 * This method is called when the controller has navigated away from the
	 * view. When this method is called, <code>toView</code> is the current view
	 * of the controller.
	 * 
	 * @param toView
	 *            the view to which the controller navigated from this view
	 *            (never <code>null</code>).
	 */
	void navigatedAwayFromView(ControllableView toView);

	/**
	 * Returns whether the view allows the controller to navigate to another
	 * view higher up in the stack. The view need not be the current view when
	 * this method is called.
	 */
	boolean mayNavigateUp();

	/**
	 * Returns whether the view allows the controller to navigate to another
	 * view lower down in the stack. The view need not be the current view when
	 * this method is called.
	 */
	boolean mayNavigateDown();

	/**
	 * Returns whether the view can remain in the stack after the controller has
	 * navigated to a view lower down in the stack.
	 * <p>
	 * If this method returns true, the view may choose when to detach the view
	 * and can also navigate up to the view if necessary (e.g. if the user
	 * interface has a "Forward" button as in a web browser).
	 * <p>
	 * If this method returns false, the view and and any views on top of it in
	 * the stack will be detached as soon as the controller has navigated past
	 * it.
	 */
	boolean canRemainInStackAfterDownNavigation();
}
