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

import java.util.EventObject;

/**
 * Base class for events that are fired by a {@link NavigationController}.
 * 
 * @see NavigationControllerListener
 * 
 * @author Petter Holmström
 * @since 1.0
 */
public abstract class NavigationControllerEvent extends EventObject {

	private static final long serialVersionUID = -8300699828827743503L;

	/**
	 * Creates a new <code>NavigationControllerEvent</code>.
	 * 
	 * @param source
	 *            the navigation controller in which the event originally
	 *            occurred (must not be <code>null</code>).
	 */
	public NavigationControllerEvent(NavigationController source) {
		super(source);
	}

	@Override
	public NavigationController getSource() {
		return (NavigationController) super.getSource();
	}

}
