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

import java.util.Iterator;

import com.github.peholmst.mvp4vaadin.navigation.ControllableView;
import com.github.peholmst.mvp4vaadin.navigation.Direction;
import com.github.peholmst.mvp4vaadin.navigation.ViewController;
import com.github.peholmst.mvp4vaadin.navigation.ViewControllerListener;
import com.vaadin.ui.AbstractComponent;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Component;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.themes.BaseTheme;

/**
 * This component is a breadcrumb navigation bar that shows all the views
 * currently in a {@link ViewController} as links in a row, where the first view
 * corresponds to the first link, etc:
 * 
 * <pre>
 * First view >> Second view >> Third view >> ...
 * </pre>
 * 
 * As the views change in the controller, the navigation bar will update itself.
 * A click on any of the navigation links will request the view controller to
 * navigate to that particular view.
 * <p>
 * The navigation bar extends {@link CssLayout}, which means that you are going to need
 * to use some CSS to get it to look right.
 * 
 * @author Petter Holmström
 * @since 1.0
 * @deprecated Will be removed in the 1.0 release, use {@link Breadcrumbs} instead.
 */
@Deprecated
public class NavigationBar extends CssLayout implements ViewControllerListener {

	// TODO Even if forward navigation is possible, the current view should
	// ALWAYS be the last breadcrumb!

	private static final String BREADCRUMB_ELEMENT = "breadcrumb-element";

	private static final long serialVersionUID = -6803449379545049738L;

	private ViewController viewController;

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
		if (this.viewController != null) {
			this.viewController.addListener(this);
		}
		addBreadcrumbsForController();
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

	private void addBreadcrumbsForController() {
		removeAllComponents();
		if (getViewController() != null) {
			for (ControllableView v : getViewController().getTrail()) {
				addBreadcrumbForView(v);
			}
		}
	}

	@Override
	public void currentViewChanged(ViewController source,
			ControllableView oldView, ControllableView newView,
			Direction direction, boolean newViewIsTopMost) {
		if (source != viewController) {
			return;
		}
		if (!newViewIsTopMost) {
			/*
			 * The view is already in the bread crumbs, so we just have to check
			 * if there are any views at the end that need to be removed
			 */
			ControllableView lastView = source.getTrail().get(
					source.getTrail().size() - 1);
			Iterator<Component> it = components.descendingIterator();
			while (it.hasNext()) {
				AbstractComponent c = (AbstractComponent) it.next();
				if (c.getData() == lastView) {
					break;
				} else {
					it.remove();
				}
			}
			requestRepaint();
		} else {
			if (direction.equals(Direction.FORWARD)) {
				// Add another button to the bread crumbs
				addBreadcrumbForView(newView);
			} else if (direction.equals(Direction.BACKWARD)) {
				// Remove buttons from the end of the bread crumbs until we
				// reach the new view
				Iterator<Component> it = components.descendingIterator();
				while (it.hasNext()) {
					AbstractComponent c = (AbstractComponent) it.next();
					if (c.getData() == newView) {
						break;
					} else {
						it.remove();
					}
				}
				requestRepaint();
			}
		}
	}

	/**
	 * Adds a breadcrumb to the navigation bar for the specified view. If there
	 * already is one or more breadcrumbs in the bar, a separator will be added
	 * before the actual navigation button:
	 * 
	 * <pre>
	 * First view >> Second view >> Third view
	 * </pre>
	 * 
	 * A click on the navigation button will result in a request to the view
	 * controller to navigate to that particular view.
	 * 
	 * @see #addViewButton(String)
	 * @see #addViewSeparator()
	 * 
	 * @param view
	 *            the view to add (must not be <code>null</code>).
	 */
	protected void addBreadcrumbForView(final ControllableView view) {
		if (getViewController().getTrail().size() > 1) {
			Label separator = addViewSeparator();
			separator.setData(view);
		}

		final Button btn = addViewButton(view.getDisplayName());
		btn.setDescription(view.getViewDescription());
		btn.addListener(new Button.ClickListener() {

			private static final long serialVersionUID = -2163467482631873920L;

			@Override
			public void buttonClick(ClickEvent event) {
				getViewController().goToView(view);
			}
		});
		btn.setData(view);
	}

	/**
	 * Creates and adds a new button to the navigation bar. Listeners etc. will
	 * be added by the {@link #addBreadcrumbForView(ControllableView)} method.
	 * 
	 * @param viewName
	 *            the name of the view to show on the button.
	 * @return the added button.
	 */
	protected Button addViewButton(String viewName) {
		final Button btn = new Button(viewName);
		btn.setStyleName(BaseTheme.BUTTON_LINK);
		btn.setSizeUndefined();
		btn.addStyleName(BREADCRUMB_ELEMENT);
		addComponent(btn);
		// setComponentAlignment(btn, Alignment.MIDDLE_LEFT);
		return btn;
	}

	/**
	 * Creates and adds a new separator label to the navigation bar.
	 * 
	 * @return the added separator label.
	 */
	protected Label addViewSeparator() {
		final Label lbl = new Label("»");
		lbl.setSizeUndefined();
		lbl.addStyleName(BREADCRUMB_ELEMENT);
		addComponent(lbl);
		// setComponentAlignment(lbl, Alignment.MIDDLE_LEFT);
		return lbl;
	}
}
