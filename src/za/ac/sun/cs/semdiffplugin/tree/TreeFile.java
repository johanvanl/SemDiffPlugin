package za.ac.sun.cs.semdiffplugin.tree;

import za.ac.sun.cs.semdiffplugin.views.FileType;
import za.ac.sun.cs.semdiffplugin.views.Utils;

public class TreeFile extends TreeObject {

	private TreeFolder parent = null;
	private String filename = null;

	public TreeFile(String filename) {
		super();
		this.filename = filename;
	}

	public void setParent(TreeFolder parent) {
		this.parent = parent;
	}

	public TreeFolder getParent() {
		return parent;
	}

	public String getFilename() {
		return this.filename;
	}

	public String toString() {
		String out = "";

		if (this.filename.contains("/")) {
			String[] split = this.filename.split("/");
			out = split[split.length - 1];
		} else {
			out = this.filename;
		}

		if (!Utils.isUserVisuallyImpaired()) {
			return out;
		}

		out += " ";

		FileType ft = Utils.getFileType(this);
		switch (ft) {
		case BINARY_ADDED:
		case JAVA_ADDED:
		case TEXT_ADDED:
			out += "[ADDED]";
			break;
		case BINARY_CHANGED:
		case JAVA_CHANGED:
		case TEXT_CHANGED:
			out += "[CHANGED]";
			break;
		default:
			break;
		}

		return out;
	}

}
