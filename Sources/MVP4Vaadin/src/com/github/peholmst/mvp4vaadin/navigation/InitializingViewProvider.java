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

/**
 * The initializing view provider is intended to be implemented by view providers
 * that actually work more like view factories in that they create new instances
 * of the views when they are requested.
 * 
 * @author Petter Holmström
 * @since 1.0
 */
@Deprecated
public interface InitializingViewProvider extends ViewProvider {

	/**
	 * Gets the view with the specified ID. If the ID is null, <code>null</code>
	 * is returned.
	 * <p>
	 * Any user data passed to the corresponding <code>goToView(..)</code>
	 * -method of the {@link ViewController} is also passed in the
	 * <code>userData</code> parameter. This may be useful if the user data is
	 * required when the view is constructed (as opposed to when it is shown).
	 * If no user data is available, the <code>userData</code> parameter is
	 * <code>null</code>, in which case this method should behave like
	 * {@link ViewProvider#getView(String)}.
	 * 
	 * @see ViewController#goToView(String, Map)
	 * @see ViewController#goToView(String, String, Object)
	 * 
	 * @param viewId
	 *            the ID of the view to fetch.
	 * @param userData
	 *            a map of user specified data, may be <code>null</code>.
	 * @return the view, or <code>null</code> if not found.
	 */
	ControllableView getView(String viewId, Map<String, Object> userData);
}
