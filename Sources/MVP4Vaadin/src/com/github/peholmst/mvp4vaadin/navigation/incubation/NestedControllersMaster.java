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

import com.github.peholmst.mvp4vaadin.navigation.ControllableView;
import com.github.peholmst.mvp4vaadin.navigation.Direction;
import com.github.peholmst.mvp4vaadin.navigation.ViewController;
import com.github.peholmst.mvp4vaadin.navigation.ViewControllerListener;

/**
 * TODO Document me!
 * 
 * @author Petter Holmström
 * @deprecated Will be deleted in the 1.0 release
 */
@Deprecated
public class NestedControllersMaster implements java.io.Serializable {

	private static final long serialVersionUID = 7878176235648881833L;

	// TODO Add logging!

	// Package visibility to make unit testing easier
	Stack<ViewController> controllerStack = new Stack<ViewController>();

	ViewControllerListener listener = new ViewControllerListener() {

		private static final long serialVersionUID = 5278881787281496864L;

		@Override
		public void currentViewChanged(ViewController source, ControllableView oldView,
				ControllableView newView, Direction direction, boolean newViewIsTopMost) {
			// Start by making the source the active controller
			makeTopController(source);
			if (newView instanceof ControllableViewWithEmbeddedController) {
				// If the source's current page has an embedded controller, make
				// that controller
				// the active controller. If that controller's current page also
				// has an embedded controller,
				// recursivly add it to the stack, etc.
				addControllerToStack((ControllableViewWithEmbeddedController) newView);
			}
		}
	};

	private void addControllerToStack(
			ControllableViewWithEmbeddedController viewWithController) {
		if (viewWithController.getEmbeddedController() != null) {
			controllerStack.push(viewWithController.getEmbeddedController())
					.addListener(listener);
			if (viewWithController.getEmbeddedController().getCurrentView() instanceof ControllableViewWithEmbeddedController) {
				addControllerToStack((ControllableViewWithEmbeddedController) viewWithController
						.getEmbeddedController().getCurrentView());
			}
		}
	}

	/**
	 * 
	 * @author petter
	 * 
	 */
	protected static class TraceElement {
		private final ViewController controller;
		private final ControllableView view;

		/**
		 * 
		 * @param controller
		 */
		public TraceElement(ViewController controller) {
			this.controller = controller;
			this.view = null;
		}

		/**
		 * 
		 * @param view
		 */
		public TraceElement(ControllableView view) {
			this.view = view;
			this.controller = null;
		}

		/**
		 * @return the controller
		 */
		public ViewController getController() {
			return controller;
		}

		/**
		 * @return the view
		 */
		public ControllableView getView() {
			return view;
		}
	}

	/**
	 * 
	 * @author Petter Holmström
	 * 
	 */
	protected interface ControllerVisitor {

		/**
		 * 
		 * @param controller
		 * @param trace
		 * @return true to continue with the next controller, false to abort and
		 *         return from
		 *         {@link NestedControllersMaster#visitControllers(ControllerVisitor)}
		 *         immediately.
		 */
		boolean visitController(ViewController controller,
				Stack<TraceElement> trace);
	}

	/**
	 * 
	 * @param visitor
	 */
	protected void visitControllers(ControllerVisitor visitor) {
		if (getToplevelController() != null) {
			doVisitController(visitor, getToplevelController(),
					new Stack<TraceElement>());
		}
	}

	@SuppressWarnings("unchecked")
	private boolean doVisitController(ControllerVisitor visitor,
			ViewController controller, Stack<TraceElement> trace) {
		trace.push(new TraceElement(controller));
		if (!visitor.visitController(controller,
				(Stack<TraceElement>) trace.clone())) {
			return false;
		}
		for (ControllableView openView : controller.getTrail()) {
			if (openView instanceof ControllableViewWithEmbeddedController) {
				ControllableViewWithEmbeddedController viewWithController = (ControllableViewWithEmbeddedController) openView;
				if (viewWithController.getEmbeddedController() != null) {
					trace.push(new TraceElement(openView));
					if (!doVisitController(visitor,
							viewWithController.getEmbeddedController(), trace)) {
						return false;
					}
					trace.pop();
				}
			}
		}
		trace.pop();
		return true;
	}

	private void makeTopController(ViewController controller) {
		while (!controllerStack.isEmpty()
				&& controllerStack.peek() != controller) {
			controllerStack.pop().removeListener(listener);
		}
		if (controllerStack.isEmpty()) {
			throw new IllegalStateException(
					"The controller stack is empty! This is a bug that should be reported!");
		}
	}

	/**
	 * 
	 * @param viewController
	 */
	public void setToplevelController(ViewController viewController) {
		// Clear the stack
		while (!controllerStack.isEmpty()) {
			controllerStack.pop().removeListener(listener);
		}
		// Add the new toplevel controller to the bottom of the stack
		if (viewController != null) {
			controllerStack.push(viewController).addListener(listener);
		}
	}

	/**
	 * 
	 * @return
	 */
	public ViewController getToplevelController() {
		return (controllerStack.isEmpty() ? null : controllerStack
				.firstElement());
	}

	/**
	 * Gets the view controller that is currently active.
	 * 
	 * @return the active view controller, or <code>null</code> if there are no
	 *         controllers in the stack at all.
	 */
	public ViewController getActiveViewController() {
		return (controllerStack.isEmpty() ? null : controllerStack.peek());
	}

	/**
	 * TODO Test me and document me!
	 * 
	 * @return
	 */
	public boolean goBack() {
		if (controllerStack.isEmpty()) {
			throw new IllegalStateException("No active controller");
		}
		for (int i = controllerStack.size() - 1; i >= 0; --i) {
			ViewController controller = controllerStack.get(i);
			if (controller.isBackwardNavigationPossible()) {
				return controller.goBack();
			}
		}
		return false;
	}

	// TODO Implement support for forward navigation
}
