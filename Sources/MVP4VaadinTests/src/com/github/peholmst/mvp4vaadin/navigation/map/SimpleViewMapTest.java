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

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import com.github.peholmst.mvp4vaadin.View;
import com.github.peholmst.mvp4vaadin.testdata.MyTestView;
import com.github.peholmst.mvp4vaadin.testdata.MyTestViewImpl;

/**
 * Test case for {@link SimpleViewMap}.
 * 
 * @author Petter Holmström
 * @since 1.0
 */
public class SimpleViewMapTest {

	SimpleViewMap viewMap;

	@Before
	public void setUp() {
		this.viewMap = new SimpleViewMap();
	}

	@Test(expected = NoSuchViewException.class)
	public void getViewById_NotFound() {
		viewMap.getViewById("nonexistent");
	}

	@Test
	public void getViewById() {
		final View view = new MyTestViewImpl();
		viewMap.registerView("myView", view);
		assertSame(view, viewMap.getViewById("myView"));
	}

	@Test(expected = NoSuchViewException.class)
	public void getViewByClass_NotFound() {
		viewMap.getViewByClass(MyTestView.class);
	}

	@Test(expected = NoSuchViewException.class)
	public void getViewByClass_MoreThanOne() {
		viewMap.registerView("view1", new MyTestViewImpl());
		viewMap.registerView("view2", new MyTestViewImpl());
		viewMap.getViewByClass(MyTestView.class);
	}

	@Test
	public void getViewByClass() {
		final View view = new MyTestViewImpl();
		viewMap.registerView("myView", view);
		assertSame(view, viewMap.getViewByClass(MyTestView.class));
	}

	@Test
	public void containsView_NotFound() {
		assertFalse(viewMap.containsView("nonexistent"));
	}

	@Test
	public void containsViewOfClass_NotFound() {
		assertFalse(viewMap.containsView(MyTestView.class));
	}

	@Test
	public void containsViewOfClass_MoreThanOne() {
		viewMap.registerView("view1", new MyTestViewImpl());
		viewMap.registerView("view2", new MyTestViewImpl());
		assertFalse(viewMap.containsView(MyTestView.class));
	}

	@Test
	public void containsView() {
		viewMap.registerView("myView", new MyTestViewImpl());
		assertTrue(viewMap.containsView("myView"));
	}

	@Test
	public void containsViewOfClass() {
		viewMap.registerView("view1", new MyTestViewImpl());
		assertTrue(viewMap.containsView(MyTestView.class));
	}

	@Test
	public void unregisterView() {
		viewMap.registerView("myView", new MyTestViewImpl());
		assertTrue(viewMap.containsView("myView"));
		viewMap.unregisterView("myView");
		assertFalse(viewMap.containsView("myView"));
	}
}
