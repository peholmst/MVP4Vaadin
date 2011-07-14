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

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import org.junit.Test;

/**
 * Test case for {@link AdaptableSupport}.
 * 
 * @author Petter Holmström
 * @since 1.0
 */
public class AdaptableSupportTest {

	public static class TransientAdapter {

	}

	@SuppressWarnings("serial")
	public static class SerializableAdapter implements java.io.Serializable {

	}

	@Test
	public void registerAdapter() {
		final AdaptableSupport support = new AdaptableSupport();
		final SerializableAdapter adapter = new SerializableAdapter();
		support.registerAdapter(SerializableAdapter.class, adapter);
		assertTrue(support.supportsAdapter(SerializableAdapter.class));
	}

	@Test
	public void unregisterAdapter() {
		final AdaptableSupport support = new AdaptableSupport();
		final SerializableAdapter adapter = new SerializableAdapter();
		support.registerAdapter(SerializableAdapter.class, adapter);
		support.unregisterAdapter(SerializableAdapter.class);
		assertFalse(support.supportsAdapter(SerializableAdapter.class));
	}

	@Test
	public void adapt() {
		final AdaptableSupport support = new AdaptableSupport();
		final SerializableAdapter adapter = new SerializableAdapter();
		support.registerAdapter(SerializableAdapter.class, adapter);
		assertSame(adapter, support.adapt(SerializableAdapter.class));
	}

	@Test(expected = UnsupportedAdapterException.class)
	public void adaptUnsupportedAdapter() {
		final AdaptableSupport support = new AdaptableSupport();
		support.adapt(SerializableAdapter.class);
	}

	@Test
	public void serializationWithSerializableAdapter() throws Exception {
		final AdaptableSupport support = new AdaptableSupport();
		support.registerAdapter(SerializableAdapter.class,
				new SerializableAdapter());

		final AdaptableSupport newSupport = serializeAndDeserialize(support);
		assertTrue(newSupport.supportsAdapter(SerializableAdapter.class));
	}

	@Test
	public void serializationWithTransientAdapter() throws Exception {
		final AdaptableSupport support = new AdaptableSupport();
		support.registerAdapter(TransientAdapter.class, new TransientAdapter());

		final AdaptableSupport newSupport = serializeAndDeserialize(support);
		assertFalse(newSupport.supportsAdapter(TransientAdapter.class));
	}

	@SuppressWarnings("unchecked")
	private <T extends java.io.Serializable> T serializeAndDeserialize(T object)
			throws IOException, ClassNotFoundException {
		final ByteArrayOutputStream bos = new ByteArrayOutputStream();
		final ObjectOutputStream oos = new ObjectOutputStream(bos);
		oos.writeObject(object);
		final ByteArrayInputStream bis = new ByteArrayInputStream(
				bos.toByteArray());
		final ObjectInputStream ois = new ObjectInputStream(bis);
		return (T) ois.readObject();
	}
}
