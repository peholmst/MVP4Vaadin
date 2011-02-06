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

import java.util.logging.Logger;

/**
 * This is an abstract base class for Presenters in the Model-View-Presenter
 * (MVP) pattern. It has been designed to work together with views that extend
 * the {@link AbstractView} base class.
 * <p>
 * The presenter contains the logic that controls the GUI. It reads information
 * from the Model and passes it to to the View for showing. It also receives
 * events from the View and decides what to do with them.
 * <p>
 * Normally, the presenter should not contain any code that couples it to a
 * particular View framework, such as Vaadin, JSP or Swing. There are two major
 * advantages with this approach:
 * <ol>
 * <li>It makes it possible to write unit tests for the GUI logic that do not
 * need to simulate user input.</li>
 * <li>It makes it easy (well, easier to be completely honest) to rewrite the
 * GUI using another framework, as only the views need to be reimplemented (in a
 * perfect world, that is).</li>
 * </ol>
 * 
 * @see AbstractView
 * @author Petter Holmström
 * @since 1.0
 * @param <V>
 *            the type of the View that uses the Presenter.
 */
public abstract class Presenter<V extends View> implements java.io.Serializable {

	private static final long serialVersionUID = -7842839205919502161L;

	private transient Logger logger;

	private final V view;

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
	 * Creates a new <code>Presenter</code> for the specified view. Any
	 * initialization code should go in the {@link #init()} method. When this
	 * constructor is invoked, the view might not yet have been initialized.
	 * 
	 * @see AbstractView#createPresenter()
	 * 
	 * @param view
	 *            the view that uses the presenter (must not be
	 *            <code>null</code>).
	 */
	public Presenter(V view) {
		if (view == null) {
			throw new IllegalArgumentException("null view");
		}
		this.view = view;
	}

	/**
	 * Gets the view that uses this presenter.
	 * 
	 * @return the view instance (never <code>null</code>).
	 */
	protected final V getView() {
		return view;
	}

	/**
	 * This method is called by {@link AbstractView#init()} to initialize the
	 * presenter. When this method is called, the view will already have been
	 * initialized using {@link AbstractView#initView()}.
	 * <p>
	 * This implementation does nothing, subclasses may override.
	 */
	public void init() {
		// NOP
	}
}
