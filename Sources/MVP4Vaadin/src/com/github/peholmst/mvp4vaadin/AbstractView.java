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

import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This is an abstract base class for {@link View} implementations. It has been
 * designed to be used together with concrete {@link Presenter} implementations.
 * 
 * @author Petter Holmström
 * @since 1.0
 * 
 * @param <V>
 *            the type of the View.
 * @param <P>
 *            the type of the Presenter.
 */
public abstract class AbstractView<V extends View, P extends Presenter<V>>
		implements View {

	private static final long serialVersionUID = 8812702399992511588L;

	private transient Logger logger;

	private P presenter;

	private LinkedList<ViewListener> listenerList = new LinkedList<ViewListener>();

	/**
	 * Returns a logger that can be used to log important information.
	 * 
	 * @return a logger instance, never <code>null</code>.
	 */
	protected Logger getLogger() {
		if (logger == null) {
			logger = Logger.getLogger(getClass().getName());
		}
		return logger;
	}

	/**
	 * This method will be called by the {@link #init()}-method to create the
	 * presenter.
	 * 
	 * @see Presenter#Presenter(View)
	 * 
	 * @return a new presenter instance (never <code>null</code>).
	 */
	protected abstract P createPresenter();

	private boolean initialized = false;

	/**
	 * {@inheritDoc}
	 * 
	 * @see #initView()
	 * @see #finalizeInitialization()
	 */
	@Override
	public void init() throws IllegalStateException {
		if (initialized) {
			throw new IllegalStateException("already initialized");
		}
		getLogger().log(Level.FINE, "Creating presenter");
		presenter = createPresenter();
		getLogger().log(Level.FINE, "Initializing view {0}", this);
		initView();
		getLogger().log(Level.FINE, "Initializing presenter {0}", presenter);
		presenter.init();
		getLogger().log(Level.FINE,
				"View and presenter initialized, finalizing initialization");
		finalizeInitialization();
		initialized = true;
	}

	/**
	 * Initializes the view, e.g. creates and configures visual components,
	 * event listeners, etc. When this method is called, {@link #getPresenter()}
	 * will return a presenter instance, but it will not have been initialized
	 * yet. In other words, if the presenter requires initialization (not all
	 * presenters do), it must not be invoked by this method.
	 * 
	 * @see #finalizeInitialization()
	 */
	protected abstract void initView();

	/**
	 * This method is called by the {@link #init()}-method after both the view
	 * and the presenter have been initialized.
	 * <p>
	 * This implementation is empty, subclasses may override.
	 */
	protected void finalizeInitialization() {

	}

	/**
	 * Gets the presenter for this view.
	 * 
	 * @return the presenter instance (never <code>null</code> once the view has
	 *         been initialized).
	 */
	public P getPresenter() {
		return presenter;
	}

	@Override
	public boolean isInitialized() {
		return initialized;
	}

	@Override
	public void addListener(ViewListener listener) {
		if (listener != null) {
			listenerList.add(listener);
		}
	}

	@Override
	public void removeListener(ViewListener listener) {
		if (listener != null) {
			listenerList.remove(listener);
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public void fireViewEvent(ViewEvent event) {
		if (event == null) {
			return;
		}
		getLogger().log(Level.FINE, "Firing event {0}", event);
		/*
		 * Create a clone of the listener list. This way, we prevent weird
		 * situations if any of the listeners register new listeners or remove
		 * existing ones.
		 */
		LinkedList<ViewListener> clonedList = (LinkedList<ViewListener>) listenerList
				.clone();
		for (ViewListener listener : clonedList) {
			listener.handleViewEvent(event);
		}
	}

}
