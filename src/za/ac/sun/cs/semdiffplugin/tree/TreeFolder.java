package za.ac.sun.cs.semdiffplugin.tree;

import java.util.ArrayList;
import java.util.List;

import za.ac.sun.cs.semdiffplugin.views.Utils;

public class TreeFolder extends TreeObject {

	private TreeFolder parent = null;
	private String folder_name = null;
	private List<TreeFile> files = null;
	private List<TreeFolder> folders = null;

	public TreeFolder(String name) {
		super();
		this.folder_name = name;
		this.files = new ArrayList<TreeFile>();
		this.folders = new ArrayList<TreeFolder>();
	}

	public String getName() {
		return this.folder_name;
	}

	public void addFile(TreeFile file) {
		this.files.add(file);
		file.setParent(this);
	}

	public void addFolder(TreeFolder folder) {
		this.folders.add(folder);
		folder.setParent(this);
	}

	public void removeFolder(TreeFolder folder) {
		this.folders.remove(folder);
	}

	public void setParent(TreeFolder parent) {
		this.parent = parent;
	}

	public boolean hasFiles() {
		return !this.files.isEmpty();
	}

	public boolean hasFolders() {
		return !this.folders.isEmpty();
	}

	public List<TreeFile> getFiles() {
		return this.files;
	}

	public List<TreeFolder> getFolders() {
		return this.folders;
	}

	public TreeFolder getParent() {
		return this.parent;
	}

	public TreeObject[] getChildren() {
		List<TreeObject> children = new ArrayList<TreeObject>();
		children.addAll(this.folders);
		children.addAll(this.files);
		return (TreeObject[]) children.toArray(new TreeObject[children.size()]);
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof TreeFolder) {
			TreeFolder folder = (TreeFolder) obj;
			return this.getName().equals(folder.getName());
		}
		return false;
	}

	@Override
	public String toString() {
		String out = getName();

		if (!Utils.isUserVisuallyImpaired()) {
			return out;
		}

		if (Utils.didFolderChange(this)) {
			out += " [CHANGED]";
		}
		return out;
	}

}
