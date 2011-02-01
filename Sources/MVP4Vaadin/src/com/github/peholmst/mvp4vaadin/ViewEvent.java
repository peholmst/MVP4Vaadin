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
package com.github.peholmst.mvp4vaadin;

import java.util.EventObject;

/**
 * Base class for events that are fired by a {@link View}. Views can use these
 * events to inform other objects of significant events, e.g. events that affect
 * the flow of the application itself (login, logout, move to another view,
 * etc.).
 * 
 * @see View#fireViewEvent(ViewEvent)
 * 
 * @author Petter Holmström
 * @since 1.0
 */
public abstract class ViewEvent extends EventObject {

	private static final long serialVersionUID = -1085851042904330047L;

	/**
	 * Creates a new <code>ViewEvent</code>.
	 * 
	 * @param source
	 *            the view in which the event originally occurred (must not be
	 *            <code>null</code>).
	 */
	public ViewEvent(View source) {
		super(source);
	}

	@Override
	public View getSource() {
		return (View) super.getSource();
	}

}
