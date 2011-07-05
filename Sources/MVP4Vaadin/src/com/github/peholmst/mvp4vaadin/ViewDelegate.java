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
package com.github.peholmst.mvp4vaadin;

import com.github.peholmst.mvp4vaadin.events.DescriptionChangedViewEvent;
import com.github.peholmst.mvp4vaadin.events.DisplayNameChangedViewEvent;

/**
 * TODO Document and test me!
 * 
 * @author Petter Holmström
 * @since 1.0
 */
public class ViewDelegate implements View {

	private static final long serialVersionUID = -8388839248083280057L;
	
	private String displayName;
	
	private String description;

	private final View delegateOwner;
	
	/**
	 * 
	 */
	public ViewDelegate(View delegateOwner) {
		this.delegateOwner = delegateOwner;
	}
	
	@Override
	public String getDisplayName() {
		return displayName;
	}
	
	/**
	 * 
	 * @param displayName
	 */
	public void setDisplayName(String displayName) {
		final String old = this.displayName;
		this.displayName = displayName;
		fireViewEvent(new DisplayNameChangedViewEvent(delegateOwner, old, displayName));
	}

	@Override
	public String getDescription() {
		return description;
	}

	/**
	 * 
	 * @param description
	 */
	public void setDescription(String description) {
		final String old = this.description;
		this.description = description;
		fireViewEvent(new DescriptionChangedViewEvent(delegateOwner, old, description));
	}
	
	/* (non-Javadoc)
	 * @see com.github.peholmst.mvp4vaadin.View#init()
	 */
	@Override
	public void init() throws IllegalStateException {
		// TODO Auto-generated method stub
		
	}

	/* (non-Javadoc)
	 * @see com.github.peholmst.mvp4vaadin.View#isInitialized()
	 */
	@Override
	public boolean isInitialized() {
		// TODO Auto-generated method stub
		return false;
	}

	/* (non-Javadoc)
	 * @see com.github.peholmst.mvp4vaadin.View#addListener(com.github.peholmst.mvp4vaadin.ViewListener)
	 */
	@Override
	public void addListener(ViewListener listener) {
		// TODO Auto-generated method stub
		
	}

	/* (non-Javadoc)
	 * @see com.github.peholmst.mvp4vaadin.View#removeListener(com.github.peholmst.mvp4vaadin.ViewListener)
	 */
	@Override
	public void removeListener(ViewListener listener) {
		// TODO Auto-generated method stub
		
	}

	/* (non-Javadoc)
	 * @see com.github.peholmst.mvp4vaadin.View#fireViewEvent(com.github.peholmst.mvp4vaadin.ViewEvent)
	 */
	@Override
	public void fireViewEvent(ViewEvent event) {
		// TODO Auto-generated method stub
		
	}

}
