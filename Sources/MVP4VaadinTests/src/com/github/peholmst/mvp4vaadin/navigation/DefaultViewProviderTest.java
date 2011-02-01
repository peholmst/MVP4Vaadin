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

import static org.easymock.EasyMock.*;
import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

/**
 * Test case for {@link DefaultViewProvider}.
 * 
 * @author Petter Holmström
 * @since 1.0
 */
public class DefaultViewProviderTest {

	DefaultViewProvider viewProvider;
	ControllableView view;

	@Before
	public void setUp() {
		viewProvider = new DefaultViewProvider();
		view = createMock(ControllableView.class);
	}

	@Test
	public void testAddPreinitializedView_NoId() {
		expect(view.isInitialized()).andReturn(true);
		replay(view);

		viewProvider.addPreinitializedView(view);
		assertSame(view, viewProvider.viewMap.get(view.getClass().getName()));

		verify(view);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testAddPreinitializedView_NoId_NullView() {
		viewProvider.addPreinitializedView(null);
	}

	@Test(expected = IllegalStateException.class)
	public void testAddPreinitializedView_NoId_NoInitializedView() {
		expect(view.isInitialized()).andReturn(false);
		replay(view);

		viewProvider.addPreinitializedView(view);
	}

	@Test
	public void testAddPreinitializedView() {
		expect(view.isInitialized()).andReturn(true);
		replay(view);

		viewProvider.addPreinitializedView(view, "myView");
		assertSame(view, viewProvider.viewMap.get("myView"));

		verify(view);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testAddPreinitializedView_NullView() {
		viewProvider.addPreinitializedView(null, "myView");
	}

	@Test(expected = IllegalArgumentException.class)
	public void testAddPreinitializedView_NullId() {
		viewProvider.addPreinitializedView(view, null);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testAddPreinitializedView_EmptyId() {
		viewProvider.addPreinitializedView(view, "");
	}

	@Test(expected = IllegalStateException.class)
	public void testAddPreinitializedView_NoInitializedView() {
		expect(view.isInitialized()).andReturn(false);
		replay(view);

		viewProvider.addPreinitializedView(view, "myView");
	}

	@Test
	public void testGetView() {
		viewProvider.viewMap.put("myView", view);
		assertSame(view, viewProvider.getView("myView"));
	}

	@Test
	public void testGetView_NullId() {
		assertNull(viewProvider.getView((String) null));
	}

	@Test
	public void testGetView_Class() {
		viewProvider.viewMap.put(view.getClass().getName(), view);
		assertSame(view, viewProvider.getView(view.getClass()));
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Test
	public void testGetView_Class_NullClass() {
		assertNull(viewProvider.getView((Class) null));
	}

	@Test
	public void testGetView_Class_NoHit() {
		assertNull(viewProvider.getView(view.getClass()));
	}

	@Test
	public void testGetViewId() {
		viewProvider.viewMap.put("myView", view);
		assertEquals("myView", viewProvider.getViewId(view));
	}

	@Test
	public void testGetViewId_NullView() {
		assertNull(viewProvider.getViewId(null));
	}

	@Test
	public void testGetViewId_NotFound() {
		assertNull(viewProvider.getViewId(view));
	}
}
