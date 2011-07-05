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

/**
 * This interface provides defines some methods that a {@link ViewDelegate} will
 * require from its owner in order to operate properly.
 * 
 * @see ViewDelegate
 * 
 * @author Petter Holmström
 * @since 1.0
 */
public interface ViewDelegateOwner<V extends View, P extends Presenter<V>>
		extends View {

	/**
	 * Initializes the view, e.g. creates and configures visual components,
	 * event listeners, etc. When this method is called,
	 * {@link ViewDelegate#getPresenter()} will return a presenter instance, but
	 * it will not have been initialized yet. In other words, if the presenter
	 * requires initialization (not all presenters do), it must not be invoked
	 * by this method.
	 * 
	 * @see #finalizeInitialization()
	 */
	void initView();

	/**
	 * This method is called by the {@link ViewDelegate#init()}-method after
	 * both the view and the presenter have been initialized, but before the
	 * {@link ViewDelegate#isInitialized() initialized} flag is set to true.
	 */
	void finalizeInitialization();

	/**
	 * This method will be used by the {@link ViewDelegate} to create a new
	 * presenter instance if none has been specified when
	 * {@link ViewDelegate#init()} is called.
	 * 
	 * @see ViewDelegate#setPresenter(Presenter)
	 * 
	 * @return a new presenter instance (never <code>null</code>).
	 */
	P createPresenter();

}
