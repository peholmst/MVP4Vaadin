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

import com.github.peholmst.mvp4vaadin.navigation.ControllableView;

/**
 * This interface is designed to be used by controllable views that can be
 * identified by a unique identifier at runtime, e.g. a URL fragment. The 
 * view implementation should make sure that the identifier is in fact unique.
 * Several instances of the same view class may have different identifiers, depending
 * on how the application is designed.
 * 
 * @see IdentifiableViewController
 * 
 * @author Petter Holmström
 * @since 1.0
 * @deprecated Will be deleted in the 1.0 release
 */
@Deprecated
public interface IdentifiableControllableView extends ControllableView {

	/**
	 * Gets the identifier of the view.
	 * 
	 * @return the identifier (never <code>null</code>).
	 */
	public String getViewIdentifier();

}
