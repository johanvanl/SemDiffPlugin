package za.ac.sun.cs.semdiffplugin.views.gitbrowser;

import org.eclipse.jface.action.Action;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;

import za.ac.sun.cs.semdiffplugin.tree.TreeCommit;
import za.ac.sun.cs.semdiffplugin.tree.TreeFile;
import za.ac.sun.cs.semdiffplugin.tree.TreeRepo;
import za.ac.sun.cs.semdiffplugin.views.Utils;
import za.ac.sun.cs.semdiffplugin.views.diffview.DiffViews;

import com.gitblit.utils.JGitUtils;

public class DiffAgainstAction extends Action {

	private GitBrowser gitBrowser = null;
	private TreeFile file = null;

	public DiffAgainstAction(GitBrowser gitBrowser, TreeFile file) {
		this.gitBrowser = gitBrowser;
		this.file = file;
		this.setText("Diff against");
	}

	@Override
	public void run() {

		DiffViews dv = null;
		try {
			// Get the DiffViews as well as show the eclipse view if not shown
			dv = (DiffViews) PlatformUI.getWorkbench()
					.getActiveWorkbenchWindow().getActivePage()
					.showView("views.TextViews");
		} catch (PartInitException e) {
			return;
		}

		TreeCommit commit = Utils.getTreeCommitFromTreeFile(file);
		TreeRepo treeRepo = (TreeRepo) commit.getParent();

		String text = "";
		try {
			text = JGitUtils.getStringContent(treeRepo.getRepo(),
					commit.getCommit(), file.getFilename());
		} catch (Exception e) {
			Utils.showMessage(gitBrowser.getTreeViewer().getControl()
					.getShell(), "Error", "Error reading file contents");
			return;
		}

		dv.setOriginaltext(text);
		dv.diff();
	}

}
