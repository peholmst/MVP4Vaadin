package com.github.peholmst.mvp4vaadin.reader;

import com.vaadin.Application;
import com.vaadin.ui.*;

public class MVPReaderApplication extends Application {
	@Override
	public void init() {
		Window mainWindow = new Window("MVPReader");
		Label label = new Label("Hello Vaadin user");
		mainWindow.addComponent(label);
		setMainWindow(mainWindow);
	}

}
