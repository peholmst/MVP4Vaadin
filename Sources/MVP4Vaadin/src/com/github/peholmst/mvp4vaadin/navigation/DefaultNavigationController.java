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
import java.util.EmptyStackException;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Stack;

import com.github.peholmst.mvp4vaadin.View;
import com.github.peholmst.mvp4vaadin.navigation.events.CurrentNavigationControllerViewChangedEvent;
import com.github.peholmst.mvp4vaadin.navigation.events.ViewAttachedToNavigationControllerEvent;
import com.github.peholmst.mvp4vaadin.navigation.events.ViewDetachedFromNavigationControllerEvent;
import com.github.peholmst.stuff4vaadin.visitor.VisitableList;
import com.github.peholmst.stuff4vaadin.visitor.Visitor;

/**
 * This is the default implementation of the {@link NavigationController}
 * interface. Create a new instance using the default constructor and attach
 * views using the {@link #navigate(NavigationRequest)} method. You can use the
 * {@link NavigationRequestBuilder} to create {@link NavigationRequest}s.
 * 
 * @author Petter Holmström
 * @since 1.0
 */
public class DefaultNavigationController implements NavigationController {

	private static final long serialVersionUID = 6838003395877804584L;

	private final Stack<View> viewStack = new Stack<View>();

	private final VisitableList<NavigationControllerListener> listeners = new VisitableList<NavigationControllerListener>();

	@Override
	public NavigationResult navigate(NavigationRequest request) {
		final View fromView = getCurrentView();

		final int differenceIndex = getIndexOfFirstDifferenceFromStack(request);
		if (differenceIndex == viewStack.size()) {
			// We're attaching new stacks to the view
			attachRemainingViewsInRequest(request);
			if (fromView != null) {
				invokeNavigatedFromViewOnView(fromView);
			}
		} else {
			// We have to detach some views (including the current view) before
			// we can attach new views
			final NavigationResult result = detachViewsFromStack(differenceIndex);
			if (result.equals(NavigationResult.SUCCEEDED)) {
				attachRemainingViewsInRequest(request);
			} else {
				if (result.equals(NavigationResult.INTERRUPTED)) {
					fireEvent(new CurrentNavigationControllerViewChangedEvent(
							this, fromView, getCurrentView()));
				}
				return result;
			}
		}
		invokeNavigatedToViewOnCurrentView(request.getParams(), fromView);
		fireEvent(new CurrentNavigationControllerViewChangedEvent(this,
				fromView, getCurrentView()));
		return NavigationResult.SUCCEEDED;
	}

	/**
	 * Compares the request path to the view stack. The returned value is the
	 * index of the first element that differs between these two.
	 */
	private int getIndexOfFirstDifferenceFromStack(NavigationRequest request) {
		final List<View> path = request.getPath();
		for (int i = 0; i < viewStack.size(); ++i) {
			final View viewInStack = viewStack.get(i);
			if (i < path.size()) {
				final View viewInPath = path.get(i);
				if (!viewInStack.equals(viewInPath)) {
					return i;
				}
			} else {
				return i;
			}
		}
		return viewStack.size();
	}

	/**
	 * If the request path contains more elements than the view stack, the
	 * remaining views from the path are added to the stack. No comparison of
	 * the stack and the request path is made.
	 */
	private void attachRemainingViewsInRequest(NavigationRequest request) {
		for (int i = viewStack.size(); i < request.getPath().size(); ++i) {
			final View viewInPath = request.getPath().get(i);
			attach(viewInPath);
		}
	}

	/**
	 * Adds the view to the view stack.
	 */
	private void attach(View view) {
		viewStack.add(view);
		if (view.supportsAdapter(NavigationControllerCallback.class)) {
			view.adapt(NavigationControllerCallback.class)
					.attachedToController(this);
		}
		fireEvent(new ViewAttachedToNavigationControllerEvent(this, view));
	}

	/**
	 * Detaches all the views from the start, starting from the top-most view
	 * and going downwards until the view at
	 * <code>indexOfFinalViewToDetach</code> has been detached.
	 * 
	 * @return {@link NavigationResult#PREVENTED} if the top-most view aborted
	 *         the operation, {@link NavigationResult#INTERRUPTED} if any of the
	 *         other views aborted the operation, or
	 *         {@link NavigationResult#SUCCEEDED} if all the views were
	 *         detached.
	 */
	private NavigationResult detachViewsFromStack(int indexOfFinalViewToDetach) {
		boolean currentViewRemoved = false;
		while (viewStack.size() > indexOfFinalViewToDetach) {
			if (!detachTopmostView()) {
				if (currentViewRemoved) {
					return NavigationResult.INTERRUPTED;
				} else {
					return NavigationResult.PREVENTED;
				}
			}
			currentViewRemoved = true;
		}
		return NavigationResult.SUCCEEDED;
	}

	/**
	 * Attempts to remove the top-most view from the stack. Returns true on
	 * success and false on failure.
	 */
	private boolean detachTopmostView() {
		final View view = viewStack.peek();
		if (view.supportsAdapter(NavigationControllerCallback.class)) {
			if (!view.adapt(NavigationControllerCallback.class)
					.detachingFromController(this)) {
				return false;
			}
			viewStack.pop();
			view.adapt(NavigationControllerCallback.class)
					.detachedFromController(this);
		} else {
			viewStack.pop();
		}
		fireEvent(new ViewDetachedFromNavigationControllerEvent(this, view));
		return true;
	}

	private void invokeNavigatedToViewOnCurrentView(Map<String, Object> params,
			View fromView) {
		if (!viewStack.isEmpty()) {
			if (getCurrentView().supportsAdapter(
					NavigationControllerCallback.class)) {
				getCurrentView().adapt(NavigationControllerCallback.class)
						.navigatedToView(params, fromView);
			}
		}
	}

	private void invokeNavigatedFromViewOnView(View fromView) {
		if (fromView.supportsAdapter(NavigationControllerCallback.class)) {
			fromView.adapt(NavigationControllerCallback.class)
					.navigatedFromView(getCurrentView());
		}
	}

	@Override
	public boolean navigateBack() {
		if (isEmpty()) {
			return false;
		} else if (viewStack.size() == 1) {
			return clear() == NavigationResult.SUCCEEDED;
		} else {
			final NavigationRequest request = NavigationRequestBuilder
					.newInstance().startWithPathToPreviousView(this)
					.buildRequest();
			return navigate(request) == NavigationResult.SUCCEEDED;
		}
	}

	@Override
	public List<View> getViewStack() {
		return Collections.unmodifiableList(viewStack);
	}

	/**
	 * This method is intended for unit testing only! Do not use for anything
	 * else!
	 */
	Stack<View> getModifiableViewStack() {
		return viewStack;
	}

	@Override
	public View getCurrentView() {
		try {
			return viewStack.peek();
		} catch (EmptyStackException e) {
			return null;
		}
	}

	@Override
	public View getFirstView() {
		try {
			return viewStack.firstElement();
		} catch (NoSuchElementException e) {
			return null;
		}
	}

	@Override
	public boolean isEmpty() {
		return viewStack.isEmpty();
	}

	@Override
	public boolean containsMoreThanOneElement() {
		return viewStack.size() > 1;
	}
	
	@Override
	public NavigationResult clear() {
		final View oldView = getCurrentView();
		final NavigationResult result = detachViewsFromStack(0);
		if (getCurrentView() != oldView) {
			fireEvent(new CurrentNavigationControllerViewChangedEvent(this,
					oldView, getCurrentView()));
		}
		return result;
	}

	/**
	 * Notifies all registered listeners of the specified event.
	 */
	protected void fireEvent(final NavigationControllerEvent event) {
		listeners.visitItems(new Visitor<NavigationControllerListener>() {

			@Override
			public void visit(NavigationControllerListener visitable) {
				visitable.handleNavigationControllerEvent(event);
			}
		});
	}

	@Override
	public void addListener(NavigationControllerListener listener) {
		if (listener != null) {
			listeners.add(listener);
		}
	}

	@Override
	public void removeListener(NavigationControllerListener listener) {
		if (listener != null) {
			listeners.remove(listener);
		}
	}

}
