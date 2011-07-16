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
public class NavigationControllerCallbackAdapter implements NavigationControllerCallback {

	private static final long serialVersionUID = -4007523174344250137L;

	/* (non-Javadoc)
	 * @see com.github.peholmst.mvp4vaadin.navigation.NavigationControllerCallback#attachedToController(com.github.peholmst.mvp4vaadin.navigation.NavigationController)
	 */
	@Override
	public void attachedToController(NavigationController controller) {
		// TODO Auto-generated method stub
		
	}

	/* (non-Javadoc)
	 * @see com.github.peholmst.mvp4vaadin.navigation.NavigationControllerCallback#detachingFromController(com.github.peholmst.mvp4vaadin.navigation.NavigationController)
	 */
	@Override
	public boolean detachingFromController(NavigationController controller) {
		// TODO Auto-generated method stub
		return false;
	}

	/* (non-Javadoc)
	 * @see com.github.peholmst.mvp4vaadin.navigation.NavigationControllerCallback#detachedFromController(com.github.peholmst.mvp4vaadin.navigation.NavigationController)
	 */
	@Override
	public void detachedFromController(NavigationController controller) {
		// TODO Auto-generated method stub
		
	}

	/* (non-Javadoc)
	 * @see com.github.peholmst.mvp4vaadin.navigation.NavigationControllerCallback#navigatedForwardToView(java.util.Map, com.github.peholmst.mvp4vaadin.View)
	 */
	@Override
	public void navigatedForwardToView(Map<String, Object> userData,
			View fromView) {
		// TODO Auto-generated method stub
		
	}

	/* (non-Javadoc)
	 * @see com.github.peholmst.mvp4vaadin.navigation.NavigationControllerCallback#navigatedBackwardToView(com.github.peholmst.mvp4vaadin.View)
	 */
	@Override
	public void navigatedBackwardToView(View fromView) {
		// TODO Auto-generated method stub
		
	}

	/* (non-Javadoc)
	 * @see com.github.peholmst.mvp4vaadin.navigation.NavigationControllerCallback#navigatedForwardFromView(com.github.peholmst.mvp4vaadin.View)
	 */
	@Override
	public void navigatedForwardFromView(View toView) {
		// TODO Auto-generated method stub
		
	}

}
