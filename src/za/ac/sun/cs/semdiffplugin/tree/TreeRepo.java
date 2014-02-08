package za.ac.sun.cs.semdiffplugin.tree;

import org.eclipse.jgit.lib.Repository;

import com.gitblit.utils.JGitUtils;

public class TreeRepo extends TreeFolder {

	private Repository repo = null;

	public TreeRepo(Repository repo) {
		super(repo.getWorkTree().getAbsoluteFile().toString());
		this.repo = repo;
	}

	public Repository getRepo() {
		return this.repo;
	}

	@Override
	public String toString() {
		String out = this.repo.getWorkTree().getName();
		if (!JGitUtils.hasCommits(this.repo)) {
			out = "(Error repository missing) " + out;
		}
		return out;
	}

}
