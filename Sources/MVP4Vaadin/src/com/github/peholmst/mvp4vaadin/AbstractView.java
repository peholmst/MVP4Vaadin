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

import javax.annotation.PostConstruct;

import com.github.peholmst.stuff4vaadin.adapter.AdaptableSupport;
import com.github.peholmst.stuff4vaadin.adapter.UnsupportedAdapterException;

/**
 * This is an abstract base class for {@link View} implementations. It has been
 * designed to be used together with concrete {@link Presenter} implementations.
 * It delegates all {@link View}-methods to a {@link ViewDelegate}.
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
		implements ViewDelegateOwner<V, P> {

	private static final long serialVersionUID = 8812702399992511588L;

	private final ViewDelegate<V, P> viewDelegate;

	/**
	 * Creates a new <code>AbstractView</code>, but does NOT initialize it. It
	 * has to be initialized later by calling {@link #init()}. This constructor
	 * is useful if any resources need to be injected into the view before it is
	 * initialized.
	 */
	public AbstractView() {
		viewDelegate = new ViewDelegate<V, P>(this);
	}

	/**
	 * Creates a new <code>AbstractView</code> and optionally initializes it by
	 * calling {@link #init()}.
	 * <p>
	 * Please note that the {@link #init()} method is annotated with
	 * <code>@PostConstruct</code>, so if you are using Spring or CDI to create
	 * the view, the method will be invoked automatically and you don't have to
	 * use this constructor at all.
	 * 
	 * @param initialize
	 *            true to initialize the view directly, false to manually
	 *            initialize it later.
	 */
	public AbstractView(boolean initialize) {
		viewDelegate = new ViewDelegate<V, P>(this);
		if (initialize) {
			init();
		}
	}

	@Override
	@Deprecated
	public String getDescription() {
		return viewDelegate.getDescription();
	}

	@Override
	public String getViewDescription() {
		return viewDelegate.getViewDescription();
	}

	/**
	 * @see ViewDelegate#setViewDescription(String)
	 */
	protected void setViewDescription(String description) {
		viewDelegate.setViewDescription(description);
	}

	@Override
	public String getDisplayName() {
		return viewDelegate.getDisplayName();
	}

	/**
	 * @see ViewDelegate#setDisplayName(String)
	 */
	protected void setDisplayName(String displayName) {
		viewDelegate.setDisplayName(displayName);
	}

	/**
	 * {@inheritDoc}
	 * <p>
	 * The default implementation will throw an
	 * {@link UnsupportedOperationException} exception, subclasses should
	 * override unless the presenter is specified using
	 * {@link #setPresenter(Presenter)} prior to initialization.
	 */
	@Override
	public P createPresenter() {
		// TODO Use introspection to detect view and presenter classes, then
		// attempt to create presenter.
		throw new UnsupportedOperationException(
				"This method has not been implemented");
	}

	/**
	 * {@inheritDoc}
	 * <p>
	 * Please note that this method has been annotated with the
	 * {@link PostConstruct @PostConstruct} annotation. If the view is created
	 * using a container such as Spring or CDI, this method will be
	 * automatically invoked after the view has been created and all the
	 * dependencies have been injected.
	 * <p>
	 * Subclasses should preferably override {@link #initView()} or
	 * {@link #finalizeInitialization()} instead of this method.
	 * 
	 * @see #initView()
	 * @see #finalizeInitialization()
	 */
	@Override
	@PostConstruct
	public void init() {
		viewDelegate.init();
	}

	/**
	 * {@inheritDoc}
	 * <p>
	 * The default implementation does nothing, subclasses may override.
	 */
	@Override
	public void initView() {
	}

	/**
	 * {@inheritDoc}
	 * <p>
	 * This implementation is empty, subclasses may override.
	 */
	@Override
	public void finalizeInitialization() {
	}

	/**
	 * Gets the presenter for this view.
	 * 
	 * @return the presenter instance (never <code>null</code> once the view has
	 *         been initialized).
	 */
	public P getPresenter() {
		return viewDelegate.getPresenter();
	}

	/**
	 * Sets the presenter for this view. This method is useful for dependency
	 * injection frameworks. If the presenter has already been initialized, this
	 * method will throw an exception.
	 * 
	 * @param presenter
	 *            the presenter instance to set.
	 */
	public void setPresenter(P presenter) {
		viewDelegate.setPresenter(presenter);
	}

	@Override
	public boolean isInitialized() {
		return viewDelegate.isInitialized();
	}

	@Override
	public void addListener(ViewListener listener) {
		viewDelegate.addListener(listener);
	}

	@Override
	public void removeListener(ViewListener listener) {
		viewDelegate.removeListener(listener);
	}

	@Override
	public void fireViewEvent(ViewEvent event) {
		viewDelegate.fireViewEvent(event);
	}

	@Override
	public boolean supportsAdapter(Class<?> adapterClass) {
		return viewDelegate.supportsAdapter(adapterClass);
	}

	@Override
	public <T> T adapt(Class<T> adapterClass)
			throws UnsupportedAdapterException {
		return viewDelegate.adapt(adapterClass);
	}

	/**
	 * Returns the <code>AdaptableSupport</code> instance used by the view.
	 */
	protected AdaptableSupport getAdaptableSupport() {
		return viewDelegate.getAdaptableSupport();
	}

}
