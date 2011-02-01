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

import com.vaadin.ui.ComponentContainer;

/**
 * This is an extended version of the {@link View} interface that ties the View
 * to the Vaadin Framework and provides a method for plugging in the View into a
 * Vaadin application window (or other Vaadin components).
 * 
 * @author Petter Holmström
 * @since 1.0
 */
public interface VaadinView extends View {

	/**
	 * Gets the visual component that constitutes the view. If this method is
	 * called before the view has been properly initialized, the results are
	 * undefined.
	 * 
	 * @see View#init()
	 * @see View#isInitialized()
	 * @return the view component.
	 */
	ComponentContainer getViewComponent();
}
