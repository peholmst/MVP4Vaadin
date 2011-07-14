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
package com.github.peholmst.mvp4vaadin.util;

/**
 * Exception thrown by an {@link Adaptable} class when an adapter is not
 * supported.
 * 
 * @author Petter Holmström
 * @since 1.0
 */
public class UnsupportedAdapterException extends IllegalArgumentException {

	private static final long serialVersionUID = -4150486999316473521L;

	private transient Class<?> adapterClass;

	/**
	 * Constructs a new <code>UnsupportedAdapterException</code>.
	 */
	public UnsupportedAdapterException() {
		super("The adapter class is not supported");
	}

	/**
	 * Constructs a new <code>UnsupportedAdapterException</code>.
	 * 
	 * @param adapterClass
	 *            the unsupported adapter class (must not be null).
	 */
	public UnsupportedAdapterException(Class<?> adapterClass) {
		super("The adapter class " + adapterClass.getName()
				+ " is not supported");
		this.adapterClass = adapterClass;
	}

	/**
	 * Returns the unsupported adapter class, or null if no class was provided.
	 */
	public Class<?> getAdapterClass() {
		return adapterClass;
	}
}
