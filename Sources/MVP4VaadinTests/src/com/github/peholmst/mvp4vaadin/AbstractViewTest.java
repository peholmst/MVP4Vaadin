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

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.github.peholmst.mvp4vaadin.events.DisplayNameChangedViewEvent;
import com.github.peholmst.mvp4vaadin.testdata.MyTestPresenter;
import com.github.peholmst.mvp4vaadin.testdata.MyTestViewImpl;

/**
 * Test case for {@link AbstractView}.
 * 
 * @author Petter Holmström
 * @since 1.0
 */
public class AbstractViewTest {
	
	MyTestPresenter presenter;
	
	MyTestViewImpl view;
	
	@Before
	public void setUp() {
		presenter = new MyTestPresenter();
		view = new MyTestViewImpl();
	}
	
	@Test(expected = UnsupportedOperationException.class)
	public void createPresenter() {
		view.createPresenter();
	}
	
	@Test
	public void setGetDisplayName() {
		view.setDisplayName("displayName");
		assertEquals("displayName", view.getDisplayName());
	}
	
	@SuppressWarnings("deprecation")
	@Test
	public void setGetViewDescription() {
		view.setViewDescription("description");
		assertEquals("description", view.getViewDescription());
		assertEquals("description", view.getDescription());
	}
	
	@Test
	public void setGetPresenter() {
		view.setPresenter(presenter);
		assertSame(presenter, view.getPresenter());
	}
	
	@Test
	public void init() {
		view.setPresenter(presenter);
		view.init();
		
		assertTrue(view.isInitialized());
		assertTrue(presenter.initialized);
	}
	
	@Test
	public void fireViewEvent() {
		final List<ViewEvent> receivedEvents = new ArrayList<ViewEvent>();
		@SuppressWarnings("serial")
		final ViewListener listener = new ViewListener() {

			@Override
			public void handleViewEvent(ViewEvent event) {
				receivedEvents.add(event);
			}			
		};
		
		final ViewEvent event = new DisplayNameChangedViewEvent(view, null, "blah");
		
		view.addListener(listener);
		view.fireViewEvent(event);
		view.removeListener(listener);
		view.fireViewEvent(event);
		
		assertEquals(1, receivedEvents.size());
		assertSame(event, receivedEvents.get(0));
	}
	
	@SuppressWarnings("serial")
	@Test
	public void initConstructor() {
		view = new MyTestViewImpl(true) {
			@Override
			public MyTestPresenter createPresenter() {
				return presenter;
			}
		};
		
		assertTrue(view.isInitialized());
		assertTrue(presenter.initialized);
		assertSame(presenter, view.getPresenter());
	}
}
