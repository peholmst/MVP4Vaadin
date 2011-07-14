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
	 * This method is called when the view is attached to the specified view
	 * controller (i.e. added to the stack).
	 * 
	 * @param controller
	 *            the view controller to which the view was attached.
	 * @param userData
	 *            a map of user-definable parameters (may be <code>null</code>).
	 */
	void attachedToController(ViewController controller,
			Map<String, Object> userData);

	/**
	 * This method is called when the view is detached from the specified view
	 * controller (i.e. removed from the stack).
	 * 
	 * @param controller
	 *            the view controller from which the view was detached.
	 */
	void detachedFromController(ViewController controller);

	/**
	 * This method is called when the specified view controller attempts to
	 * detach the view. The view can prevent this from happening by returning
	 * false, in which case the view will become the current view. If the view
	 * returns true, it will be detached and
	 * {@link #detachedFromController(ViewController)} will be called.
	 * 
	 * @param controller
	 *            the view controller from which the view is being detached.
	 * @return true to allow the view from being detached, false to prevent it.
	 */
	boolean detachingFromController(ViewController controller);

	/**
	 * Returns whether the view is the current view of its controller. If the
	 * view is not attached to a controller, this method always returns false.
	 */
	boolean isCurrentViewOfViewController();
		
	void navigatedForwardToView(ControllableView fromView);
	
	void navigatedBackwardToView(ControllableView fromView);
	
	/**
	 * This method is called when the controller navigates away from this view
	 * to the next view in the stack.
	 * @param toView
	 */
	void navigatedToNextView(ControllableView toView);

}
