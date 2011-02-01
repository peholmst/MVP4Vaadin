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


/**
 * 
 * @author petter
 * 
 */
public interface ViewProvider extends java.io.Serializable {

	/**
	 * 
	 * @param view
	 */
	void addPreinitializedView(ControllableView view);

	/**
	 * 
	 * @param view
	 * @param viewId
	 */
	void addPreinitializedView(ControllableView view, String viewId);

	/**
	 * 
	 * @param viewId
	 * @return
	 */
	ControllableView getView(String viewId);

	/**
	 * 
	 * @param <T>
	 * @param viewClass
	 * @return
	 */
	<T extends ControllableView> T getView(Class<T> viewClass);

	/**
	 * 
	 * @param view
	 * @return
	 */
	String getViewId(ControllableView view);
}
