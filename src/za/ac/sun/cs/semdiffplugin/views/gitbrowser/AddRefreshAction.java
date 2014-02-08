package za.ac.sun.cs.semdiffplugin.views.gitbrowser;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.resource.ImageDescriptor;

import za.ac.sun.cs.semdiffplugin.views.Utils;

public class AddRefreshAction extends Action {

	private GitBrowser gitBrowser = null;

	public AddRefreshAction(GitBrowser gitBrowser) {
		this.gitBrowser = gitBrowser;

		this.setText("Refresh");
		this.setToolTipText("Refresh repositories");

		ImageDescriptor myImage = Utils.getImageDescriptor("icons/refresh.gif");
		this.setImageDescriptor(myImage);
	}

	@Override
	public void run() {
		this.gitBrowser.getViewContentProvider().fullRefresh();
		this.gitBrowser.getTreeViewer().refresh(false);
	}

}
