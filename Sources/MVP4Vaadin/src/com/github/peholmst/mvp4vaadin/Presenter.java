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
import com.github.peholmst.stuff4vaadin.adapter.AdaptableSupport;
import com.github.peholmst.stuff4vaadin.adapter.UnsupportedAdapterException;

/**
 * This is an abstract base class for Presenters in the Model-View-Presenter
 * (MVP) pattern. It has been designed to work together with views that extend
 * the {@link AbstractView} or {@link AbstractViewComponent} base class.
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
 * @see AbstractViewComponent
 * @author Petter Holmström
 * @since 1.0
 * @param <V>
 *            the type of the View that uses the Presenter.
 */
public abstract class Presenter<V extends View> implements Adaptable {

	private static final long serialVersionUID = -7842839205919502161L;

	private V view;

	private final AdaptableSupport adaptableSupport = new AdaptableSupport();

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
	 * Creates a new <code>Presenter</code> without a view. A view must be
	 * specified using the {@link #setView(View)} method.
	 */
	public Presenter() {
	}

	/**
	 * Sets the view for this presenter.
	 * 
	 * @param view
	 *            the view to set.
	 */
	public void setView(V view) {
		this.view = view;
	}

	/**
	 * Gets the view that uses this presenter. If no view has been set, an
	 * exception is thrown.
	 * 
	 * @return the view instance (never <code>null</code>).
	 */
	public V getView() {
		if (view == null) {
			throw new IllegalStateException("View has not been set yet");
		}
		return view;
	}

	/**
	 * Convenience method that delegates to
	 * {@link View#fireViewEvent(ViewEvent)}.
	 * 
	 * @param event
	 *            the event to fire.
	 */
	public void fireViewEvent(ViewEvent event) {
		getView().fireViewEvent(event);
	}

	/**
	 * This method is called to initialize the presenter. When this happens, the
	 * view will already be initialized, i.e. invoking any methods on the view
	 * will not throw any exceptions.
	 * <p>
	 * This implementation does nothing, subclasses may override.
	 */
	public void init() {
		// NOP
	}

	@Override
	public boolean supportsAdapter(Class<?> adapterClass) {
		return adaptableSupport.supportsAdapter(adapterClass);
	}

	@Override
	public <T> T adapt(Class<T> adapterClass)
			throws UnsupportedAdapterException {
		return adaptableSupport.adapt(adapterClass);
	}

	/**
	 * Returns the <code>AdaptableSupport</code> instance used by the presenter.
	 */
	protected AdaptableSupport getAdaptableSupport() {
		return adaptableSupport;
	}
}
