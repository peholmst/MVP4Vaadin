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
package com.github.peholmst.mvp4vaadin.navigation.map;

import com.github.peholmst.mvp4vaadin.navigation.NavigationRequestBuilder;

/**
 * Helper class for creating {@link NavigationRequestBuilder}s that use a
 * {@link ViewMapPathBuilder}.
 * 
 * @author Petter Holmström
 * @since 1.0
 */
public final class ViewMapNavigationRequestBuilder {

	private ViewMapNavigationRequestBuilder() {
	}

	/**
	 * Returns a new navigation request builder that uses a
	 * {@link ViewMapPathBuilder}.
	 */
	public static NavigationRequestBuilder<ViewMapPathBuilder> newInstance() {
		return NavigationRequestBuilder.newInstance(ViewMapPathBuilder.class);
	}
}
