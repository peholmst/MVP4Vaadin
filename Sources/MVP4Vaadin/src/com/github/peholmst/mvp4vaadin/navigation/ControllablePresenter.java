package com.github.peholmst.mvp4vaadin.navigation;

import java.util.Map;

import com.github.peholmst.mvp4vaadin.Presenter;

/**
 * This is an extended version of {@link Presenter} that has been designed to
 * work together with views that extend the {@link AbstractControllableView}
 * base class.
 * 
 * @see AbstractControllableView
 * @author Petter Holmström
 * @since 1.0
 * @param <V>
 *            the type of the View that uses the Presenter.
 */
@Deprecated
public abstract class ControllablePresenter<V extends ControllableView> extends
		Presenter<V> {

	private static final long serialVersionUID = -7958435335190869658L;

	/**
	 * Creates a new <code>ControllablePresenter</code> for the specified view.
	 * Any initialization code should go in the {@link Presenter#init()} method.
	 * When this constructor is invoked, the view might not yet have been
	 * initialized.
	 * 
	 * @param view
	 *            the view that uses the presenter (must not be
	 *            <code>null</code>).
	 */
	public ControllablePresenter(V view) {
		super(view);
	}

	/**
	 * Gets the view controller that currently controls the view that uses this
	 * presenter.
	 * 
	 * @see #getView()
	 * @see ControllableView#getViewController()
	 * @return the view controller, or <code>null</code> if the view is not
	 *         currently controlled.
	 */
	protected ViewController getViewController() {
		return getView().getViewController();
	}

	/**
	 * Called by
	 * {@link AbstractControllableView#showView(ViewController, java.util.Map, ControllableView, Direction)}
	 * . This implementation does nothing, subclasses may override.
	 * 
	 * @param viewController
	 *            the view controller (never <code>null</code>).
	 * @param userData
	 *            a map of user-definable parameters (may be <code>null</code>).
	 * @param oldView
	 *            the view that was previously visible (may be <code>null</code>
	 *            if this view is the first view to be shown).
	 * @param direction
	 *            the direction of the navigation inside the stack (must not be
	 *            <code>null</code>).
	 */
	protected void viewShown(ViewController viewController,
			Map<String, Object> userData, ControllableView oldView,
			Direction direction) {
	}
}
