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

import java.util.Collections;
import java.util.EmptyStackException;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Stack;

import com.github.peholmst.mvp4vaadin.View;

/**
 * TODO Document and implement me!
 * 
 * @author Petter Holmström
 * @since 1.0
 */
public class DefaultNavigationController implements NavigationController {

	private static final long serialVersionUID = 6838003395877804584L;
	
	private final Stack<View> viewStack = new Stack<View>();
	
	@Override
	public NavigationResult navigate(NavigationRequest request) {
		for (View viewInPath : request.getPath()) {
			if (!viewStack.contains(viewInPath)) {
				viewStack.add(viewInPath);
			}
		}
		return NavigationResult.SUCCEEDED;
	}

	@Override
	public boolean navigateBack() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public List<View> getViewStack() {
		return Collections.unmodifiableList(viewStack);
	}

	/**
	 * This method is intended for unit testing only! Do not use for anything else!
	 */
	Stack<View> getModifiableViewStack() {
		return viewStack;
	}
	
	@Override
	public View getCurrentView() {
		try {
			return viewStack.peek();
		} catch (EmptyStackException e) {
			return null;
		}
	}

	@Override
	public View getFirstView() {
		try {
			return viewStack.firstElement();
		} catch (NoSuchElementException e) {
			return null;
		}
	}

	@Override
	public boolean isEmpty() {
		return viewStack.isEmpty();
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
