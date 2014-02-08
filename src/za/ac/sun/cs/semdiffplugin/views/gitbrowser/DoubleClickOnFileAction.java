package za.ac.sun.cs.semdiffplugin.views.gitbrowser;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;

import za.ac.sun.cs.semdiffplugin.tree.TreeCommit;
import za.ac.sun.cs.semdiffplugin.tree.TreeFile;
import za.ac.sun.cs.semdiffplugin.tree.TreeRepo;
import za.ac.sun.cs.semdiffplugin.views.Utils;
import za.ac.sun.cs.semdiffplugin.views.diffview.DiffViews;

public class DoubleClickOnFileAction extends Action {

	private GitBrowser gitBrowser = null;

	public DoubleClickOnFileAction(GitBrowser gitBrowser) {
		this.gitBrowser = gitBrowser;
	}

	@Override
	public void run() {
		ISelection selection = gitBrowser.getTreeViewer().getSelection();
		Object obj = ((IStructuredSelection) selection).getFirstElement();

		if (!(obj instanceof TreeFile)) {
			return;
		}

		TreeFile treeFile = (TreeFile) obj;
		DiffViews dv = null;
		try {
			// Get the DiffViews as well as show the eclipse view if not shown
			dv = (DiffViews) PlatformUI.getWorkbench()
					.getActiveWorkbenchWindow().getActivePage()
					.showView("views.TextViews");
		} catch (PartInitException e) {
			return;
		}

		TreeCommit treeCommit = Utils.getTreeCommitFromTreeFile(treeFile);
		TreeRepo treeRepo = (TreeRepo) treeCommit.getParent();

		String revised_text = Utils.getText(treeRepo, treeCommit, treeFile);
		String original_text = Utils.getParentText(treeRepo, treeCommit,
				treeFile);

		if (original_text == null) {
			original_text = "";
		}

		dv.setFileName(treeFile.getFilename());
		dv.setOriginaltext(original_text);
		dv.setRevisedtext(revised_text);
		dv.diff();
	}

}
