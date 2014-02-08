package za.ac.sun.cs.semdiffplugin.views.gitbrowser;

import org.eclipse.jface.action.Action;

import za.ac.sun.cs.semdiffplugin.tree.TreeRepo;
import za.ac.sun.cs.semdiffplugin.views.Utils;

/**
 * Only to be used with the right click of an TreeRepo.
 */
public class DeleteAction extends Action {

	private GitBrowser gitBrowser = null;
	private TreeRepo treeRepo = null;

	public DeleteAction(GitBrowser gitBrowser, TreeRepo repo) {
		this.gitBrowser = gitBrowser;
		this.treeRepo = repo;
		this.setText("Delete Repo");
	}

	@Override
	public void run() {
		String location = treeRepo.getRepo().getWorkTree().getAbsolutePath();
		Utils.removeRepository(location);
		this.gitBrowser.getViewContentProvider().removeRepoFromTree(treeRepo);
		this.gitBrowser.getTreeViewer().refresh(false);
	}

}
