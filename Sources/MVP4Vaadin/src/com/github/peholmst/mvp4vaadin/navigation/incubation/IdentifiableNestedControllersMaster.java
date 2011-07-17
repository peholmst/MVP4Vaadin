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
package com.github.peholmst.mvp4vaadin.navigation.incubation;

import java.util.Stack;

import com.github.peholmst.mvp4vaadin.navigation.ViewController;

/**
 * TODO Document me!
 * 
 * @author Petter Holmström
 * @since 1.0
 * @deprecated Will be deleted in the 1.0 release
 */
@Deprecated
public class IdentifiableNestedControllersMaster extends
		NestedControllersMaster {

	// TODO This is a stub, implement me!

	// TODO Add logging!

	private static final long serialVersionUID = 2826839632930996105L;

	/**
	 * Checks if any of the nested controllers contain a view with the specified
	 * view identifier.
	 * 
	 * @see IdentifiableViewController#containsIdentifiableView(String)
	 * 
	 * @param viewIdentifier
	 *            the identifier to look for (must not be <code>null</code>).
	 * @return true if a view was found, false if not.
	 */
	public boolean hasOpenIdentifiableView(final String viewIdentifier) {
		if (viewIdentifier == null) {
			throw new IllegalArgumentException("null viewIdentifier");
		}
		final boolean[] found = new boolean[0];
		visitControllers(new ControllerVisitor() {

			@Override
			public boolean visitController(ViewController controller,
					Stack<TraceElement> trace) {
				if (controller instanceof IdentifiableViewController) {
					if (((IdentifiableViewController) controller)
							.containsIdentifiableView(viewIdentifier)) {
						found[0] = true;
						return false;
					}
				}
				return true;
			}
		});
		return found[0];
	}

	/**
	 * 
	 * @param viewIdentifier
	 * @return true if the switch was successful, false if not.
	 */
	public boolean switchToIdentifiableView(final String viewIdentifier)
			throws Exception {
		if (viewIdentifier == null) {
			throw new IllegalArgumentException("null viewIdentifier");
		}
		final boolean[] viewChanged = new boolean[0];
		viewChanged[0] = true;
		visitControllers(new ControllerVisitor() {

			@Override
			public boolean visitController(ViewController controller,
					Stack<TraceElement> trace) {
				if (controller instanceof IdentifiableViewController) {
					IdentifiableViewController ivc = (IdentifiableViewController) controller;
					if (ivc.containsIdentifiableView(viewIdentifier)) {
						if (!ivc.goToIdentifiableView(viewIdentifier)) {
							// The view did not change
							viewChanged[0] = false;
						} else {
							// Now the controller has moved to the view. Now we
							// have
							// to make sure the controller becomes the active
							// controller

							// The topmost element in the trace stack is the
							// controller itself,
							// so we can just pop it.
							trace.pop();
							while (getActiveViewController() != ivc) {
								if (trace.isEmpty()) {
									// The trace is empty and the controller is
									// still not the active controller
									// => the switch failed.
									viewChanged[0] = false;
									break;
								}
								// This should always be a view
								TraceElement viewElement = trace.pop();
								if (viewElement.getView() == null) {
									throw new IllegalStateException(
											"No view found in the stack where one was expected");
								}
								if (trace.isEmpty()) {
									throw new IllegalStateException(
											"The stack is empty when it should contain at least one element");
								}
								// Now pop the view's controller
								TraceElement controllerElement = trace.pop();
								if (controllerElement.getController() == null) {
									throw new IllegalStateException(
											"No controller found in the stack where one was expected");
								}
								// Navigate to the view
								controllerElement.getController().goToView(
										viewElement.getView());
								if (controllerElement.getController()
										.getCurrentView() != viewElement
										.getView()) {
									// We could not go to the desired view =>
									// the switch failed
									viewChanged[0] = false;
									break;
								}
							}
						}
						return false;
					}
				}
				return true;
			}
		});
		return viewChanged[0];
	}

}
