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

import java.util.HashMap;
import java.util.Map;

import com.github.peholmst.mvp4vaadin.View;
import com.github.peholmst.mvp4vaadin.ViewEvent;
import com.github.peholmst.mvp4vaadin.ViewListener;
import com.github.peholmst.mvp4vaadin.events.DescriptionChangedViewEvent;
import com.github.peholmst.mvp4vaadin.events.DisplayNameChangedViewEvent;
import com.github.peholmst.mvp4vaadin.navigation.NavigationController;
import com.github.peholmst.mvp4vaadin.navigation.NavigationControllerEvent;
import com.github.peholmst.mvp4vaadin.navigation.NavigationControllerListener;
import com.github.peholmst.mvp4vaadin.navigation.NavigationRequest;
import com.github.peholmst.mvp4vaadin.navigation.NavigationRequestBuilder;
import com.github.peholmst.mvp4vaadin.navigation.events.CurrentNavigationControllerViewChangedEvent;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.themes.BaseTheme;

/**
 * This class implements a breadcrumb navigation bar that shows all the views
 * currently in a {@link NavigationController} as links in a row, where the
 * first view corresponds to the first link, etc:
 * 
 * <pre>
 * First view >> Second view >> Third view >> ...
 * </pre>
 * <p>
 * As the views change in the controller, the navigation bar will update itself.
 * A click on any of the navigation links will request the view controller to
 * navigate to that particular view.
 * <p>
 * Both the links and the separators can be customized by implementing the
 * {@link ButtonFactory} and {@link SeparatorFactory} interfaces, respectively.
 * 
 * @see #setController(NavigationController)
 * 
 * @author Petter Holmström
 * @since 1.0
 */
public class Breadcrumbs extends HorizontalLayout implements
		NavigationControllerListener, ViewListener {

	private static final long serialVersionUID = 4513495936876605206L;

	public static final String BREADCRUMB_ELEMENT = "breadcrumb-element";

	/**
	 * Factory interface for creating breadcrumb separators.
	 * 
	 * @see Breadcrumbs#setSeparatorFactory(SeparatorFactory)
	 * @author Petter Holmström
	 * @since 1.0
	 */
	public static interface SeparatorFactory extends java.io.Serializable {
		/**
		 * Creates and returns a component to be used as a separator between
		 * breadcrumbs.
		 */
		Component createSeparator();
	}

	/**
	 * Default implementation of {@link SeparatorFactory}. The separators are
	 * labels containing the "»" character and having the
	 * {@link Breadcrumbs#BREADCRUMB_ELEMENT} style.
	 * 
	 * @author Petter Holmström
	 * @since 1.0
	 */
	public static class DefaultSeparatorFactory implements SeparatorFactory {

		private static final long serialVersionUID = 7957216244739746986L;

		@Override
		public Component createSeparator() {
			final Label separator = new Label("»");
			separator.setSizeUndefined();
			separator.addStyleName(BREADCRUMB_ELEMENT);
			return separator;
		}
	}

	/**
	 * Factory interface for creating breadcrumb buttons.
	 * 
	 * @see Breadcrumbs#setButtonFactory(ButtonFactory)
	 * @author Petter Holmström
	 * @since 1.0
	 */
	public static interface ButtonFactory extends java.io.Serializable {

		/**
		 * Creates and returns a button for the specified view. The click
		 * listener will be registered by the breadcrumbs component.
		 */
		Button createButton(View view);

		/**
		 * Updates the button texts. This method is called when the display name
		 * and/or the description of the specified view are changed.
		 */
		void updateButtonTexts(Button button, View view);
	}

	/**
	 * Default implementation of {@link ButtonFactory}. The created buttons have
	 * the {@link BaseTheme#BUTTON_LINK} and
	 * {@link Breadcrumbs#BREADCRUMB_ELEMENT} styles.
	 * 
	 * @author Petter Holmström
	 * @since 1.0
	 */
	public static class DefaultButtonFactory implements ButtonFactory {

		private static final long serialVersionUID = 8031407455065485896L;

		@Override
		public Button createButton(View view) {
			final Button btn = new Button();
			btn.setStyleName(BaseTheme.BUTTON_LINK);
			btn.setSizeUndefined();
			btn.addStyleName(BREADCRUMB_ELEMENT);
			updateButtonTexts(btn, view);
			return btn;
		}

		@Override
		public void updateButtonTexts(Button button, View view) {
			button.setCaption(view.getDisplayName());
			button.setDescription(view.getViewDescription());
		}
	}

	private SeparatorFactory separatorFactory = new DefaultSeparatorFactory();

	private ButtonFactory buttonFactory = new DefaultButtonFactory();

	private NavigationController controller;

	private Map<View, Button> viewButtonMap = new HashMap<View, Button>();

	/**
	 * Returns the navigation controller whose view stack will be displayed as
	 * breadcrumbs. If no controller has been set, <code>null</code> is
	 * returned.
	 */
	public NavigationController getController() {
		return controller;
	}

	/**
	 * Sets the navigation controller to use. This component will register
	 * itself as a listener of the controller. Setting the controller to
	 * <code>null</code> will unregister the listener.
	 */
	public void setController(NavigationController controller) {
		if (this.controller != null) {
			this.controller.removeListener(this);
		}
		this.controller = controller;
		addBreadcrumbsForControllerRemovingAnyExistingOnes();
		if (this.controller != null) {
			this.controller.addListener(this);
		}
	}

	/**
	 * Returns the separator factory to use for creating separators between
	 * breadcrumb buttons.
	 */
	public SeparatorFactory getSeparatorFactory() {
		return separatorFactory;
	}

	/**
	 * Sets the separator factory to use for creating separators between
	 * breadcrumb buttons. Set this value to <code>null</code> to use the
	 * default separator factory.
	 */
	public void setSeparatorFactory(SeparatorFactory separatorFactory) {
		if (separatorFactory == null) {
			separatorFactory = new DefaultSeparatorFactory();
		}
		this.separatorFactory = separatorFactory;
	}

	/**
	 * Returns the button factory to use for creating breadcrumb buttons.
	 */
	public ButtonFactory getButtonFactory() {
		return buttonFactory;
	}

	/**
	 * Sets the button factory to use for creating breadcrumb buttons. Set this
	 * value to <code>null</code> to use the default button factory.
	 */
	public void setButtonFactory(ButtonFactory buttonFactory) {
		if (buttonFactory == null) {
			buttonFactory = new DefaultButtonFactory();
		}
		this.buttonFactory = buttonFactory;
	}

	private void addBreadcrumbsForControllerRemovingAnyExistingOnes() {
		removeBreadcrumbs();
		if (getController() != null) {
			for (View view : getController().getViewStack()) {
				addBreadcrumbForView(view);
			}
		}
	}

	@Override
	public void handleNavigationControllerEvent(NavigationControllerEvent event) {
		if (event.getSource() != getController()
				|| !(event instanceof CurrentNavigationControllerViewChangedEvent)) {
			return;
		}
		// TODO Maybe this method should be optimized so that not all buttons
		// need to be removed unless absolutely necessary.
		addBreadcrumbsForControllerRemovingAnyExistingOnes();
	}

	protected void addBreadcrumbForView(final View view) {
		addSeparatorForView(view);
		final Button btn = getButtonFactory().createButton(view);
		final NavigationRequest navigationRequest = NavigationRequestBuilder
				.newInstance().startWithPathToView(getController(), view)
				.buildRequest();
		btn.addListener(new Button.ClickListener() {

			private static final long serialVersionUID = 5199653237630939848L;

			@Override
			public void buttonClick(ClickEvent event) {
				getController().navigate(navigationRequest);
			}
		});
		viewButtonMap.put(view, btn);
		view.addListener(this);
		addComponent(btn);
		setComponentAlignment(btn, Alignment.MIDDLE_LEFT);
	}

	protected void addSeparatorForView(final View view) {
		if (getController().containsMoreThanOneElement()) {
			Component separator = getSeparatorFactory().createSeparator();
			addComponent(separator);
			setComponentAlignment(separator, Alignment.MIDDLE_LEFT);
		}
	}

	protected void removeBreadcrumbs() {
		removeAllComponents();
		for (View view : viewButtonMap.keySet()) {
			view.removeListener(this);
		}
		viewButtonMap.clear();
	}

	@Override
	public void handleViewEvent(ViewEvent event) {
		if (event instanceof DisplayNameChangedViewEvent
				|| event instanceof DescriptionChangedViewEvent) {
			final Button btn = viewButtonMap.get(event.getSource());
			if (btn != null) {
				getButtonFactory().updateButtonTexts(btn, event.getSource());
			}
		}
	}
}
