package za.ac.sun.cs.semdiffplugin.views.gitbrowser;

import org.eclipse.jface.action.Action;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;

import za.ac.sun.cs.semdiffplugin.views.Utils;

public class AddRepoAction extends Action {

	private GitBrowser gitBrowser = null;

	public AddRepoAction(GitBrowser gitBrowser) {
		this.gitBrowser = gitBrowser;

		this.setText("Add");
		this.setToolTipText("Add Git repository");
		this.setImageDescriptor(PlatformUI.getWorkbench().getSharedImages()
				.getImageDescriptor(ISharedImages.IMG_OBJ_ADD));
	}

	@Override
	public void run() {
		DirectoryDialog dd = new DirectoryDialog(gitBrowser.getTreeViewer()
				.getControl().getShell());
		dd.setText("Select location of git repository");
		String repo = dd.open();

		if (repo == null) {
			return;
		}

		if (!Utils.isRepoAlreadyPresent(repo)) {
			Utils.addNewRepository(repo);
			gitBrowser.getViewContentProvider().addRepoToTree(repo);
			this.gitBrowser.getViewContentProvider().fullRefresh();
			gitBrowser.getTreeViewer().refresh(false);
		} else {
			Utils.showMessage(gitBrowser.getTreeViewer().getControl()
					.getShell(), "Information",
					"The repository has already been added.");
		}
	}

}
