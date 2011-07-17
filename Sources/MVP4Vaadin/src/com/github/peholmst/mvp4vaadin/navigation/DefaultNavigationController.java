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

import java.util.List;

import com.github.peholmst.mvp4vaadin.View;

/**
 * TODO Document and implement me!
 * 
 * @author Petter Holmström
 * @since 1.0
 */
public class DefaultNavigationController implements NavigationController {

	private static final long serialVersionUID = 6838003395877804584L;

	@Override
	public NavigationResult navigate(NavigationRequest request) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean navigateBack() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public List<View> getViewStack() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public View getCurrentView() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public View getFirstView() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isEmpty() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public NavigationResult clear() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void addListener(NavigationControllerListener listener) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void removeListener(NavigationControllerListener listener) {
		// TODO Auto-generated method stub
		
	}

}
