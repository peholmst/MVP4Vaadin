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

import java.util.Stack;


/**
 * TODO Document me!
 * 
 * @author Petter Holmström
 * 
 * @param <V>
 */
public class NestedControllersMaster<V extends ControllableView> implements
		java.io.Serializable {

	private static final long serialVersionUID = 7878176235648881833L;

	// TODO Add logging!
	
	// Package visibility to make unit testing easier
	Stack<ViewController<V>> controllerStack = new Stack<ViewController<V>>();
	
	ViewControllerListener<V> listener = new ViewControllerListener<V>() {

		private static final long serialVersionUID = 5278881787281496864L;

		@SuppressWarnings("unchecked")
		@Override
		public void currentViewChanged(ViewController<V> source, V oldView,
				V newView, Direction direction, boolean newViewIsTopMost) {
			// Start by making the source the active controller
			makeTopController(source);
			if (newView instanceof ControllableViewWithEmbeddedController) {
				// If the source's current page has an embedded controller, make that controller
				// the active controller. If that controller's current page also has an embedded controller,
				// recursivly add it to the stack, etc.
				addControllerToStack((ControllableViewWithEmbeddedController<V>) newView);
			}
		}
	};
	
	@SuppressWarnings("unchecked")
	private void addControllerToStack(ControllableViewWithEmbeddedController<V> viewWithController) {
		if (viewWithController.getEmbeddedController() != null) {
			controllerStack.push(viewWithController.getEmbeddedController()).addListener(listener);
			if (viewWithController.getEmbeddedController().getCurrentView() instanceof ControllableViewWithEmbeddedController) {
				addControllerToStack((ControllableViewWithEmbeddedController<V>) viewWithController.getEmbeddedController().getCurrentView());
			}
		}
	}
	
	/**
	 * 
	 * @author petter
	 *
	 * @param <V>
	 */
	protected static class TraceElement<V extends ControllableView> {
		private final ViewController<V> controller;
		private final V view;
		
		/**
		 * 
		 * @param controller
		 */
		public TraceElement(ViewController<V> controller) {
			this.controller = controller;
			this.view = null;
		}
		
		/**
		 * 
		 * @param view
		 */
		public TraceElement(V view) {
			this.view = view;
			this.controller = null;
		}
		
		/**
		 * @return the controller
		 */
		public ViewController<V> getController() {
			return controller;
		}
		
		/**
		 * @return the view
		 */
		public V getView() {
			return view;
		}
	}
	
	/**
	 * 
	 * @author Petter Holmström
	 *
	 * @param <V>
	 */
	protected interface ControllerVisitor<V extends ControllableView> {
		
		/**
		 * 
		 * @param controller
		 * @param trace
		 * @return true to continue with the next controller, false to abort and return from {@link NestedControllersMaster#visitControllers(ControllerVisitor)} immediately.
		 */
		boolean visitController(ViewController<V> controller, Stack<TraceElement<V>> trace);
	}

	/**
	 * 
	 * @param visitor
	 */
	protected void visitControllers(ControllerVisitor<V> visitor) {
		if (getToplevelController() != null) {			
			doVisitController(visitor, getToplevelController(), new Stack<TraceElement<V>>());
		}
	}
	
	@SuppressWarnings("unchecked")
	private boolean doVisitController(ControllerVisitor<V> visitor, ViewController<V> controller, Stack<TraceElement<V>> trace) {
		trace.push(new TraceElement<V>(controller));
		if (!visitor.visitController(controller, (Stack<TraceElement<V>>) trace.clone())) {
			return false;
		}
		for (V openView : controller.getTrail()) {
			if (openView instanceof ControllableViewWithEmbeddedController) {
				ControllableViewWithEmbeddedController<V> viewWithController = (ControllableViewWithEmbeddedController<V>) openView;
				if (viewWithController.getEmbeddedController() != null) {
					trace.push(new TraceElement<V>(openView));
					if (!doVisitController(visitor, viewWithController.getEmbeddedController(), trace)) {
						return false;
					}
					trace.pop();
				}
			}
		}
		trace.pop();
		return true;
	}
	
	private void makeTopController(ViewController<V> controller) {
		while (!controllerStack.isEmpty() && controllerStack.peek() != controller) {
			controllerStack.pop().removeListener(listener);
		}
		if (controllerStack.isEmpty()) {
			throw new IllegalStateException("The controller stack is empty! This is a bug that should be reported!");
		}
	}
	
	/**
	 * 
	 * @param viewController
	 */
	public void setToplevelController(ViewController<V> viewController) {
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
	public ViewController<V> getToplevelController() {
		return (controllerStack.isEmpty() ? null : controllerStack.firstElement());
	}

	/**
	 * Gets the view controller that is currently active.
	 * 
	 * @return the active view controller, or <code>null</code> if there are no controllers in the stack at all.
	 */
	public ViewController<V> getActiveViewController() {
		return (controllerStack.isEmpty() ? null : controllerStack.peek());
	}
	
	/**
	 * TODO Test me and document me!
	 * @return
	 */
	public boolean goBack() {
		if (controllerStack.isEmpty()) {
			throw new IllegalStateException("No active controller");
		}
		for (int i = controllerStack.size() -1; i >= 0; --i) {
			ViewController<V> controller = controllerStack.get(i);
			if (controller.isBackwardNavigationPossible()) {
				return controller.goBack();
			}
		}
		return false;
	}

	// TODO Implement support for forward navigation
}
