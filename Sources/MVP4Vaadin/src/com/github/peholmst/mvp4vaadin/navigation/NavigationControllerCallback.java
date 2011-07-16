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

import java.util.Map;

import com.github.peholmst.mvp4vaadin.View;


/**
 * TODO Define and document me!
 * 
 * @author Petter Holmström
 * @since 1.0
 */
public interface NavigationControllerCallback extends java.io.Serializable {

	/**
	 * 
	 * @param controller
	 */
	void attachedToController(NavigationController controller);
	
	/**
	 * 
	 * @param controller
	 * @return
	 */
	boolean detachingFromController(NavigationController controller);
	
	/**
	 * 
	 * @param controller
	 */
	void detachedFromController(NavigationController controller);
	
	/**
	 * 
	 * @param userData
	 * @param fromView
	 */
	void navigatedForwardToView(Map<String, Object> userData, View fromView);
	
	/**
	 * 
	 * @param fromView
	 */
	void navigatedBackwardToView(View fromView);

	/**
	 * 
	 * @param toView
	 */
	void navigatedForwardFromView(View toView);
}
