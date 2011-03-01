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

/**
 * This is an extension of the {@link ControllableView} interface for
 * controllable views that themselves contain embedded view controllers. See the
 * {@link NestedControllersMaster} class for more information.
 * 
 * @author Petter Holmström
 * 
 * @param <V>
 *            the type of views that are handled by the embedded controller.
 */
public interface ControllableViewWithEmbeddedController<V extends ControllableView>
		extends ControllableView {

	/**
	 * Gets the view controller that is embedded into this particular view.
	 * 
	 * @return the embedded controller, or <code>null</code> if the view
	 *         currently has none.
	 */
	ViewController<V> getEmbeddedController();

}
