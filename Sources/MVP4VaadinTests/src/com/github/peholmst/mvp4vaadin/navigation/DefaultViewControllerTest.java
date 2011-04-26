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

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.util.HashMap;

import org.junit.Before;
import org.junit.Test;

import com.github.peholmst.mvp4vaadin.navigation.ControllableView.HideOperation;

/**
 * Test case for {@link DefaultViewController}.
 * 
 * @author Petter Holmström
 * @since 1.0
 */
public class DefaultViewControllerTest {

	DefaultViewController controller;
	ControllableView view;
	ControllableView view2;
	ControllableView view3;
	ControllableView view4;
	ViewProvider viewProvider;
	ViewControllerListener controllerListener;

	@Before
	public void setUp() {
		controller = new DefaultViewController();
		view = createMock(ControllableView.class);
		view2 = createMock(ControllableView.class);
		view3 = createMock(ControllableView.class);
		view4 = createMock(ControllableView.class);
		viewProvider = createMock(ViewProvider.class);
		controllerListener = createMock(ViewControllerListener.class);
	}
	
	@Test
	public void testInitialState() {
		assertNull(controller.getCurrentView());
		assertNull(controller.getFirstView());
		assertFalse(controller.isForwardNavigationPossible());
		assertTrue(controller.getTrail().isEmpty());
		assertNull(controller.getViewProvider());
		assertFalse(controller.goForward());
		assertFalse(controller.goBack());
		assertFalse(controller.goToFirstView());
	}
	
	@Test
	public void testSetViewProvider() {
		controller.setViewProvider(viewProvider);
		assertSame(viewProvider, controller.getViewProvider());
		controller.setViewProvider(null);
		assertNull(controller.getViewProvider());
	}
	
	@SuppressWarnings("serial")
	@Test
	public void testGoToViewSingleKeyValuePair() {
		final boolean called[] = new boolean[1];
		controller = new DefaultViewController() {
			/*
			 * As the method under test should redirect to this method, we just override it
			 * to make sure it receives the proper parameters.
			 */
			public boolean goToView(ControllableView view, java.util.Map<String,Object> userData) {
				assertSame(DefaultViewControllerTest.this.view, view);
				assertEquals("world", userData.get("hello"));
				called[0] = true;
				return true;
			};
		};
		
		assertTrue(controller.goToView(view, "hello", "world"));
		
		assertTrue(called[0]);
	}
	
	@SuppressWarnings("serial")
	@Test
	public void testGoToViewSingleKeyValuePair_NullKey() {
		final boolean called[] = new boolean[1];
		controller = new DefaultViewController() {
			/*
			 * As the method under test should redirect to this method, we just override it
			 * to make sure it receives the proper parameters.
			 */
			public boolean goToView(ControllableView view, java.util.Map<String,Object> userData) {
				assertSame(DefaultViewControllerTest.this.view, view);
				assertNull(userData);
				called[0] = true;
				return false; // Return false to check that the return value is also properly propagated
			};
		};
		
		assertFalse(controller.goToView(view, null, null));
		
		assertTrue(called[0]);
	}	
	
	@SuppressWarnings("serial")
	@Test
	public void testGoToViewNoUserData() {
		final boolean called[] = new boolean[1];
		controller = new DefaultViewController() {
			/*
			 * As the method under test should redirect to this method, we just override it
			 * to make sure it receives the proper parameters.
			 */
			public boolean goToView(ControllableView view, java.util.Map<String,Object> userData) {
				assertSame(DefaultViewControllerTest.this.view, view);
				assertNull(userData);
				called[0] = true;
				return true;
			};
		};
		
		assertTrue(controller.goToView(view));
		
		assertTrue(called[0]);
	}
	
	@SuppressWarnings("serial")
	@Test
	public void testGoToFirstView() {
		final boolean called[] = new boolean[1];
		controller = new DefaultViewController() {
			/*
			 * As the method under test should redirect to this method, we just override it
			 * to make sure it receives the proper parameters.
			 */
			public boolean goToView(ControllableView view, java.util.Map<String,Object> userData) {
				assertSame(DefaultViewControllerTest.this.view, view);
				assertNull(userData);
				called[0] = true;
				return true;
			};
		};
		
		controller.viewStack.push(view);
		controller.viewStack.push(view2);
		controller.viewStack.push(view3);
		controller.currentView = view3;
		controller.indexOfCurrentView = 2;
		
		assertTrue(controller.goToFirstView());
		
		assertTrue(called[0]);
	}	
	
	@SuppressWarnings("serial")
	@Test
	public void testGoToViewSingleKeyValuePair_ViewProvider_Success() {
		expect(viewProvider.getView("viewId")).andReturn(view);
		replay(viewProvider);
		
		final boolean called[] = new boolean[1];
		controller = new DefaultViewController() {
			/*
			 * As the method under test should redirect to this method, we just override it
			 * to make sure it receives the proper parameters.
			 */
			public boolean goToView(ControllableView view, java.util.Map<String,Object> userData) {
				assertSame(DefaultViewControllerTest.this.view, view);
				assertEquals("world", userData.get("hello"));
				called[0] = true;
				return true;
			};
		};
		controller.setViewProvider(viewProvider);
		
		assertTrue(controller.goToView("viewId", "hello", "world"));
		
		assertTrue(called[0]);
		
		verify(viewProvider);
	}
	
	@SuppressWarnings("serial")
	@Test
	public void testGoToViewNoUserData_ViewProvider_Success() {
		expect(viewProvider.getView("viewId")).andReturn(view);
		replay(viewProvider);

		final boolean called[] = new boolean[1];
		controller = new DefaultViewController() {
			/*
			 * As the method under test should redirect to this method, we just override it
			 * to make sure it receives the proper parameters.
			 */
			public boolean goToView(ControllableView view, java.util.Map<String,Object> userData) {
				assertSame(DefaultViewControllerTest.this.view, view);
				assertNull(userData);
				called[0] = true;
				return true;
			};
		};
		controller.setViewProvider(viewProvider);	
		
		assertTrue(controller.goToView("viewId"));
		
		assertTrue(called[0]);
		
		verify(viewProvider);
	}
	
	@SuppressWarnings("serial")
	@Test
	public void testGoToView_ViewProvider_Success() {
		final HashMap<String, Object> userData = new HashMap<String, Object>();
		userData.put("Hello", "World");

		expect(viewProvider.getView("viewId")).andReturn(view);
		replay(viewProvider);
		
		final boolean called[] = new boolean[1];
		controller = new DefaultViewController() {
			/*
			 * As the method under test should redirect to this method, we just override it
			 * to make sure it receives the proper parameters.
			 */
			public boolean goToView(ControllableView view, java.util.Map<String,Object> userData2) {
				assertSame(DefaultViewControllerTest.this.view, view);
				assertSame(userData, userData2);
				called[0] = true;
				return true;
			};
		};
		controller.setViewProvider(viewProvider);
		
		assertTrue(controller.goToView("viewId", userData));
		
		assertTrue(called[0]);
		
		verify(viewProvider);
	}
	
	@Test(expected = IllegalStateException.class)
	public void testGoToView_ViewProvider_NoProvider() {
		controller.goToView("viewId", null);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testGoToView_ViewProvider_IllegalViewId() {
		expect(viewProvider.getView("viewId")).andReturn(null);
		replay(viewProvider);
		controller.setViewProvider(viewProvider);
		controller.goToView("viewId", null);
		verify(viewProvider);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testGoToView_ViewProvider_NullViewId() {
		controller.goToView((String) null, null);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testGoToView_NullView() {
		controller.goToView((ControllableView) null, null);
	}
	
	@Test
	public void testGoToView_EmptyStack() {		
		HashMap<String, Object> userData = new HashMap<String, Object>();
		userData.put("Hello", "World");
		
		// Instruct mocks
		view.showView(controller, userData, null, Direction.FORWARD);
		replay(view);
		
		controllerListener.currentViewChanged(controller, null, view, Direction.FORWARD, true);
		replay(controllerListener);
		
		// Register listener
		controller.addListener(controllerListener);
		
		// Run test
		assertTrue(controller.goToView(view, userData));
		
		// Verify results
		assertSame(view, controller.getCurrentView());
		assertSame(view, controller.getFirstView());
		assertEquals(1, controller.getTrail().size());
		assertSame(view, controller.getTrail().get(0));
		assertFalse(controller.isForwardNavigationPossible());
		
		verify(view);
		verify(controllerListener);
	}
	
	@Test
	public void testGoToView_OneViewInStack() {
		HashMap<String, Object> userData = new HashMap<String, Object>();
		userData.put("Hello", "World");

		// Instruct mocks
		view.showView(controller, userData, null, Direction.FORWARD);
		replay(view);
		view2.showView(controller, userData, view, Direction.FORWARD);
		replay(view2);
		
		controllerListener.currentViewChanged(controller, null, view, Direction.FORWARD, true);
		controllerListener.currentViewChanged(controller, view, view2, Direction.FORWARD, true);
		replay(controllerListener);
		
		// Register listener
		controller.addListener(controllerListener);
		
		// Run test
		assertTrue(controller.goToView(view, userData));
		assertTrue(controller.goToView(view2, userData));
		
		// Verify results
		assertSame(view2, controller.getCurrentView());
		assertSame(view, controller.getFirstView());
		assertEquals(2, controller.getTrail().size());
		assertSame(view, controller.getTrail().get(0));
		assertSame(view2, controller.getTrail().get(1));
		assertFalse(controller.isForwardNavigationPossible());
		
		verify(view);
		verify(view2);
		verify(controllerListener);
	}
	
	@Test
	public void testGoToView_ThreeViewsInStack_Backwards_NoForwardNavigation() {
		HashMap<String, Object> userData = new HashMap<String, Object>();
		userData.put("Hello", "World");
		
		// Instruct mocks
		replay(view);
		
		view2.showView(controller, userData, view3, Direction.BACKWARD);
		replay(view2);

		expect(view3.hideView(controller, view2, Direction.BACKWARD)).andReturn(HideOperation.ALLOW_WITHOUT_FORWARD_NAVIGATION);
		replay(view3);
		
		controllerListener.currentViewChanged(controller, view3, view2, Direction.BACKWARD, true);
		replay(controllerListener);
		
		// Setup views
		controller.viewStack.push(view);
		controller.viewStack.push(view2);
		controller.viewStack.push(view3);
		controller.currentView = view3;
		controller.indexOfCurrentView = 2;
		
		// Register listener
		controller.addListener(controllerListener);
		
		// Run test
		assertTrue(controller.goToView(view2, userData));
		
		// Verify results
		assertSame(view2, controller.getCurrentView());
		assertSame(view, controller.getFirstView());
		assertEquals(2, controller.getTrail().size());
		assertSame(view, controller.getTrail().get(0));
		assertSame(view2, controller.getTrail().get(1));
		assertFalse(controller.isForwardNavigationPossible());

		verify(view);
		verify(view2);
		verify(view3);
		verify(controllerListener);
	}
	
	@Test
	public void testGoToView_ThreeViewsInStack_Backwards_WithForwardNavigation() {
		HashMap<String, Object> userData = new HashMap<String, Object>();
		userData.put("Hello", "World");

		// Instruct mocks
		replay(view);
		
		view2.showView(controller, userData, view3, Direction.BACKWARD);
		replay(view2);

		expect(view3.hideView(controller, view2, Direction.BACKWARD)).andReturn(HideOperation.ALLOW);
		replay(view3);
		
		controllerListener.currentViewChanged(controller, view3, view2, Direction.BACKWARD, false);
		replay(controllerListener);
		
		// Setup views
		controller.viewStack.push(view);
		controller.viewStack.push(view2);
		controller.viewStack.push(view3);
		controller.currentView = view3;
		controller.indexOfCurrentView = 2;
		
		// Register listener
		controller.addListener(controllerListener);
		
		// Run test
		assertTrue(controller.goToView(view2, userData));
		
		// Verify results
		assertSame(view2, controller.getCurrentView());
		assertSame(view, controller.getFirstView());
		assertEquals(3, controller.getTrail().size());
		assertSame(view, controller.getTrail().get(0));
		assertSame(view2, controller.getTrail().get(1));
		assertSame(view3, controller.getTrail().get(2));
		assertTrue(controller.isForwardNavigationPossible());

		verify(view);
		verify(view2);
		verify(view3);
		verify(controllerListener);
	}
	
	@Test
	public void testGoToView_ThreeViewsInStack_Backwards_CurrentViewPrevents() {
		HashMap<String, Object> userData = new HashMap<String, Object>();
		userData.put("Hello", "World");

		// Instruct mocks
		replay(view);
		
		replay(view2);

		expect(view3.hideView(controller, view2, Direction.BACKWARD)).andReturn(HideOperation.PREVENT);
		replay(view3);
		
		replay(controllerListener);
		
		// Setup views
		controller.viewStack.push(view);
		controller.viewStack.push(view2);
		controller.viewStack.push(view3);
		controller.currentView = view3;
		controller.indexOfCurrentView = 2;
		
		// Register listener
		controller.addListener(controllerListener);
		
		// Run test
		assertFalse(controller.goToView(view2, userData));
		
		// Verify results
		assertSame(view3, controller.getCurrentView());
		assertSame(view, controller.getFirstView());
		assertEquals(3, controller.getTrail().size());
		assertSame(view, controller.getTrail().get(0));
		assertSame(view2, controller.getTrail().get(1));
		assertSame(view3, controller.getTrail().get(2));
		assertFalse(controller.isForwardNavigationPossible());

		verify(view);
		verify(view2);
		verify(view3);
		verify(controllerListener);
	}
	
	@Test
	public void testGoToView_ThreeViewsInStack_Backwards_SecondViewPrevents() {
		HashMap<String, Object> userData = new HashMap<String, Object>();
		userData.put("Hello", "World");

		// Instruct mocks
		replay(view);
		
		expect(view2.hideView(controller, view, Direction.BACKWARD)).andReturn(HideOperation.PREVENT);
		view2.showView(controller, null, view3, Direction.BACKWARD);
		replay(view2);

		expect(view3.hideView(controller, view, Direction.BACKWARD)).andReturn(HideOperation.ALLOW_WITHOUT_FORWARD_NAVIGATION);
		replay(view3);
		
		controllerListener.currentViewChanged(controller, view3, view2, Direction.BACKWARD, true);
		replay(controllerListener);
		
		// Setup views
		controller.viewStack.push(view);
		controller.viewStack.push(view2);
		controller.viewStack.push(view3);
		controller.currentView = view3;
		controller.indexOfCurrentView = 2;
		
		// Register listener
		controller.addListener(controllerListener);
		
		// Run test
		assertTrue(controller.goToView(view, userData));
		
		// Verify results
		assertSame(view2, controller.getCurrentView());
		assertSame(view, controller.getFirstView());
		assertEquals(2, controller.getTrail().size());
		assertSame(view, controller.getTrail().get(0));
		assertSame(view2, controller.getTrail().get(1));
		assertFalse(controller.isForwardNavigationPossible());

		verify(view);
		verify(view2);
		verify(view3);
		verify(controllerListener);
	}
	
	@Test
	public void testGoToView_AlreadyCurrentView() {
		HashMap<String, Object> userData = new HashMap<String, Object>();
		userData.put("Hello", "World");

		// Instruct mocks
		replay(view);
		replay(controllerListener);
		
		// Setup views
		controller.viewStack.push(view);
		controller.currentView = view;
		controller.indexOfCurrentView = 0;
		
		// Register listener
		controller.addListener(controllerListener);
		
		// Run test
		assertFalse(controller.goToView(view, userData));
		
		// Verify results
		assertSame(view, controller.getCurrentView());
		assertSame(view, controller.getFirstView());
		assertEquals(1, controller.getTrail().size());
		assertSame(view, controller.getTrail().get(0));
		assertFalse(controller.isForwardNavigationPossible());

		verify(view);
		verify(controllerListener);		
	}
	
	@Test
	public void testGoBack_NoForwardNavigation() {
		// Instruct mocks
		replay(view);
		
		view2.showView(controller, null, view3, Direction.BACKWARD);
		replay(view2);

		expect(view3.hideView(controller, view2, Direction.BACKWARD)).andReturn(HideOperation.ALLOW_WITHOUT_FORWARD_NAVIGATION);
		replay(view3);
		
		controllerListener.currentViewChanged(controller, view3, view2, Direction.BACKWARD, true);
		replay(controllerListener);
		
		// Setup views
		controller.viewStack.push(view);
		controller.viewStack.push(view2);
		controller.viewStack.push(view3);
		controller.currentView = view3;
		controller.indexOfCurrentView = 2;
		
		// Register listener
		controller.addListener(controllerListener);
		
		// Run test
		assertTrue(controller.goBack());
		
		// Verify results
		assertSame(view2, controller.getCurrentView());
		assertSame(view, controller.getFirstView());
		assertEquals(2, controller.getTrail().size());
		assertSame(view, controller.getTrail().get(0));
		assertSame(view2, controller.getTrail().get(1));
		assertFalse(controller.isForwardNavigationPossible());

		verify(view);
		verify(view2);
		verify(view3);
		verify(controllerListener);		
	}
	
	@Test
	public void testGoBack_WithForwardNavigation() {
		// Instruct mocks
		replay(view);
		
		view2.showView(controller, null, view3, Direction.BACKWARD);
		replay(view2);

		expect(view3.hideView(controller, view2, Direction.BACKWARD)).andReturn(HideOperation.ALLOW);
		replay(view3);
		
		controllerListener.currentViewChanged(controller, view3, view2, Direction.BACKWARD, false);
		replay(controllerListener);
		
		// Setup views
		controller.viewStack.push(view);
		controller.viewStack.push(view2);
		controller.viewStack.push(view3);
		controller.currentView = view3;
		controller.indexOfCurrentView = 2;
		
		// Register listener
		controller.addListener(controllerListener);
		
		// Run test
		assertTrue(controller.goBack());
		
		// Verify results
		assertSame(view2, controller.getCurrentView());
		assertSame(view, controller.getFirstView());
		assertEquals(3, controller.getTrail().size());
		assertSame(view, controller.getTrail().get(0));
		assertSame(view2, controller.getTrail().get(1));
		assertSame(view3, controller.getTrail().get(2));
		assertTrue(controller.isForwardNavigationPossible());

		verify(view);
		verify(view2);
		verify(view3);
		verify(controllerListener);	
	}
	
	@Test
	public void testGoBack_CurrentViewPrevents() {
		// Instruct mocks
		replay(view);
		
		replay(view2);

		expect(view3.hideView(controller, view2, Direction.BACKWARD)).andReturn(HideOperation.PREVENT);
		replay(view3);
		
		replay(controllerListener);
		
		// Setup views
		controller.viewStack.push(view);
		controller.viewStack.push(view2);
		controller.viewStack.push(view3);
		controller.currentView = view3;
		controller.indexOfCurrentView = 2;
		
		// Register listener
		controller.addListener(controllerListener);
		
		// Run test
		assertFalse(controller.goBack());
		
		// Verify results
		assertSame(view3, controller.getCurrentView());
		assertSame(view, controller.getFirstView());
		assertEquals(3, controller.getTrail().size());
		assertSame(view, controller.getTrail().get(0));
		assertSame(view2, controller.getTrail().get(1));
		assertSame(view3, controller.getTrail().get(2));
		assertFalse(controller.isForwardNavigationPossible());

		verify(view);
		verify(view2);
		verify(view3);
		verify(controllerListener);		
	}
	
	@Test
	public void testGoBack_OneView() {
		// Instruct mocks
		replay(view);
		replay(controllerListener);
		
		// Setup views
		controller.viewStack.push(view);
		controller.currentView = view;
		controller.indexOfCurrentView = 0;
		
		// Register listener
		controller.addListener(controllerListener);
		
		// Run test
		assertFalse(controller.goBack());
		
		// Verify results
		assertSame(view, controller.getCurrentView());
		assertSame(view, controller.getFirstView());
		assertEquals(1, controller.getTrail().size());
		assertSame(view, controller.getTrail().get(0));
		assertFalse(controller.isForwardNavigationPossible());

		verify(view);
		verify(controllerListener);		
	}
	
	@Test
	public void testGoToNewViewsWithForwardNavigationPossible() {
		// This test was written to detect a bug that Marcus found.
		
		// Instruct mocks
		view.showView(controller, null, view2, Direction.BACKWARD);
		view.showView(controller, null, view3, Direction.BACKWARD);
		view.showView(controller, null, view4, Direction.BACKWARD);
		replay(view);
		
		view2.showView(controller, null, view, Direction.FORWARD);		
		expect(view2.hideView(controller, view, Direction.BACKWARD)).andReturn(HideOperation.ALLOW);		
		replay(view2);

		view3.showView(controller, null, view, Direction.FORWARD);		
		expect(view3.hideView(controller, view, Direction.BACKWARD)).andReturn(HideOperation.ALLOW);		
		replay(view3);
		
		view4.showView(controller, null, view, Direction.FORWARD);		
		expect(view4.hideView(controller, view, Direction.BACKWARD)).andReturn(HideOperation.ALLOW);		
		replay(view4);		
		
		// Setup views
		controller.viewStack.push(view);
		controller.currentView = view;
		controller.indexOfCurrentView = 0;

		// Run test
		controller.goToView(view2);
		controller.goBack();
		controller.goToView(view3);
		controller.goBack();
		controller.goToView(view4);
		controller.goBack();
		
		// Verify results
		assertSame(view, controller.getCurrentView());
		
		verify(view);
		verify(view2);
		verify(view3);
		verify(view4);
	}
}
