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

import org.junit.Before;
import org.junit.Test;

import com.github.peholmst.mvp4vaadin.View;
import com.github.peholmst.mvp4vaadin.navigation.NavigationController.NavigationResult;
import com.github.peholmst.mvp4vaadin.testdata.MyTestViewImpl;

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

	public void navigateBackToPreviousView_WithoutCallback() {
		// TODO Implement this test!
	}

	public void navigateBackInExistingPath_WithoutCallback() {
		// TODO Implement this test!
	}

	public void navigateBackToNewPath_WithoutCallback() {
		// TODO Implement this test
	}

	public void clearStack_WithoutCallback() {
		// TODO Implement this test
	}

	// TODO Add tests for listeners and callback interfaces
}
