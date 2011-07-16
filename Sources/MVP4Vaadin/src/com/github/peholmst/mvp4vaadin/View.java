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

import com.github.peholmst.stuff4vaadin.adapter.Adaptable;

/**
 * This interface represents a View in the Model-View-Presenter (MVP) pattern.
 * The view is responsible for displaying information to the user when requested
 * by the presenter. The view also informs the presenter of actions taken by the
 * user. In addition, it is possible to fire {@link ViewEvent}s from the view.
 * <p>
 * Normally, there is a circular dependency between a view and a presenter. That
 * is, the view holds a reference to its presenter and the presenter holds a
 * reference to its view. In the case of this particular MVP-implementation, the
 * view is always initialized first and is also responsible for both creating
 * and initializing the presenter (see {@link #init()}).
 * 
 * @see AbstractView
 * @see AbstractViewComponent
 * 
 * @author Petter Holmström
 * @since 1.0
 */
public interface View extends Adaptable {

	/**
	 * Gets the display name of the view. This name will be shown to the user in
	 * the UI. If this method is called before the view has been initialized,
	 * the results are undefined.
	 * 
	 * @see #isInitialized()
	 * 
	 * @return the display name (never <code>null</code> but may be empty).
	 */
	String getDisplayName();

	/**
	 * Gets the description of the view. This description might be shown to the
	 * user in the UI, e.g. inside a tooltip. If this method is called before
	 * the view has been initialized, the results are undefined.
	 * 
	 * @see #isInitialized()
	 * 
	 * @return the description (never <code>null</code> but may be empty).
	 */
	String getViewDescription();

	/**
	 * @deprecated This property collides with the <code>getDescription()</code>
	 *             method of the Vaadin components and will be removed before
	 *             the 1.0 release. Use {@link #getViewDescription()} instead.
	 */
	@Deprecated
	String getDescription();

	/**
	 * This method initializes both the view and the presenter. After this
	 * method has been called, {@link #isInitialized()} will always return true.
	 * 
	 * @throws IllegalStateException
	 *             if the view has already been initialized when this method is
	 *             called.
	 */
	void init();

	/**
	 * Checks if the view (and the presenter) has been initialized.
	 * 
	 * @see #init()
	 * @return true if the view has been initialized, false otherwise.
	 */
	boolean isInitialized();

	/**
	 * Registers a listener to be notified of {@link ViewEvent}s. A listener can
	 * be registered several times and will be notified once for each
	 * registration. If the listener is <code>null</code>, nothing happens.
	 * 
	 * @param listener
	 *            the listener to add.
	 */
	void addListener(ViewListener listener);

	/**
	 * Unregisters a listener previously registered using
	 * {@link #addListener(ViewListener)}. If the listener was registered
	 * multiple times, it will be notified one time less after this method
	 * invocation. If the listener is <code>null</code> or was never added,
	 * nothing happens.
	 * 
	 * @param listener
	 *            the listener to remove.
	 */
	void removeListener(ViewListener listener);

	/**
	 * Fires <code>event</code> to all the registered {@link ViewListener}s.
	 * This method is primarily intended to be called by the presenter, but may
	 * be called by other classes as well. If the event is <code>null</code>,
	 * nothing happens.
	 * 
	 * @see #addListener(ViewListener)
	 * @see #removeListener(ViewListener)
	 * @param event
	 *            the event to fire.
	 */
	void fireViewEvent(ViewEvent event);
}
