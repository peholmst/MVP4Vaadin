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

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Implementation of the {@link Adaptable} interface that can be used either as
 * a super class or as a delegate. Adapters are registered using the
 * {@link #registerAdapter(Class, Object)} method.
 * <p>
 * This class is serializable. When instances of this class are serialized, all
 * serializable adapters will also be serialized. Any transient adapters will be
 * left out.
 * 
 * @author Petter Holmström
 * @since 1.0
 */
public class AdaptableSupport implements Adaptable {

	private static final long serialVersionUID = -7780952614009866736L;

	private static class AdapterEntry implements Serializable {
		private static final long serialVersionUID = -3833719181978897176L;
		private transient Object adapter;
		private Serializable serializableAdapter;

		public AdapterEntry(Object adapter) {
			this.adapter = adapter;
			if (adapter instanceof Serializable) {
				serializableAdapter = (Serializable) adapter;
			}
		}

		public Object getAdapter() {
			return adapter;
		}

		private void readObject(ObjectInputStream in) throws IOException,
				ClassNotFoundException {
			in.defaultReadObject();
			adapter = serializableAdapter;
		}
	}

	private Map<String, AdapterEntry> adapterMap = new HashMap<String, AdapterEntry>();

	/**
	 * Registers the specified adapter of the specified adapter class. If an
	 * adapter of the same class has already been registered, it will be
	 * replaced.
	 * 
	 * @param adapterClass
	 *            the adapter class (must not be null).
	 * @param adapter
	 *            the adapter (must not be null).
	 */
	public <T> void registerAdapter(Class<T> adapterClass, T adapter) {
		if (adapterClass == null) {
			throw new IllegalArgumentException("adapterClass must not be null");
		}
		if (adapter == null) {
			throw new IllegalArgumentException("adapter must not be null");
		}
		adapterMap.put(adapterClass.getName(), new AdapterEntry(adapter));
	}

	/**
	 * Unregisters the specified adapter. If no such adapter has been
	 * registered, nothing happens.
	 * 
	 * @param adapterClass
	 *            the adapter class (must not be null).
	 */
	public void unregisterAdapter(Class<?> adapterClass) {
		if (adapterClass == null) {
			throw new IllegalArgumentException("adapterClass must not be null");
		}
		adapterMap.remove(adapterClass.getName());

	}

	public boolean supportsAdapter(Class<?> adapterClass) {
		return adapterMap.keySet().contains(adapterClass.getName());
	}

	public <T> T adapt(Class<T> adapterClass)
			throws UnsupportedAdapterException {
		if (adapterClass == null) {
			throw new IllegalArgumentException("adapterClass must not be null");
		}
		final AdapterEntry adapterEntry = adapterMap
				.get(adapterClass.getName());
		if (adapterEntry == null) {
			throw new UnsupportedAdapterException(adapterClass);
		}
		return adapterClass.cast(adapterEntry.getAdapter());
	}

	private void readObject(ObjectInputStream in) throws IOException,
			ClassNotFoundException {
		in.defaultReadObject();
		/*
		 * Remove all transient adapters from the map
		 */
		final Set<String> classNames = new HashSet<String>();
		classNames.addAll(adapterMap.keySet());
		for (String className : classNames) {
			final AdapterEntry entry = adapterMap.get(className);
			if (entry.getAdapter() == null) {
				adapterMap.remove(className);
			}
		}
	}

}
