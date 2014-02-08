package za.ac.sun.cs.semdiffplugin.views.gitbrowser;

import org.eclipse.jface.action.Action;

import za.ac.sun.cs.semdiffplugin.tree.TreeCommit;
import za.ac.sun.cs.semdiffplugin.views.Utils;

public class AddViewCommitMessageAction extends Action{
	
	private GitBrowser gitBrowser = null;
	private TreeCommit commit = null;
	
	public AddViewCommitMessageAction(GitBrowser gitbrowser, TreeCommit commit) {
		this.gitBrowser = gitbrowser;
		this.commit = commit;
		this.setText("View commit message");
	}

	@Override
	public void run() {
		Utils.showMessage(gitBrowser.getTreeViewer().getControl()
				.getShell(), "Commit message",this.commit.getCommit().getFullMessage());
	}
	
}
