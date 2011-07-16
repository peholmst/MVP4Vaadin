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

import java.util.logging.Level;
import java.util.logging.Logger;

import com.github.peholmst.mvp4vaadin.events.DescriptionChangedViewEvent;
import com.github.peholmst.mvp4vaadin.events.DisplayNameChangedViewEvent;
import com.github.peholmst.mvp4vaadin.events.InitializedViewEvent;
import com.github.peholmst.stuff4vaadin.adapter.Adaptable;
import com.github.peholmst.stuff4vaadin.adapter.AdaptableSupport;
import com.github.peholmst.stuff4vaadin.adapter.UnsupportedAdapterException;
import com.github.peholmst.stuff4vaadin.visitor.VisitableList;
import com.github.peholmst.stuff4vaadin.visitor.Visitor;

/**
 * This class is intended to be used as a delegate by {@link View}
 * implementations. It includes a full implementation of the {@link View}
 * interface. Usage of this class requires that the owning class implements the
 * {@link ViewDelegateOwner} interface.
 * <p>
 * This class exists in order to make it possible to extend different base
 * classes when implementing the {@link View} interface without having to
 * implement all the basic View methods again and again and again.
 * 
 * @see AbstractView
 * @see AbstractViewComponent
 * 
 * @author Petter Holmström
 * @since 1.0
 */
public class ViewDelegate<V extends View, P extends Presenter<V>> implements
		View {

	private static final long serialVersionUID = -8388839248083280057L;

	private String displayName;

	private String description;

	private final VisitableList<ViewListener> listenerList = new VisitableList<ViewListener>();

	private boolean initialized = false;

	private transient Logger logger;

	private final ViewDelegateOwner<V, P> delegateOwner;

	private final AdaptableSupport adaptableSupport = new AdaptableSupport();

	private P presenter;

	/**
	 * Creates a new <code>ViewDelegate</code> of the specified delegate owner.
	 */
	public ViewDelegate(ViewDelegateOwner<V, P> delegateOwner) {
		this.delegateOwner = delegateOwner;
	}

	private Logger getLogger() {
		if (logger == null) {
			logger = Logger.getLogger(delegateOwner.getClass().getName());
		}
		return logger;
	}

	@Override
	public String getDisplayName() {
		return displayName;
	}

	/**
	 * Sets the display name of the view and fires a
	 * {@link DisplayNameChangedViewEvent}.
	 */
	public void setDisplayName(String displayName) {
		final String old = this.displayName;
		this.displayName = displayName;
		fireViewEvent(new DisplayNameChangedViewEvent(delegateOwner, old,
				displayName));
	}

	@Override
	public String getViewDescription() {
		return description;
	}

	/**
	 * Sets the description of the view and fires a
	 * {@link DescriptionChangedViewEvent}.
	 */
	public void setViewDescription(String description) {
		final String old = this.description;
		this.description = description;
		fireViewEvent(new DescriptionChangedViewEvent(delegateOwner, old,
				description));
	}

	/**
	 * Gets the presenter of the view.
	 * 
	 * @return the presenter instance (never <code>null</code> once the view has
	 *         been initialized).
	 */
	public P getPresenter() {
		return presenter;
	}

	/**
	 * Sets the presenter of the view. This method is useful for dependency
	 * injection frameworks. If the view has already been initialized, this
	 * method will throw an exception.
	 */
	public void setPresenter(P presenter) {
		if (isInitialized()) {
			throw new IllegalStateException("already initialized");
		}
		this.presenter = presenter;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see ViewDelegateOwner#createPresenter()
	 * @see ViewDelegateOwner#initView()
	 * @see ViewDelegateOwner#finalizeInitialization()
	 * @see InitializedViewEvent
	 */
	@Override
	public void init() {
		if (isInitialized()) {
			throw new IllegalStateException("already initialized");
		}
		if (presenter == null) {
			getLogger().log(Level.FINE, "Creating new presenter instance");
			presenter = delegateOwner.createPresenter();
		}

		getLogger().log(Level.FINE, "Initializing view {0}", this);
		delegateOwner.initView();

		getLogger().log(Level.FINE, "Initializing presenter {0}", presenter);
		presenter.init();

		getLogger().log(Level.FINE,
				"View and presenter initialized, finalizing initialization");
		delegateOwner.finalizeInitialization();
		initialized = true;

		fireViewEvent(new InitializedViewEvent(delegateOwner));
	}

	@Override
	public boolean isInitialized() {
		return initialized;
	}

	/**
	 * This method is intended to be used by unit tests only.
	 */
	void setInitialized(boolean initialized) {
		this.initialized = initialized;
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

	@Override
	public void fireViewEvent(final ViewEvent event) {
		if (event == null) {
			return;
		}
		getLogger().log(Level.FINE, "Firing event {0}", event);
		listenerList.visitItems(new Visitor<ViewListener>() {

			@Override
			public void visit(ViewListener visitable) {
				visitable.handleViewEvent(event);
			}
		});
	}

	@Override
	@Deprecated
	public String getDescription() {
		return getViewDescription();
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
	 * Returns the <code>AdaptableSupport</code> instance used by the view
	 * delegate to implement the {@link Adaptable} interface.
	 */
	public AdaptableSupport getAdaptableSupport() {
		return adaptableSupport;
	}

}
