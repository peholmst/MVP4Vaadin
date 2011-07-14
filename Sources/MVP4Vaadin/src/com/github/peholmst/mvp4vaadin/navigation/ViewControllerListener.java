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

/**
 * Listener interface to be implemented by classes that need to be notified when
 * the current view of a {@link ViewController} changes.
 * 
 * @author Petter Holmström
 * @since 1.0
 */
@Deprecated
public interface ViewControllerListener extends java.io.Serializable {

	/**
	 * This method is called by a {@link ViewController} instance when its
	 * current view has changed.
	 * 
	 * @param source
	 *            the source of the event (must not be <code>null</code>).
	 * @param oldView
	 *            the old view, or <code>null</code> if <code>newView</code> was
	 *            the first view to be added to the controller.
	 * @param newView
	 *            the new view (must not be <code>null</code>).
	 * @param direction
	 *            <code>FORWARD</code> if the current view changed from a lower
	 *            view to an upper view in the stack, <code>BACKWARD</code> if
	 *            the current view changed from an upper view to a lower view in
	 *            the stack.
	 * @param newViewIsTopMost
	 *            true if <code>newView</code> is the top most view on the
	 *            stack, false if there are views on top of <code>newView</code>
	 *            (allowing forward navigation).
	 */
	void currentViewChanged(ViewController source, ControllableView oldView,
			ControllableView newView, Direction direction,
			boolean newViewIsTopMost);

}
