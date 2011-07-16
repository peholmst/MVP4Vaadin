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

import java.util.List;

import com.github.peholmst.mvp4vaadin.View;

/**
 * TODO Document me!
 * 
 * @author Petter Holmström
 * @since 1.0
 */
public interface NavigationController extends java.io.Serializable {

	/**
	 * Enumeration defining the result of the
	 * {@link NavigationController#navigate(NavigationRequest)} method.
	 * 
	 * @author Petter Holmström
	 * @since 1.0
	 */
	enum NavigationResult {
		/**
		 * The navigation request was prevented. The current view was not
		 * changed.
		 */
		PREVENTED,

		/**
		 * The navigation request succeeded. The current view was changed to the
		 * view addressed by the navigation request.
		 */
		SUCCEEDED,
		/**
		 * The navigation request was interrupted. The current view did change,
		 * but not to the view addressed by the navigation request.
		 */
		INTERRUPTED
	}

	/**
	 * 
	 * @param request
	 *            the request indicating to which view the controller should
	 *            navigate (must not be <code>null</code>).
	 * @return the result of the navigation (see {@link NavigationRequest} for
	 *         more information).
	 */
	NavigationResult navigate(NavigationRequest request);

	/**
	 * 
	 * @return
	 */
	boolean navigateBack();

	/**
	 * 
	 * @return
	 */
	List<View> getViewStack();

	/**
	 * 
	 * @return
	 */
	View getCurrentView();

	/**
	 * 
	 * @return
	 */
	View getFirstView();

	/**
	 * 
	 * @return
	 */
	boolean isEmpty();

	/**
	 * 
	 * @return
	 */
	boolean clear();

	/**
	 * 
	 * @param listener
	 */
	void addListener(NavigationControllerListener listener);

	/**
	 * 
	 * @param listener
	 */
	void removeListener(NavigationControllerListener listener);
}
