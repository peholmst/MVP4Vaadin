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
package com.github.peholmst.mvp4vaadin.navigation.incubation;

import com.github.peholmst.mvp4vaadin.navigation.ViewController;

/**
 * This is a specialized view controller interface for identifiable views. It
 * defines methods for navigating to existing views based on their view
 * identifiers.
 * 
 * @see IdentifiableControllableView
 * 
 * @author Petter Holmström
 * @deprecated Will be deleted in the 1.0 release
 */
@Deprecated
public interface IdentifiableViewController extends ViewController {

	/**
	 * Checks if there is a view somewhere in the stack that has the given
	 * identifier.
	 * 
	 * @see IdentifiableControllableView#getViewIdentifier()
	 * 
	 * @param viewIdentifier
	 *            the identifier to look for (must not be <code>null</code>).
	 * @return true if a view was found, false if not.
	 */
	boolean containsIdentifiableView(String viewIdentifier);

	/**
	 * Attempts to navigate to the view in the stack that has the given
	 * identifier.
	 * <p>
	 * This method will return true if the current view has changed. This may
	 * not necessarily mean that the current view has changed to the specified
	 * view, though. If any of the views in the stack refuses to hide, the
	 * current view will change to that view. This method only returns false if
	 * the current view did not change at all, or if no view with the specified
	 * identifier could be found.
	 * <p>
	 * Do not confuse this method with {@link ViewController#goToView(String)},
	 * which uses another kind of view ID.
	 * 
	 * @param viewIdentifier
	 *            the identifier of the view to navigate to (must not be
	 *            <code>null</code>).
	 * @return true if the current view was changed as a result of this method
	 *         call, false if not (or if there are no views at all).
	 */
	boolean goToIdentifiableView(String viewIdentifier);

}
