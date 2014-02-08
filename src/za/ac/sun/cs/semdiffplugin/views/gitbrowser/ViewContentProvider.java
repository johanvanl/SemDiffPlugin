package za.ac.sun.cs.semdiffplugin.views.gitbrowser;

import java.util.List;

import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.ui.part.ViewPart;

import za.ac.sun.cs.semdiffplugin.tree.TreeFile;
import za.ac.sun.cs.semdiffplugin.tree.TreeFolder;
import za.ac.sun.cs.semdiffplugin.tree.TreeRepo;
import za.ac.sun.cs.semdiffplugin.views.Utils;

/*
 * The content provider class is responsible for providing objects to the
 * view. It can wrap existing objects in adapters or simply return objects
 * as-is. These objects may be sensitive to the current input of the view,
 * or ignore it and always show the same content (like Task List, for
 * example).
 */
public class ViewContentProvider implements IStructuredContentProvider,
		ITreeContentProvider {

	private TreeFolder invisibleRoot = null;
	private ViewPart view = null;

	public ViewContentProvider(ViewPart view) {
		this.view = view;

		this.invisibleRoot = new TreeFolder("");
		initialize();
	}

	public void inputChanged(Viewer v, Object oldInput, Object newInput) {
	}

	public void dispose() {
	}

	public Object[] getElements(Object parent) {
		// if (parent.equals(this.view.getViewSite())) { XXX
		return getChildren(this.invisibleRoot);
		// } XXX
		// return getChildren(parent); XXX
	}

	public Object getParent(Object child) {
		if (child instanceof TreeFile) {
			return ((TreeFile) child).getParent();
		}
		return null;
	}

	public Object[] getChildren(Object parent) {
		if (parent instanceof TreeFolder) {
			return ((TreeFolder) parent).getChildren();
		}
		return new Object[0];
	}

	public boolean hasChildren(Object parent) {
		if (parent instanceof TreeFolder) {
			return ((TreeFolder) parent).hasFiles()
					|| ((TreeFolder) parent).hasFolders();
		}
		return false;
	}

	public void addRepoToTree(String repoLocation) {
		TreeRepo folder = null;
		try {
			folder = GitUtils.refresh(repoLocation);
		} catch (Exception e) {
		}
		this.invisibleRoot.addFolder(folder);
	}
	
	public void removeRepoFromTree(TreeFolder repoFolder){
		this.invisibleRoot.removeFolder(repoFolder);
	}
	
	public void fullRefresh(){
		this.invisibleRoot = new TreeFolder("");
		initialize();
	}

	private void initialize() {
		List<String> repositories = Utils.getListOfRepositoryLocations();
		for (String repo : repositories) {
			addRepoToTree(repo);
		}
	}

}
