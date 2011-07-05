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
package com.github.peholmst.mvp4vaadin.events;

import com.github.peholmst.mvp4vaadin.View;
import com.github.peholmst.mvp4vaadin.ViewEvent;

/**
 * Event indicating that the description of a view has changed.
 * 
 * @see View#getViewDescription()
 * 
 * @author Petter Holmström
 * @since 1.0
 */
public class DescriptionChangedViewEvent extends ViewEvent {

	private static final long serialVersionUID = -6354989637872610537L;

	private final String oldDescription;
	private final String newDescription;
	
	/**
	 * Creates a new <code>DescriptionChangedViewEvent</code>.
	 * 
	 * @param source
	 *            the view in which the event originally occurred (must not be
	 *            <code>null</code>).
	 *            @param oldDescription the old description (may be <code>null</code>).
	 *            @param newDescription the new description (may be <code>null</code>).
	 */
	public DescriptionChangedViewEvent(View source, String oldDescription, String newDescription) {
		super(source);
		this.oldDescription = oldDescription;
		this.newDescription = newDescription;
	}
	
	/**
	 * Returns the old description of the view.
	 */
	public String getOldDescription() {
		return oldDescription;
	}
	
	/**
	 * Returns the new description of the view.
	 */
	public String getNewDescription() {
		return newDescription;
	}

}
