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

import java.util.Map;

import com.github.peholmst.mvp4vaadin.View;

/**
 * This is an extended version of the {@link View} interface that is designed to
 * be used together with a {@link ViewController}. Please see the JavaDocs for
 * the controller for more information.
 * 
 * @author Petter Holmström
 * @since 1.0
 */
public interface ControllableView extends View {

	/**
	 * This method is called by the view controller when the view is opened and
	 * added to the view stack. If the view will not be used outside of the view
	 * controller, it may create the GUI components and allocate resources in
	 * this method.
	 * 
	 * @see #init()
	 * 
	 * @param viewController
	 *            the view controller (must not be <code>null</code>).
	 * @param userData
	 *            a map of user-definable parameters (may be <code>null</code>).
	 */
	void showView(ViewController viewController, Map<String, Object> userData);

	/**
	 * This method is called by the view controller before the view is closed
	 * and removed from the view stack. The view can prevent itself from being
	 * closed by returning false, e.g. if the user has unsaved changes.
	 * <p>
	 * F the view will not be used outside of the view controller, it may
	 * dispose of any UI resources in this method. This is, however, not a
	 * requirement.
	 * 
	 * @return true if it is OK to close the view, false if the view should
	 *         remain open.
	 */
	boolean okToClose();
}
