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

import org.easymock.Capture;
import org.junit.Before;
import org.junit.Test;

import static org.easymock.EasyMock.*;
import static org.junit.Assert.*;

import com.github.peholmst.mvp4vaadin.events.DescriptionChangedViewEvent;
import com.github.peholmst.mvp4vaadin.events.DisplayNameChangedViewEvent;
import com.github.peholmst.mvp4vaadin.events.InitializedViewEvent;
import com.github.peholmst.mvp4vaadin.testdata.MyTestPresenter;
import com.github.peholmst.mvp4vaadin.testdata.MyTestView;

/**
 * Test case for {@link ViewDelegate}.
 * 
 * @author Petter Holmström
 * @since 1.0
 */
public class ViewDelegateTest {

	ViewDelegateOwner<MyTestView, MyTestPresenter> delegateOwnerMock;
	ViewListener viewListenerMock;
	ViewDelegate<MyTestView, MyTestPresenter> delegate;

	@SuppressWarnings("unchecked")
	@Before
	public void setUp() {
		delegateOwnerMock = createMock(ViewDelegateOwner.class);
		viewListenerMock = createMock(ViewListener.class);
		delegate = new ViewDelegate<MyTestView, MyTestPresenter>(
				delegateOwnerMock);
		delegate.addListener(viewListenerMock);
	}

	@Test
	public void setGetDisplayName() {
		final Capture<ViewEvent> viewEventCapture = new Capture<ViewEvent>();
		viewListenerMock.handleViewEvent(capture(viewEventCapture));
		replay(viewListenerMock);

		delegate.setDisplayName("new display name");

		assertEquals("new display name", delegate.getDisplayName());

		final DisplayNameChangedViewEvent viewEvent = (DisplayNameChangedViewEvent) viewEventCapture
				.getValue();

		assertNull(viewEvent.getOldDisplayName());
		assertEquals("new display name", viewEvent.getNewDisplayName());
		assertSame(delegateOwnerMock, viewEvent.getSource());
	}

	@SuppressWarnings("deprecation")
	@Test
	public void setGetViewDescription() {
		final Capture<ViewEvent> viewEventCapture = new Capture<ViewEvent>();
		viewListenerMock.handleViewEvent(capture(viewEventCapture));
		replay(viewListenerMock);

		delegate.setViewDescription("new description");

		assertEquals("new description", delegate.getViewDescription());

		final DescriptionChangedViewEvent viewEvent = (DescriptionChangedViewEvent) viewEventCapture
				.getValue();

		assertNull(viewEvent.getOldDescription());
		assertEquals("new description", viewEvent.getNewDescription());
		assertSame(delegateOwnerMock, viewEvent.getSource());
		assertEquals(delegate.getViewDescription(), delegate.getDescription());

		verify(viewListenerMock);
	}

	@Test
	public void initWithNullPresenter() {
		final Capture<ViewEvent> viewEventCapture = new Capture<ViewEvent>();
		viewListenerMock.handleViewEvent(capture(viewEventCapture));

		final MyTestPresenter presenter = new MyTestPresenter();
		expect(delegateOwnerMock.createPresenter()).andReturn(presenter);
		delegateOwnerMock.initView();
		delegateOwnerMock.finalizeInitialization();

		replay(viewListenerMock, delegateOwnerMock);

		assertFalse(delegate.isInitialized());

		delegate.init();

		assertTrue(delegate.isInitialized());
		assertTrue(presenter.initialized);
		assertSame(presenter, delegate.getPresenter());
		assertTrue(viewEventCapture.getValue() instanceof InitializedViewEvent);

		verify(viewListenerMock, delegateOwnerMock);
	}

	@Test
	public void initWithSetPresenter() {
		final Capture<ViewEvent> viewEventCapture = new Capture<ViewEvent>();
		viewListenerMock.handleViewEvent(capture(viewEventCapture));

		final MyTestPresenter presenter = new MyTestPresenter();
		delegateOwnerMock.initView();
		delegateOwnerMock.finalizeInitialization();

		replay(viewListenerMock, delegateOwnerMock);

		delegate.setPresenter(presenter);
		delegate.init();

		assertTrue(delegate.isInitialized());
		assertTrue(presenter.initialized);
		assertSame(presenter, delegate.getPresenter());
		assertTrue(viewEventCapture.getValue() instanceof InitializedViewEvent);

		verify(viewListenerMock, delegateOwnerMock);
	}

	@Test(expected = IllegalStateException.class)
	public void initWhenAlreadyInitialized() {
		delegate.setInitialized(true);
		delegate.init();
	}

	@Test(expected = IllegalStateException.class)
	public void setPresenterWhenAlreadyInitialized() {
		final MyTestPresenter presenter = new MyTestPresenter();
		delegate.setInitialized(true);
		delegate.setPresenter(presenter);
	}

	@Test
	public void fireNullViewEvent() {
		replay(viewListenerMock);

		delegate.fireViewEvent(null);

		verify(viewListenerMock);
	}

	@Test
	public void removeListener() {
		replay(viewListenerMock);

		delegate.removeListener(viewListenerMock);
		delegate.setDisplayName("display name that won't be sent to any listeners");

		verify(viewListenerMock);
	}
}
