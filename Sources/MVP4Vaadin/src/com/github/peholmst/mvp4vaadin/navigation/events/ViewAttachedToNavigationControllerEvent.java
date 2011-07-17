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
 * This event is fired when a new view is attached to a
 * {@link NavigationController}.
 * 
 * @author Petter Holmström
 * @since 1.0
 */
public class ViewAttachedToNavigationControllerEvent extends
		NavigationControllerEvent {

	private static final long serialVersionUID = -3726755858174360539L;

	private final View attachedView;

	/**
	 * Creates a new <code>ViewAttachedToNavigationControllerEvent</code>.
	 * 
	 * @param source
	 *            the navigation controller in which the event originally
	 *            occurred (must not be <code>null</code>).
	 * @param attachedView
	 *            the view that was attached (must not be <code>null</code>).
	 */
	public ViewAttachedToNavigationControllerEvent(NavigationController source,
			View attachedView) {
		super(source);
		this.attachedView = attachedView;
	}

	/**
	 * Returns the view that was attached (never <code>null</code>).
	 */
	public View getAttachedView() {
		return attachedView;
	}

}
