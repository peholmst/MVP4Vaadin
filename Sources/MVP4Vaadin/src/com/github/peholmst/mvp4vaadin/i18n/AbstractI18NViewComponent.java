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
package com.github.peholmst.mvp4vaadin.i18n;

import java.util.Locale;

import com.github.peholmst.i18n4vaadin.I18N;
import com.github.peholmst.i18n4vaadin.I18NComponent;
import com.github.peholmst.i18n4vaadin.I18NListener;
import com.github.peholmst.i18n4vaadin.support.I18NComponentSupport;
import com.github.peholmst.mvp4vaadin.AbstractViewComponent;
import com.github.peholmst.mvp4vaadin.Presenter;
import com.github.peholmst.mvp4vaadin.View;

/**
 * This is an extended version of {@link AbstractI18NViewComponent} that also
 * adds support for <a
 * href="https://github.com/peholmst/I18N4Vaadin">I18N4Vaadin</a>.
 * <p>
 * The component implements the {@link I18NComponent} interface and delegates to
 * {@link I18NComponentSupport}. This means that as soon as the component is
 * attached to a component that has another {@link I18NComponent} somewhere in
 * its parent chain, it will immediately get access to that component's
 * {@link I18N} instance. Thus, you only need call
 * {@link I18NComponent#setI18N(I18N)} on the top-most component in the chain.
 * 
 * @author Petter Holmström
 * @since 1.0
 */
public abstract class AbstractI18NViewComponent<V extends View, P extends Presenter<V>>
		extends AbstractViewComponent<V, P> implements I18NComponent,
		I18NListener {

	private static final long serialVersionUID = -6495255909287389468L;

	private final I18NComponentSupport i18nSupport = new I18NComponentSupport(
			this);
	
	public AbstractI18NViewComponent() {
		super();
	}

	public AbstractI18NViewComponent(Class<P> presenterClass, Class<V> viewClass) {
		super(presenterClass, viewClass);
	}

	@Override
	public void setI18N(I18N i18n) {
		i18nSupport.setI18N(i18n);
	}

	@Override
	public I18N getI18N() {
		return i18nSupport.getI18N();
	}

	@Override
	public void attach() {
		super.attach();
		if (getI18N() == null) {
			throw new IllegalStateException("No I18N attached to component");
		}
		getI18N().addListener(this);
		updateInternationalizedData(getI18N().getCurrentLocale());
	}

	@Override
	public void detach() {
		if (getI18N() != null) {
			getI18N().removeListener(this);
		}
		super.detach();
	}

	@Override
	public void localeChanged(I18N sender, Locale oldLocale, Locale newLocale) {
		updateInternationalizedData(newLocale);
	}

	/**
	 * This method is called when the component is attached to another component
	 * or when the current locale changes. All internationalized data (e.g.
	 * message strings, format strings, etc.) should be updated.
	 * 
	 * @param locale
	 *            the new locale, may be <code>null</code> if no locale is
	 *            available.
	 */
	protected abstract void updateInternationalizedData(Locale locale);

}
