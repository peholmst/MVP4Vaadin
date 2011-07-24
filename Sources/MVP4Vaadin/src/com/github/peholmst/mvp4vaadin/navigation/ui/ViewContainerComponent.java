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

import com.github.peholmst.mvp4vaadin.VaadinView;
import com.github.peholmst.mvp4vaadin.navigation.ControllableView;
import com.github.peholmst.mvp4vaadin.navigation.Direction;
import com.github.peholmst.mvp4vaadin.navigation.ViewController;
import com.github.peholmst.mvp4vaadin.navigation.ViewControllerListener;
import com.vaadin.ui.Component;
import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.VerticalLayout;

/**
 * This component displays the current view of a {@link ViewController},
 * provided that the view implements the {@link VaadinView} interface. When the
 * current view changes, this component will update itself accordingly.
 * 
 * @author Petter Holmström
 * @since 1.0
 * @deprecated Will be removed in the 1.0 release, use
 *             {@link NavigationControllerViewComponent} instead.
 */
@Deprecated
public class ViewContainerComponent extends VerticalLayout implements
		ViewControllerListener {

	private static final long serialVersionUID = 5199669312515753609L;

	private ViewController viewController;

	private ComponentContainer currentViewComponent;

	/**
	 * Sets the view controller whose current view will be shown in this view
	 * container.
	 * 
	 * @param viewController
	 *            the view controller to set.
	 */
	public void setViewController(ViewController viewController) {
		if (this.viewController != null) {
			this.viewController.removeListener(this);
		}
		this.viewController = viewController;
		setCurrentViewComponent();
		if (this.viewController != null) {
			this.viewController.addListener(this);
		}
	}

	/**
	 * Gets the view controller whose current view will be shown in this view
	 * container.
	 * 
	 * @return the view controller, or <code>null</code> if none has been set.
	 */
	public ViewController getViewController() {
		return viewController;
	}

	/**
	 * Gets the view component that is currently displayed in this view
	 * container.
	 * 
	 * @return the currentViewComponent the view component, or <code>null</code>
	 *         if no view is currently visible.
	 */
	public Component getCurrentViewComponent() {
		return currentViewComponent;
	}

	@Override
	public void currentViewChanged(ViewController source,
			ControllableView oldView, ControllableView newView,
			Direction direction, boolean newViewIsTopMost) {
		if (source == this.viewController) {
			setCurrentViewComponent();
		}
	}

	private void setCurrentViewComponent() {
		if (currentViewComponent != null) {
			removeComponent(currentViewComponent);
			currentViewComponent = null;
		}

		if (viewController != null
				&& viewController.getCurrentView() instanceof VaadinView) {
			currentViewComponent = ((VaadinView) viewController
					.getCurrentView()).getViewComponent();
			addComponent(currentViewComponent);
		}
	}

}
