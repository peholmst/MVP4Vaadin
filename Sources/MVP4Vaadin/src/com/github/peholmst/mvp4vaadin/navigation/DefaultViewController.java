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

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Stack;

import com.github.peholmst.mvp4vaadin.navigation.ControllableView.HideOperation;

/**
 * This is the default implementation of the {@link ViewController} interface.
 * It does not require any special initialization, just create a new instance
 * and start using it.
 * 
 * @author Petter Holmström
 * @since 1.0
 */
@Deprecated
public class DefaultViewController implements ViewController {

	private static final long serialVersionUID = -8268392494366653976L;

	private ViewProvider viewProvider;

	// Package access to make unit testing easier

	ControllableView currentView;

	int indexOfCurrentView = -1;

	Stack<ControllableView> viewStack = new Stack<ControllableView>();

	protected Stack<ControllableView> getViewStack() {
		return viewStack;
	}

	@Override
	public ControllableView getCurrentView() {
		return currentView;
	}

	@Override
	public ControllableView getFirstView() {
		return viewStack.isEmpty() ? null : viewStack.firstElement();
	}

	@Override
	public boolean goBack() {
		if (viewStack.size() <= 1) {
			// There is nothing to go back to
			return false;
		}
		ControllableView newView = viewStack.get(indexOfCurrentView - 1);
		HideOperation operation = currentView.hideView(this, newView,
				Direction.BACKWARD);
		if (operation.equals(HideOperation.PREVENT)) {
			// Current view is not changed
			return false;
		} else if (operation.equals(HideOperation.ALLOW)) {
			// Current view is changed, but stack remains untouched
			setCurrentView(newView, indexOfCurrentView - 1, null,
					Direction.BACKWARD);
			return true;
		} else {
			// Current view is changed, stack is modified
			viewStack.pop();
			setCurrentView(newView, indexOfCurrentView - 1, null,
					Direction.BACKWARD);
			return true;
		}
	}

	@Override
	public boolean isBackwardNavigationPossible() {
		return indexOfCurrentView > 0;
	}

	@Override
	public boolean goForward() {
		// TODO Implement forward navigation
		return false;
	}

	@Override
	public boolean isForwardNavigationPossible() {
		return !viewStack.isEmpty() && currentView != viewStack.peek();
	}

	@Override
	public boolean goToFirstView() {
		ControllableView firstView = getFirstView();
		if (firstView == null) {
			return false;
		} else {
			return goToView(firstView, null);
		}
	}

	@Override
	public ViewProvider getViewProvider() {
		return viewProvider;
	}

	@Override
	public void setViewProvider(ViewProvider viewProvider) {
		this.viewProvider = viewProvider;
	}

	/**
	 * TODO Document me!
	 * 
	 * @param viewId
	 * @param userData
	 * @return something
	 * @throws IllegalStateException
	 * @throws IllegalArgumentException
	 */
	protected ControllableView getViewFromProvider(String viewId,
			Map<String, Object> userData) throws IllegalStateException,
			IllegalArgumentException {
		if (viewId == null) {
			throw new IllegalArgumentException("null viewId");
		}
		if (getViewProvider() == null) {
			throw new IllegalStateException("null viewProvider");
		}
		ControllableView view = null;
		if (getViewProvider() instanceof InitializingViewProvider) {
			view = ((InitializingViewProvider) getViewProvider()).getView(
					viewId, userData);
		} else {
			view = getViewProvider().getView(viewId);
		}
		if (view == null) {
			throw new IllegalArgumentException("no such view");
		}
		return view;
	}

	@Override
	public boolean goToView(ControllableView view) {
		return goToView(view, null);
	}

	@Override
	public boolean goToView(String viewId) throws IllegalStateException,
			IllegalArgumentException {
		return goToView(getViewFromProvider(viewId, null));
	}

	private Map<String, Object> buildSimpleUserDataMap(String key, Object value) {
		Map<String, Object> userData = null;
		if (key != null) {
			userData = new HashMap<String, Object>();
			userData.put(key, value);
		}
		return userData;
	}

	@Override
	public boolean goToView(ControllableView view, String userDataKey,
			Object userDataValue) {
		return goToView(view,
				buildSimpleUserDataMap(userDataKey, userDataValue));
	}

	@Override
	public boolean goToView(String viewId, String userDataKey,
			Object userDataValue) throws IllegalStateException,
			IllegalArgumentException {
		Map<String, Object> userData = buildSimpleUserDataMap(userDataKey,
				userDataValue);
		return goToView(getViewFromProvider(viewId, userData), userData);
	}

	@Override
	public boolean goToView(ControllableView view, Map<String, Object> userData) {
		if (view == null) {
			throw new IllegalArgumentException("null view");
		}

		// Check if the view is already the current view
		if (currentView == view) {
			return false;
		}

		ControllableView oldView = currentView;

		int indexOfNewViewInStack = viewStack.indexOf(view);
		if (indexOfNewViewInStack != -1) {
			// The view already exists in the stack, check if we're moving
			// forward or backward
			if (indexOfNewViewInStack < indexOfCurrentView) {
				// We're moving backward
				ControllableView viewToHide = currentView;
				int indexOfViewToHide = indexOfCurrentView;
				while (viewToHide != view) {
					HideOperation operation = viewToHide.hideView(this, view,
							Direction.BACKWARD);
					if (operation.equals(HideOperation.PREVENT)) {
						if (viewToHide != oldView) {
							// We did not get all the way, but the current view
							// did change
							// We pass null instead of the user data as this
							// view is not the intended destination.
							setCurrentView(viewToHide, indexOfViewToHide, null,
									Direction.BACKWARD);
							return true;
						} else {
							return false;
						}
					} else if (operation.equals(HideOperation.ALLOW)) {
						// Do not remove any elements from the view stack =>
						// allow forward navigation
						viewToHide = viewStack.elementAt(--indexOfViewToHide);
					} else {
						// Remove elements from stack to prevent forward
						// navigation
						viewToHide = viewStack.elementAt(--indexOfViewToHide);
						viewStack.setSize(indexOfViewToHide + 1);
					}
				}
				setCurrentView(view, indexOfViewToHide, userData,
						Direction.BACKWARD);
			} else {
				// We're moving forward
				// TODO Implement forward navigation
				throw new UnsupportedOperationException(
						"Forward navigation is not yet implemented");
			}
		} else {
			// We're adding a new view to the stack.
			// If the current view is not the topmost view, we should clear the
			// stack.
			if (!viewStack.isEmpty() && currentView != viewStack.peek()) {
				do {
					/*
					 * No hideView() is called here as the views are already hidden and
					 * are in front of the current view, i.e. they have already allowed
					 * the current view to become visible. 
					 */
					viewStack.pop();
				} while (currentView != viewStack.peek());
			}
			if (currentView != null
					&& currentView.hideView(this, view, Direction.FORWARD)
							.equals(HideOperation.PREVENT)) {
				return false;
			}
			viewStack.push(view);
			setCurrentView(view, viewStack.size() - 1, userData,
					Direction.FORWARD);
		}
		return true;
	}

	protected void setCurrentView(ControllableView view,
			int indexOfCurrentView, Map<String, Object> userData,
			Direction direction) {
		ControllableView oldView = currentView;
		currentView = view;
		this.indexOfCurrentView = indexOfCurrentView;
		view.showView(this, userData, oldView, direction);
		fireCurrentViewChanged(oldView, view, direction,
				currentView == viewStack.peek());
	}

	@Override
	public boolean goToView(String viewId, Map<String, Object> userData)
			throws IllegalStateException, IllegalArgumentException {
		return goToView(getViewFromProvider(viewId, userData), userData);
	}

	@Override
	public List<ControllableView> getTrail() {
		return Collections.unmodifiableList(viewStack);
	}

	private final LinkedList<ViewControllerListener> listenerList = new LinkedList<ViewControllerListener>();

	/**
	 * TODO Document me!
	 * 
	 * @param oldView
	 * @param newView
	 * @param direction
	 * @param newViewIsTopMost
	 */
	@SuppressWarnings("unchecked")
	protected void fireCurrentViewChanged(ControllableView oldView,
			ControllableView newView, Direction direction,
			boolean newViewIsTopMost) {
		/*
		 * Create a clone of the listener list. This way, we prevent weird
		 * situations if any of the listeners register new listeners or remove
		 * existing ones.
		 */
		LinkedList<ViewControllerListener> clonedList = (LinkedList<ViewControllerListener>) listenerList
				.clone();
		for (ViewControllerListener listener : clonedList) {
			listener.currentViewChanged(this, oldView, newView, direction,
					newViewIsTopMost);
		}
	}

	@Override
	public void addListener(ViewControllerListener listener) {
		if (listener != null) {
			listenerList.add(listener);
		}
	}

	@Override
	public void removeListener(ViewControllerListener listener) {
		if (listener != null) {
			listenerList.remove(listener);
		}
	}

}
