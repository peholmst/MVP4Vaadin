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
import java.util.Map;

import com.github.peholmst.mvp4vaadin.View;

/**
 * This interface defines a navigation request that is used to instruct a
 * navigation controller to navigate to a specific view.
 * 
 * @see NavigationController
 * 
 * @author Petter Holmström
 * @since 1.0
 */
public interface NavigationRequest extends java.io.Serializable {

	/**
	 * Returns a map of user definable parameters that will be passed to the
	 * view. If no parameters have been defined, an empty map is returned.
	 * 
	 * @see NavigationControllerCallback#navigatedToView(Map, View)
	 */
	Map<String, Object> getParams();

	/**
	 * Returns the path to the destination view. The last view in the path is
	 * the destination view.
	 */
	List<View> getPath();

}
