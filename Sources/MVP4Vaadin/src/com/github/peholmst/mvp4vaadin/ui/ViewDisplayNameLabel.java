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
package com.github.peholmst.mvp4vaadin.ui;

import com.github.peholmst.mvp4vaadin.View;
import com.github.peholmst.mvp4vaadin.ViewEvent;
import com.github.peholmst.mvp4vaadin.ViewListener;
import com.github.peholmst.mvp4vaadin.events.DisplayNameChangedViewEvent;
import com.vaadin.ui.Label;

/**
 * A label that contains the display name of a certain view. When the display
 * name changes, the label is automatically updated.
 * 
 * @author Petter Holmström
 * @since 1.0
 */
@SuppressWarnings("unchecked")
public class ViewDisplayNameLabel extends Label implements ViewListener {

	private static final long serialVersionUID = -664243311290214350L;

	private View view;

	/**
	 * Creates a new <code>ViewDisplayNameLabel</code> without a view.
	 */
	public ViewDisplayNameLabel() {
	}

	/**
	 * Creates a new <code>ViewDisplayNameLabel</code> with the specified view.
	 */
	public ViewDisplayNameLabel(View view) {
		setView(view);
	}

	/**
	 * Returns the view whose display name is shown in this label, or
	 * <code>null</code> if none has been set.
	 */
	public View getView() {
		return view;
	}

	/**
	 * Sets the view whose display name should be shown in this label, may be
	 * <code>null</code>.
	 */
	public void setView(View view) {
		if (this.view != null) {
			this.view.removeListener(this);
		}
		this.view = view;
		updateLabelValue();
		if (this.view != null) {
			this.view.addListener(this);
		}
	}

	@Override
	public void handleViewEvent(ViewEvent event) {
		if (event.getSource() == view
				&& event instanceof DisplayNameChangedViewEvent) {
			updateLabelValue();
		}
	}

	private void updateLabelValue() {
		if (view == null) {
			setValue("");
		} else {
			setValue(view.getDisplayName());
		}
	}

}
