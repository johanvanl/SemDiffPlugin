package za.ac.sun.cs.semdiffplugin.views.gitbrowser;

import java.util.List;

import org.eclipse.core.runtime.preferences.InstanceScope;
import org.eclipse.jgit.internal.storage.file.FileRepository;
import org.eclipse.jgit.revwalk.RevCommit;
import org.osgi.service.prefs.Preferences;

import za.ac.sun.cs.semdiffplugin.Constants;
import za.ac.sun.cs.semdiffplugin.preferences.PreferenceConstants;
import za.ac.sun.cs.semdiffplugin.tree.TreeCommit;
import za.ac.sun.cs.semdiffplugin.tree.TreeFile;
import za.ac.sun.cs.semdiffplugin.tree.TreeFolder;
import za.ac.sun.cs.semdiffplugin.tree.TreeRepo;

import com.gitblit.models.PathModel;
import com.gitblit.utils.JGitUtils;

public class GitUtils {

	private static int getDepth() {
		Preferences preferences = InstanceScope.INSTANCE
				.getNode(Constants.PLUGIN_ID);
		int depth = preferences.getInt(PreferenceConstants.DEPTH,
				PreferenceConstants.DEPTH_DEFAULT);
		return depth;
	}

	public static TreeRepo refresh(String gitLocation) throws Exception {
		FileRepository fr = new FileRepository(gitLocation + "/.git");
		TreeRepo root = new TreeRepo(fr);

		List<RevCommit> commits = JGitUtils.getRevLog(fr, getDepth());

		for (RevCommit commit : commits) {
			TreeCommit treecommit = new TreeCommit(commit);

			List<PathModel> files = JGitUtils.getFilesInCommit(fr, commit);
			for (PathModel file : files) {
				if (file.name.contains("/")) {
					String[] split = file.name.split("/");
					StringBuilder folder_name = new StringBuilder();
					String prefix = "";
					for (int i = 0; i < split.length - 1; i++) {
						folder_name.append(prefix);
						prefix = "/";
						folder_name.append(split[i]);
					}
					boolean folderExists = false;
					List<TreeFolder> folders = treecommit.getFolders();
					for (TreeFolder folder : folders) {
						if (folder.getName().equals(folder_name.toString())) {
							folder.addFile(new TreeFile(file.name));
							folderExists = true;
							break;
						}
					}
					if (!folderExists) {
						TreeFolder treefolder = new TreeFolder(
								folder_name.toString());
						treefolder.addFile(new TreeFile(file.name));
						treecommit.addFolder(treefolder);
					}
				} else {
					treecommit.addFile(new TreeFile(file.name));
				}
			}

			root.addFolder(treecommit);
		}

		return root;
	}

}
