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

import java.util.Map;

import com.github.peholmst.mvp4vaadin.AbstractView;
import com.github.peholmst.mvp4vaadin.Presenter;

/**
 * This is an abstract base class for {@link ControllableView} implementations.
 * 
 * @author Petter Holmström
 * @since 1.0
 * 
 * @param <V>
 *            the type of the View.
 * @param <P>
 *            the type of the Presenter.
 */
public abstract class AbstractControllableView<V extends ControllableView, P extends Presenter<V>>
		extends AbstractView<V, P> implements ControllableView {

	private static final long serialVersionUID = 6769129811745901667L;

	private ViewController viewController;

	@Override
	public ViewController getViewController() {
		return viewController;
	}

	/**
	 * {@inheritDoc}
	 * <p>
	 * This implementation updates the {@link #getViewController()
	 * viewController} property, then delegates to
	 * {@link #doShowView(Map, ControllableView, Direction)}.
	 */
	@Override
	public final void showView(ViewController viewController,
			Map<String, Object> userData, ControllableView oldView,
			Direction direction) {
		if (getViewController() != null
				&& getViewController() != viewController) {
			throw new IllegalStateException(
					"View is already controlled by another controller");
		}
		this.viewController = viewController;
		doShowView(userData, oldView, direction);
	}

	/**
	 * This method is called by
	 * {@link #showView(ViewController, Map, ControllableView, Direction)} after
	 * the view controller has been set. This implementation does nothing,
	 * subclasses may override.
	 * 
	 * @see #getViewController().
	 */
	protected void doShowView(Map<String, Object> userData,
			ControllableView oldView, Direction direction) {
	}

	/**
	 * {@inheritDoc}
	 * <p>
	 * This implementation delegates to
	 * {@link #doHideView(ControllableView, Direction)}. If the returned
	 * {@link HideOperation} allows the operation, the
	 * {@link #getViewController() viewController} property is reset to
	 * <code>null</code>.
	 */
	@Override
	public final HideOperation hideView(ViewController viewController,
			ControllableView newView, Direction direction) {
		if (getViewController() == null
				|| getViewController() != viewController) {
			throw new IllegalStateException(
					"View is already controlled by another controller "
							+ "or is not controlled at all");
		}
		HideOperation operation = doHideView(newView, direction);
		if (operation != HideOperation.PREVENT) {
			this.viewController = null;
		}
		return operation;
	}

	/**
	 * This method is called by
	 * {@link #hideView(ViewController, ControllableView, Direction)} before the
	 * view controller has been reset. This implementation always returns
	 * <code>ALLOW</code>, subclasses may override.
	 * 
	 * @see #getViewController()
	 */
	protected HideOperation doHideView(ControllableView newView,
			Direction direction) {
		return HideOperation.ALLOW;
	}

}