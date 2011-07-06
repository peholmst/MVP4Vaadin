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

import org.junit.Before;
import org.junit.Test;

import static org.easymock.EasyMock.*;
import static org.junit.Assert.*;

import com.github.peholmst.mvp4vaadin.events.InitializedViewEvent;
import com.github.peholmst.mvp4vaadin.testdata.MyTestPresenter;
import com.github.peholmst.mvp4vaadin.testdata.MyTestView;

/**
 * Test case for {@link Presenter}.
 * 
 * @author Petter Holmström
 * @since 1.0
 */
public class PresenterTest {
	
	MyTestView viewMock;
	
	MyTestPresenter presenter;
	
	@Before
	public void setUp() {
		viewMock = createMock(MyTestView.class);
		presenter = new MyTestPresenter(viewMock);
	}
	
	@Test(expected = IllegalStateException.class)
	public void getViewNoView() {
		presenter = new MyTestPresenter();
		presenter.getView();
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void constructorWithNullView() {
		presenter = new MyTestPresenter(null);
	}
	
	@Test
	public void getView() {
		assertSame(viewMock, presenter.getView());
	}
	
	@Test
	public void setGetView() {
		presenter = new MyTestPresenter();
		presenter.setView(viewMock);
		assertSame(viewMock, presenter.getView());
	}
	
	@Test
	public void fireViewEvent() {
		final ViewEvent event = new InitializedViewEvent(viewMock);
		viewMock.fireViewEvent(event);
		replay(viewMock);
		
		presenter.fireViewEvent(event);
		
		verify(viewMock);
	}	
	
}