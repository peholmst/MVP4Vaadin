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

/**
 * This exception is thrown when a {@link ViewMap} cannot find a specific view.
 * 
 * @author Petter Holmström
 * @since 1.0
 */
public class NoSuchViewException extends RuntimeException {

	private static final long serialVersionUID = 538731553828478382L;

	public NoSuchViewException() {
		super();
	}

	public NoSuchViewException(String message) {
		super(message);
	}

}
