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
import com.github.peholmst.stuff4vaadin.adapter.Adaptable;

/**
 * Views that want to control how they are handled by a
 * {@link NavigationController} should adapt this interface.
 * 
 * @see Adaptable#adapt(Class)
 * @see NavigationControllerCallbackAdapter
 * 
 * @author Petter Holmström
 * @since 1.0
 */
public interface NavigationControllerCallback extends java.io.Serializable {

	/**
	 * This method is called when the view is attached to the specified
	 * controller (i.e. added to the stack). A view can only be attached to one
	 * controller at a time.
	 */
	void attachedToController(NavigationController controller);

	/**
	 * This method is called when the view is being detached from the specified
	 * controller (i.e. removed from the stack). The view can prevent this by
	 * returning false, in which case the view will become the current view of
	 * the controller.
	 * 
	 * @see NavigationController#navigate(NavigationRequest)
	 * @see NavigationController#clear()
	 */
	boolean detachingFromController(NavigationController controller);

	/**
	 * This method is called when the view has been detached from the specified
	 * controller.
	 */
	void detachedFromController(NavigationController controller);

	/**
	 * This method is called when the controller has navigated to this view.
	 * After this method has been called, this view is the current view of the
	 * controller.
	 * 
	 * @param params
	 *            a map of user definable parameters (never <code>null</code>
	 *            but may be empty).
	 * @param fromView
	 *            the previous current view, or <code>null</code> if there was
	 *            no current view (i.e. the stack was empty).
	 */
	void navigatedToView(Map<String, Object> params, View fromView);

	/**
	 * This method is called when the controller has navigated away from this
	 * view to another view higher up in the stack. After this method has been
	 * called, this view is no longer the current view of the controller but it
	 * remains in the stack.
	 * 
	 * @param toView
	 *            the new current view (never <code>null</code>).
	 */
	void navigatedFromView(View toView);
}
