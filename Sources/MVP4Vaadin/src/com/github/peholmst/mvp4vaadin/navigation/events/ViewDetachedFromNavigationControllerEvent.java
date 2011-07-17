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
 * This event is fired when a view is detached from a
 * {@link NavigationController}.
 * 
 * @author Petter Holmström
 * @since 1.0
 */
public class ViewDetachedFromNavigationControllerEvent extends
		NavigationControllerEvent {

	private static final long serialVersionUID = -1709673144712290714L;

	private final View detachedView;

	/**
	 * Creates a new <code>ViewDetachedFromNavigationControllerEvent</code>.
	 * 
	 * @param source
	 *            the navigation controller in which the event originally
	 *            occurred (must not be <code>null</code>).
	 * @param detachedView
	 *            the view that was detached (must not be <code>null</code>).
	 */
	public ViewDetachedFromNavigationControllerEvent(
			NavigationController source, View detachedView) {
		super(source);
		this.detachedView = detachedView;
	}

	/**
	 * Returns the view that was detached (never <code>null</code>).
	 */
	public View getDetachedView() {
		return detachedView;
	}
}
