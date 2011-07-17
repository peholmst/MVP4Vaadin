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
package com.github.peholmst.mvp4vaadin.navigation.incubation;

import static org.junit.Assert.*;

import static org.easymock.EasyMock.*;

import org.easymock.Capture;
import org.junit.Before;
import org.junit.Test;

import com.github.peholmst.mvp4vaadin.navigation.ControllableView;
import com.github.peholmst.mvp4vaadin.navigation.Direction;
import com.github.peholmst.mvp4vaadin.navigation.ViewController;
import com.github.peholmst.mvp4vaadin.navigation.ViewControllerListener;
import com.github.peholmst.mvp4vaadin.navigation.incubation.ControllableViewWithEmbeddedController;
import com.github.peholmst.mvp4vaadin.navigation.incubation.NestedControllersMaster;

/**
 * Test case for {@link NestedControllersMaster}.
 * 
 * @author Petter Holmström
 * @since 1.0
 * @deprecated Will be deleted in the 1.0 release
 */
@Deprecated
public class NestedControllersMasterTest {

	NestedControllersMaster master;
	ViewController controller;
	ViewController controller2;
	ViewController controller3;
	Capture<ViewControllerListener> listener;
	ControllableView view;
	ControllableViewWithEmbeddedController viewWithController;
	ControllableViewWithEmbeddedController viewWithController2;
	
	@Before
	public void setUp() {
		master = new NestedControllersMaster();
		controller = createMock(ViewController.class);
		controller2 = createMock(ViewController.class);
		controller3 = createMock(ViewController.class);
		listener = new Capture<ViewControllerListener>();
		view = createMock(ControllableView.class);
		viewWithController = createMock(ControllableViewWithEmbeddedController.class);
		viewWithController2 = createMock(ControllableViewWithEmbeddedController.class);
	}
	
	@Test
	public void activeControllerWithEmptyStack() {
		assertNull(master.getActiveViewController());
	}
	
	@Test
	public void setToplevelControllerWithEmptyStack() {
		controller.addListener(capture(listener));
		replay(controller);
		
		master.setToplevelController(controller);
		
		assertSame(controller, master.getActiveViewController());
		verify(controller);
		assertNotNull(listener.getValue());
	}
	
	@Test
	public void currentViewOfTopLevelChanges_NoEmbeddedController() {
		master.controllerStack.add(controller);
		replay(view);
		
		master.listener.currentViewChanged(controller, null, view, Direction.FORWARD, true);
		
		assertSame(controller, master.getActiveViewController());
		verify(view);
	}
	
	@Test
	public void currentViewOfTopLevelChanges_EmbeddedController() {
		master.controllerStack.add(controller);

		expect(viewWithController.getEmbeddedController()).andStubReturn(controller2);
		replay(viewWithController);
		controller2.addListener(capture(listener));
		expect(controller2.getCurrentView()).andStubReturn(null);
		replay(controller2);
		
		master.listener.currentViewChanged(controller, null, viewWithController, Direction.FORWARD, true);
		
		assertSame(controller2, master.getActiveViewController());
		verify(controller2);
		verify(viewWithController);
	}
	
	@Test
	public void currentViewOfTopLevelChanges_EmbeddedControllerWithEmbeddedController() {
		master.controllerStack.add(controller);
		
		expect(viewWithController.getEmbeddedController()).andStubReturn(controller2);
		replay(viewWithController);
		
		controller2.addListener(capture(listener));
		expect(controller2.getCurrentView()).andStubReturn(viewWithController2);
		replay(controller2);
		
		expect(viewWithController2.getEmbeddedController()).andStubReturn(null);
		replay(viewWithController2);
		
		master.listener.currentViewChanged(controller, null, viewWithController, Direction.FORWARD, true);

		assertSame(controller2, master.getActiveViewController());
		verify(controller2);
		verify(viewWithController);
		verify(viewWithController2);
	}
	
	@Test
	public void currentViewOfEmbeddedChanges() {
		// Setup the stack
		master.controllerStack.add(controller);
		master.controllerStack.add(controller2);
		master.controllerStack.add(controller3);
				
		// Run test
		master.listener.currentViewChanged(controller2, null, view, Direction.FORWARD, true);
		
		// Verify results
		assertSame(controller2, master.getActiveViewController());
	}
	
	@Test
	public void changeToplevelController() {
		// Setup the stack
		master.controllerStack.add(controller);
	
		// Configure mocks
		controller2.addListener(capture(listener));
		replay(controller2);
		
		// Run test
		master.setToplevelController(controller2);
		
		// Verify results
		assertSame(controller2, master.getActiveViewController());
		assertEquals(1, master.controllerStack.size());
		verify(controller2);
	}
}
