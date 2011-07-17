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
package com.github.peholmst.mvp4vaadin.navigation.incubation;

import com.github.peholmst.mvp4vaadin.navigation.ControllableView;
import com.github.peholmst.mvp4vaadin.navigation.DefaultViewController;

/**
 * This is the default implementation of the {@link IdentifiableViewController}
 * interface. It does not require any special initialization, just create a new
 * instance and start using it.
 * 
 * @author Petter Holmström
 * @since 1.0
 * @deprecated Will be deleted in the 1.0 release
 */
@Deprecated
public class DefaultIdentifiableViewController extends DefaultViewController
		implements IdentifiableViewController {

	private static final long serialVersionUID = -6566446894265364755L;

	// TODO Test me!

	@Override
	public boolean containsIdentifiableView(String viewIdentifier) {
		if (getViewStack().isEmpty()) {
			return false;
		}
		for (ControllableView view : getViewStack()) {
			if (((IdentifiableControllableView) view).getViewIdentifier()
					.equals(viewIdentifier)) {
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean goToIdentifiableView(String viewIdentifier) {
		if (getViewStack().isEmpty()) {
			return false;
		}
		for (ControllableView view : getViewStack()) {
			if (((IdentifiableControllableView) view).getViewIdentifier()
					.equals(viewIdentifier)) {
				return goToView(view);
			}
		}
		return false;
	}

}
