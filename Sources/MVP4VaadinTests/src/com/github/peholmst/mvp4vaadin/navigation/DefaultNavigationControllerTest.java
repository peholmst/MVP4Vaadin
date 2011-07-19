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

import static org.junit.Assert.*;

import java.util.HashMap;

import org.junit.Before;
import org.junit.Test;

import com.github.peholmst.mvp4vaadin.View;
import com.github.peholmst.mvp4vaadin.navigation.NavigationController.NavigationResult;
import com.github.peholmst.mvp4vaadin.testdata.MyTestViewImpl;
import static org.easymock.EasyMock.*;
/**
 * Test case for {@link DefaultNavigationController}.
 * 
 * @author Petter Holmström
 * @since 1.0
 */
public class DefaultNavigationControllerTest {
	// TODO Implement test!

	DefaultNavigationController controller;

	@Before
	public void setUp() {
		controller = new DefaultNavigationController();
	}

	@Test
	public void attachSingleViewToEmptyController_WithoutCallback() {
		final View singleView = new MyTestViewImpl();
		final NavigationRequest request = NavigationRequestBuilder
				.newInstance().startWithEmptyPath().addViewToPath(singleView)
				.buildRequest();
		final NavigationResult result = controller.navigate(request);

		assertEquals(NavigationResult.SUCCEEDED, result);
		assertSame(singleView, controller.getCurrentView());
		assertSame(singleView, controller.getFirstView());
		assertEquals(1, controller.getViewStack().size());
	}

	@Test
	public void attachSingleViewToEmptyController_WithCallback() {		
		final MyTestViewImpl singleView = new MyTestViewImpl();
		final NavigationRequest request = NavigationRequestBuilder
				.newInstance().startWithEmptyPath().addViewToPath(singleView)
				.buildRequest();
		
		final NavigationControllerCallback callback = createMock(NavigationControllerCallback.class);
		callback.attachedToController(controller);
		callback.navigatedToView(request.getParams(), null);
		replay(callback);
		singleView.getAdaptableSupport().registerAdapter(NavigationControllerCallback.class, callback);
		
		final NavigationResult result = controller.navigate(request);

		assertEquals(NavigationResult.SUCCEEDED, result);
		assertSame(singleView, controller.getCurrentView());
		assertSame(singleView, controller.getFirstView());
		assertEquals(1, controller.getViewStack().size());
		verify(callback);
	}	
	
	@Test
	public void attachMultipleViewsToEmptyController_WithoutCallback() {
		final View firstView = new MyTestViewImpl();
		final View secondView = new MyTestViewImpl();
		final NavigationRequest request = NavigationRequestBuilder
				.newInstance().startWithEmptyPath()
				.addViewsToPath(firstView, secondView).buildRequest();
		final NavigationResult result = controller.navigate(request);

		assertEquals(NavigationResult.SUCCEEDED, result);
		assertSame(secondView, controller.getCurrentView());
		assertSame(firstView, controller.getFirstView());
		assertEquals(2, controller.getViewStack().size());
	}

	@Test
	public void attachMultipleViewsToEmptyController_WithCallback() {
		final MyTestViewImpl firstView = new MyTestViewImpl();
		final MyTestViewImpl secondView = new MyTestViewImpl();
		final NavigationRequest request = NavigationRequestBuilder
				.newInstance().startWithEmptyPath()
				.addViewsToPath(firstView, secondView).buildRequest();
		
		final NavigationControllerCallback firstCallback = createMock(NavigationControllerCallback.class);
		firstCallback.attachedToController(controller);
		replay(firstCallback);
		firstView.getAdaptableSupport().registerAdapter(NavigationControllerCallback.class, firstCallback);
		
		final NavigationControllerCallback secondCallback = createMock(NavigationControllerCallback.class);
		secondCallback.attachedToController(controller);
		secondCallback.navigatedToView(request.getParams(), null);
		replay(secondCallback);
		secondView.getAdaptableSupport().registerAdapter(NavigationControllerCallback.class, secondCallback);
		
		final NavigationResult result = controller.navigate(request);

		assertEquals(NavigationResult.SUCCEEDED, result);
		assertSame(secondView, controller.getCurrentView());
		assertSame(firstView, controller.getFirstView());
		assertEquals(2, controller.getViewStack().size());
		verify(firstCallback);
		verify(secondCallback);
	}	
	
	@Test
	public void attachSingleViewToNonEmptyController_WithoutCallback() {
		final View firstView = new MyTestViewImpl();
		controller.getModifiableViewStack().add(firstView);

		final View singleView = new MyTestViewImpl();
		final NavigationRequest request = NavigationRequestBuilder
				.newInstance().startWithPathToCurrentView(controller)
				.addViewToPath(singleView).buildRequest();
		final NavigationResult result = controller.navigate(request);

		assertEquals(NavigationResult.SUCCEEDED, result);
		assertSame(singleView, controller.getCurrentView());
		assertSame(firstView, controller.getFirstView());
		assertEquals(2, controller.getViewStack().size());
	}
	
	@Test
	public void attachSingleViewToNonEmptyController_WithCallback() {
		final MyTestViewImpl firstView = new MyTestViewImpl();
		controller.getModifiableViewStack().add(firstView);

		final MyTestViewImpl singleView = new MyTestViewImpl();
		final NavigationRequest request = NavigationRequestBuilder
				.newInstance().startWithPathToCurrentView(controller)
				.addViewToPath(singleView).buildRequest();
		
		final NavigationControllerCallback firstCallback = createMock(NavigationControllerCallback.class);
		firstCallback.navigatedFromView(singleView);
		replay(firstCallback);
		firstView.getAdaptableSupport().registerAdapter(NavigationControllerCallback.class, firstCallback);
		
		final NavigationControllerCallback singleCallback = createMock(NavigationControllerCallback.class);
		singleCallback.attachedToController(controller);
		singleCallback.navigatedToView(request.getParams(), firstView);
		replay(singleCallback);
		singleView.getAdaptableSupport().registerAdapter(NavigationControllerCallback.class, singleCallback);		
		
		final NavigationResult result = controller.navigate(request);

		assertEquals(NavigationResult.SUCCEEDED, result);
		assertSame(singleView, controller.getCurrentView());
		assertSame(firstView, controller.getFirstView());
		assertEquals(2, controller.getViewStack().size());
		verify(firstCallback);
		verify(singleCallback);
	}	

	@Test
	public void attachMultipleViewsToNonEmptyController_WithoutCallback() {
		final View firstView = new MyTestViewImpl();
		controller.getModifiableViewStack().add(firstView);

		final View secondView = new MyTestViewImpl();
		final View thirdView = new MyTestViewImpl();
		final NavigationRequest request = NavigationRequestBuilder
				.newInstance().startWithPathToCurrentView(controller)
				.addViewsToPath(secondView, thirdView).buildRequest();
		final NavigationResult result = controller.navigate(request);

		assertEquals(NavigationResult.SUCCEEDED, result);
		assertSame(thirdView, controller.getCurrentView());
		assertSame(firstView, controller.getFirstView());
		assertEquals(3, controller.getViewStack().size());
	}
	
	@Test
	public void attachMultipleViewsToNonEmptyController_WithCallback() {
		final MyTestViewImpl firstView = new MyTestViewImpl();
		controller.getModifiableViewStack().add(firstView);

		final MyTestViewImpl secondView = new MyTestViewImpl();
		final MyTestViewImpl thirdView = new MyTestViewImpl();
		final NavigationRequest request = NavigationRequestBuilder
				.newInstance().startWithPathToCurrentView(controller)
				.addViewsToPath(secondView, thirdView).buildRequest();
		
		final NavigationControllerCallback firstCallback = createMock(NavigationControllerCallback.class);
		firstCallback.navigatedFromView(thirdView);
		replay(firstCallback);
		firstView.getAdaptableSupport().registerAdapter(NavigationControllerCallback.class, firstCallback);
		
		final NavigationControllerCallback secondCallback = createMock(NavigationControllerCallback.class);
		secondCallback.attachedToController(controller);
		replay(secondCallback);
		secondView.getAdaptableSupport().registerAdapter(NavigationControllerCallback.class, secondCallback);		
		
		final NavigationControllerCallback thirdCallback = createMock(NavigationControllerCallback.class);
		thirdCallback.attachedToController(controller);
		thirdCallback.navigatedToView(request.getParams(), firstView);
		replay(thirdCallback);
		thirdView.getAdaptableSupport().registerAdapter(NavigationControllerCallback.class, thirdCallback);		
		
		final NavigationResult result = controller.navigate(request);

		assertEquals(NavigationResult.SUCCEEDED, result);
		assertSame(thirdView, controller.getCurrentView());
		assertSame(firstView, controller.getFirstView());
		assertEquals(3, controller.getViewStack().size());
		verify(firstCallback);
		verify(secondCallback);
		verify(thirdCallback);
	}	

	@Test
	public void navigateBackToPreviousView_WithoutCallback() {
		final View firstView = new MyTestViewImpl();
		final View secondView = new MyTestViewImpl();
		controller.getModifiableViewStack().add(firstView);
		controller.getModifiableViewStack().add(secondView);

		final boolean result = controller.navigateBack();

		assertTrue(result);
		assertSame(firstView, controller.getCurrentView());
		assertSame(firstView, controller.getFirstView());
		assertEquals(1, controller.getViewStack().size());
	}

	@Test
	public void navigateBackToPreviousView_WithCallback() {
		final MyTestViewImpl firstView = new MyTestViewImpl();
		final MyTestViewImpl secondView = new MyTestViewImpl();
		controller.getModifiableViewStack().add(firstView);
		controller.getModifiableViewStack().add(secondView);

		final NavigationControllerCallback firstCallback = createMock(NavigationControllerCallback.class);
		firstCallback.navigatedToView(new HashMap<String, Object>(), secondView);
		replay(firstCallback);
		firstView.getAdaptableSupport().registerAdapter(NavigationControllerCallback.class, firstCallback);
		
		final NavigationControllerCallback secondCallback = createMock(NavigationControllerCallback.class);
		expect(secondCallback.detachingFromController(controller)).andReturn(true);
		secondCallback.detachedFromController(controller);
		replay(secondCallback);
		secondView.getAdaptableSupport().registerAdapter(NavigationControllerCallback.class, secondCallback);		
		
		final boolean result = controller.navigateBack();

		assertTrue(result);
		assertSame(firstView, controller.getCurrentView());
		assertSame(firstView, controller.getFirstView());
		assertEquals(1, controller.getViewStack().size());
		verify(firstCallback);
		verify(secondCallback);
	}
	
	@Test
	public void navigateBackWithEmptyStack() {
		final boolean result = controller.navigateBack();

		assertFalse(result);
		assertTrue(controller.isEmpty());
	}

	@Test
	public void navigateBackFromFirstView_WithoutCallback() {
		final View firstView = new MyTestViewImpl();
		controller.getModifiableViewStack().add(firstView);

		final boolean result = controller.navigateBack();

		assertTrue(result);
		assertNull(controller.getCurrentView());
		assertNull(controller.getFirstView());
		assertTrue(controller.isEmpty());
	}
	
	@Test
	public void navigateBackFromFirstView_WithCallback() {
		final MyTestViewImpl firstView = new MyTestViewImpl();
		controller.getModifiableViewStack().add(firstView);

		final NavigationControllerCallback firstCallback = createMock(NavigationControllerCallback.class);
		expect(firstCallback.detachingFromController(controller)).andReturn(true);
		firstCallback.detachedFromController(controller);
		replay(firstCallback);
		firstView.getAdaptableSupport().registerAdapter(NavigationControllerCallback.class, firstCallback);		
		
		final boolean result = controller.navigateBack();

		assertTrue(result);
		assertNull(controller.getCurrentView());
		assertNull(controller.getFirstView());
		assertTrue(controller.isEmpty());
		verify(firstCallback);
	}	

	@Test
	public void navigateBackInExistingPath_WithoutCallback() {
		final View firstView = new MyTestViewImpl();
		final View secondView = new MyTestViewImpl();
		final View thirdView = new MyTestViewImpl();
		controller.getModifiableViewStack().add(firstView);
		controller.getModifiableViewStack().add(secondView);
		controller.getModifiableViewStack().add(thirdView);

		final NavigationRequest request = NavigationRequestBuilder
				.newInstance().startWithPathToFirstView(controller)
				.buildRequest();
		final NavigationResult result = controller.navigate(request);

		assertEquals(NavigationResult.SUCCEEDED, result);
		assertSame(firstView, controller.getCurrentView());
		assertSame(firstView, controller.getFirstView());
		assertEquals(1, controller.getViewStack().size());
	}

	@Test
	public void navigateBackToNewPath_WithoutCallback() {
		final View firstView = new MyTestViewImpl();
		final View secondView = new MyTestViewImpl();
		final View thirdView = new MyTestViewImpl();
		controller.getModifiableViewStack().add(firstView);
		controller.getModifiableViewStack().add(secondView);
		controller.getModifiableViewStack().add(thirdView);

		final View fourthView = new MyTestViewImpl();
		final NavigationRequest request = NavigationRequestBuilder
				.newInstance().startWithPathToFirstView(controller)
				.addViewsToPath(fourthView).buildRequest();
		final NavigationResult result = controller.navigate(request);

		assertEquals(NavigationResult.SUCCEEDED, result);
		assertSame(fourthView, controller.getCurrentView());
		assertSame(firstView, controller.getFirstView());
		assertEquals(2, controller.getViewStack().size());
	}

	@Test
	public void clearStack_WithoutCallback() {
		final View firstView = new MyTestViewImpl();
		final View secondView = new MyTestViewImpl();
		final View thirdView = new MyTestViewImpl();
		controller.getModifiableViewStack().add(firstView);
		controller.getModifiableViewStack().add(secondView);
		controller.getModifiableViewStack().add(thirdView);

		final NavigationResult result = controller.clear();
		assertEquals(NavigationResult.SUCCEEDED, result);
		assertNull(controller.getCurrentView());
		assertNull(controller.getFirstView());
		assertTrue(controller.isEmpty());
	}
	
	public void clearEmptyStack() {
		
	}

	// TODO Add tests for listeners and callback interfaces
}
