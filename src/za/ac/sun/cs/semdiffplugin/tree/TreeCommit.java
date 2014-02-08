package za.ac.sun.cs.semdiffplugin.tree;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.eclipse.jgit.revwalk.RevCommit;

public class TreeCommit extends TreeFolder {

	private RevCommit commit = null;

	public TreeCommit(RevCommit commit) {
		super(commit.getName());
		this.commit = commit;
	}

	public RevCommit getCommit() {
		return this.commit;
	}

	@Override
	public String toString() {
		long epoch = commit.getCommitTime() * 1000L;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd 'at' HH:mm:ss");
		String out = sdf.format(new Date(epoch)) + " (" + commit.getName()
				+ ")";
		return out;
	}

}
