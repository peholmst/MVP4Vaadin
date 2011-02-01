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

/**
 * Listener interface to be implemented by classes that need to be notified of
 * {@link ViewEvent}s.
 * 
 * @see View#addListener(ViewListener)
 * @see View#removeListener(ViewListener)
 * 
 * @author Petter Holmström
 * @since 1.0
 */
public interface ViewListener extends java.io.Serializable {

	/**
	 * Called when <code>event</code> has occurred.
	 * 
	 * @param event
	 *            the event (must not be <code>null</code>).
	 */
	void handleViewEvent(ViewEvent event);
}
