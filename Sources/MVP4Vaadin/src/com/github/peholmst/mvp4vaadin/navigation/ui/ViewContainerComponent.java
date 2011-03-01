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
 * @param V
 *            the super interface of the views. In most cases this is
 *            <code>ControllableView</code>, but you are free to use your own
 *            interface.
 */
public class ViewContainerComponent<V extends ControllableView> extends
		VerticalLayout implements ViewControllerListener<V> {

	private static final long serialVersionUID = 5199669312515753609L;

	private ViewController<V> viewController;

	private ComponentContainer currentViewComponent;

	/**
	 * Sets the view controller whose current view will be shown in this view
	 * container.
	 * 
	 * @param viewController
	 *            the view controller to set.
	 */
	public void setViewController(ViewController<V> viewController) {
		if (this.viewController != null) {
			this.viewController.removeListener(this);
		}
		this.viewController = viewController;
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
	public ViewController<V> getViewController() {
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
	public void currentViewChanged(ViewController<V> source, V oldView,
			V newView, Direction direction, boolean newViewIsTopMost) {
		if (source == this.viewController) {
			if (newView instanceof VaadinView) {
				ComponentContainer newViewComponent = ((VaadinView) newView)
						.getViewComponent();
				if (currentViewComponent != newViewComponent) {
					if (currentViewComponent == null) {
						addComponent(newViewComponent);
					} else {
						replaceComponent(currentViewComponent, newViewComponent);
					}
					currentViewComponent = newViewComponent;
				}
			}
		}
	}

}
