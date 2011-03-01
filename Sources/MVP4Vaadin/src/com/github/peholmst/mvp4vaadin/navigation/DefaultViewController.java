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
 * @param <V>
 *            the super interface of all views handled by this controller. In
 *            most cases this is <code>ControllableView</code>, but if you want
 *            to extend MVP4Vaadin and use a more specialized view interface,
 *            you can specify it here as well.
 */
public class DefaultViewController<V extends ControllableView> implements
		ViewController<V> {

	private static final long serialVersionUID = -8268392494366653976L;

	private ViewProvider<V> viewProvider;

	// Protected access to make unit testing easier

	protected V currentView;

	protected int indexOfCurrentView = -1;

	protected Stack<V> viewStack = new Stack<V>();

	@Override
	public V getCurrentView() {
		return currentView;
	}

	@Override
	public V getFirstView() {
		return viewStack.isEmpty() ? null : viewStack.firstElement();
	}

	@Override
	public boolean goBack() {
		if (viewStack.size() <= 1) {
			// There is nothing to go back to
			return false;
		}
		V newView = viewStack.get(indexOfCurrentView - 1);
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
		V firstView = getFirstView();
		if (firstView == null) {
			return false;
		} else {
			return goToView(firstView, null);
		}
	}

	@Override
	public ViewProvider<V> getViewProvider() {
		return viewProvider;
	}

	@Override
	public void setViewProvider(ViewProvider<V> viewProvider) {
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
	protected V getViewFromProvider(String viewId, Map<String, Object> userData)
			throws IllegalStateException, IllegalArgumentException {
		if (viewId == null) {
			throw new IllegalArgumentException("null viewId");
		}
		if (getViewProvider() == null) {
			throw new IllegalStateException("null viewProvider");
		}
		V view = null;
		if (getViewProvider() instanceof InitializingViewProvider) {
			view = ((InitializingViewProvider<V>) getViewProvider()).getView(
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
	public boolean goToView(V view) {
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
	public boolean goToView(V view, String userDataKey, Object userDataValue) {
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
	public boolean goToView(V view, Map<String, Object> userData) {
		if (view == null) {
			throw new IllegalArgumentException("null view");
		}

		// Check if the view is already the current view
		if (currentView == view) {
			return false;
		}

		V oldView = currentView;

		int indexOfNewViewInStack = viewStack.indexOf(view);
		if (indexOfNewViewInStack != -1) {
			// The view already exists in the stack, check if we're moving
			// forward or backward
			if (indexOfNewViewInStack < indexOfCurrentView) {
				// We're moving backward
				V viewToHide = currentView;
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
			// We're adding a new view to the stack
			viewStack.push(view);
			setCurrentView(view, viewStack.size() - 1, userData,
					Direction.FORWARD);
		}
		return true;
	}

	protected void setCurrentView(V view, int indexOfCurrentView,
			Map<String, Object> userData, Direction direction) {
		V oldView = currentView;
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
	public List<V> getTrail() {
		return Collections.unmodifiableList(viewStack);
	}

	private final LinkedList<ViewControllerListener<V>> listenerList = new LinkedList<ViewControllerListener<V>>();

	/**
	 * TODO Document me!
	 * 
	 * @param oldView
	 * @param newView
	 * @param direction
	 * @param newViewIsTopMost
	 */
	@SuppressWarnings("unchecked")
	protected void fireCurrentViewChanged(V oldView, V newView,
			Direction direction, boolean newViewIsTopMost) {
		/*
		 * Create a clone of the listener list. This way, we prevent weird
		 * situations if any of the listeners register new listeners or remove
		 * existing ones.
		 */
		LinkedList<ViewControllerListener<V>> clonedList = (LinkedList<ViewControllerListener<V>>) listenerList
				.clone();
		for (ViewControllerListener<V> listener : clonedList) {
			listener.currentViewChanged(this, oldView, newView, direction,
					newViewIsTopMost);
		}
	}

	@Override
	public void addListener(ViewControllerListener<V> listener) {
		if (listener != null) {
			listenerList.add(listener);
		}
	}

	@Override
	public void removeListener(ViewControllerListener<V> listener) {
		if (listener != null) {
			listenerList.remove(listener);
		}
	}

}
