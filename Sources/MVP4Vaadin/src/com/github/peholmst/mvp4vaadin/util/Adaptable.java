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
 * Interface to be implemented by classes that can be adapted to other classes.
 * 
 * @author Petter Holmström
 * @since 1.0
 */
public interface Adaptable extends java.io.Serializable {

	/**
	 * Checks whether the adaptable supports the specified adapter class.
	 * 
	 * @param adapterClass
	 *            the adapter class (must not be <code>null</code>).
	 * @return true if the adapter class is supported, false otherwise.
	 */
	boolean supportsAdapter(Class<?> adapterClass);

	/**
	 * Adapts the adaptable to the specified adapter class.
	 * 
	 * @param adapterClass
	 *            the adapter class.
	 * @return an instance of the adapter class (must not be <code>null</code>).
	 * @throws UnsupportedAdapterException
	 *             if the adapter class is not supported.
	 */
	<T> T adapt(Class<T> adapterClass) throws UnsupportedAdapterException;

}
