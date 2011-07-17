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
package com.github.peholmst.mvp4vaadin.navigation.events;

import com.github.peholmst.mvp4vaadin.View;
import com.github.peholmst.mvp4vaadin.navigation.NavigationController;
import com.github.peholmst.mvp4vaadin.navigation.NavigationControllerEvent;

/**
 * This event is fired when the current view of a {@link NavigationController}
 * changes.
 * 
 * @author Petter Holmström
 * @since 1.0
 */
public class CurrentNavigationControllerViewChangedEvent extends
		NavigationControllerEvent {

	private static final long serialVersionUID = -8647961113080534344L;

	private final View oldView;

	private final View newView;

	/**
	 * Creates a new <code>CurrentNavigationControllerViewChangedEvent</code>.
	 * 
	 * @param source
	 *            the navigation controller in which the event originally
	 *            occurred (must not be <code>null</code>).
	 * @param oldView
	 *            the view that was the current view before it changed, or
	 *            <code>null</code> if the controller was empty.
	 * @param newView
	 *            the view that is the current view, or <code>null</code> if the
	 *            controller is now empty.
	 */
	public CurrentNavigationControllerViewChangedEvent(
			NavigationController source, View oldView, View newView) {
		super(source);
		this.oldView = oldView;
		this.newView = newView;
	}

	/**
	 * Returns the new view, or <code>null</code> if the controller is empty.
	 */
	public View getNewView() {
		return newView;
	}

	/**
	 * Returns the old view, or <code>null</code> if the controller was empty.
	 */
	public View getOldView() {
		return oldView;
	}

}
