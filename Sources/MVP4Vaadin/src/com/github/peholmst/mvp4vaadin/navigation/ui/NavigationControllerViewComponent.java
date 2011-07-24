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
package com.github.peholmst.mvp4vaadin.navigation.ui;

import com.github.peholmst.mvp4vaadin.AbstractViewComponent;
import com.github.peholmst.mvp4vaadin.VaadinView;
import com.github.peholmst.mvp4vaadin.navigation.NavigationController;
import com.github.peholmst.mvp4vaadin.navigation.NavigationControllerEvent;
import com.github.peholmst.mvp4vaadin.navigation.NavigationControllerListener;
import com.github.peholmst.mvp4vaadin.navigation.events.CurrentNavigationControllerViewChangedEvent;
import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.VerticalLayout;

/**
 * This component displays the current view of a {@link NavigationController},
 * provided that the view implements the {@link VaadinView} interface (see
 * {@link AbstractViewComponent} for example). When the current view changes,
 * this component will update itself accordingly.
 * 
 * @see #setController(NavigationController)
 * 
 * @author Petter Holmström
 * @since 1.0
 */
public class NavigationControllerViewComponent extends VerticalLayout implements
		NavigationControllerListener {

	private static final long serialVersionUID = -8383927952103198829L;

	private NavigationController controller;

	private ComponentContainer viewComponent;

	/**
	 * Sets the navigation controller whose current view will be shown inside
	 * this component. This component will register itself as a listener with
	 * the controller, so if you no longer need it you should set the controller
	 * to <code>null</code>.
	 */
	public void setController(NavigationController controller) {
		if (this.controller != null) {
			this.controller.removeListener(this);
		}
		this.controller = controller;
		setCurrentViewComponent();
		if (this.controller != null) {
			this.controller.addListener(this);
		}
	}

	/**
	 * Returns the navigation controller whose current view will be shown inside
	 * this component. If no controller has been set, this method returns
	 * <code>null</code>.
	 */
	public NavigationController getController() {
		return controller;
	}

	@Override
	public void handleNavigationControllerEvent(NavigationControllerEvent event) {
		if (event.getSource() == this.controller
				&& event instanceof CurrentNavigationControllerViewChangedEvent) {
			setCurrentViewComponent();
		}
	}

	private void setCurrentViewComponent() {
		if (viewComponent != null) {
			removeComponent(viewComponent);
			viewComponent = null;
		}

		if (controller != null
				&& controller.getCurrentView() instanceof VaadinView) {
			viewComponent = ((VaadinView) controller.getCurrentView())
					.getViewComponent();
			addComponent(viewComponent);
		}
	}
}
