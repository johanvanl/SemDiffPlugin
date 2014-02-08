package za.ac.sun.cs.semdiffplugin.tree;

import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;

import za.ac.sun.cs.semdiffplugin.views.FileType;
import za.ac.sun.cs.semdiffplugin.views.Utils;

public class TreeLabelProvider extends LabelProvider {

	public String getText(Object obj) {
		return obj.toString();
	}

	public Image getImage(Object obj) {

		if (obj instanceof TreeRepo) {
			return Utils.getImage("icons/repo.gif");
		}

		if (obj instanceof TreeCommit) {
			return Utils.getImage("icons/commit.gif");
		}

		if (obj instanceof TreeFolder) {
			TreeFolder treeFolder = (TreeFolder) obj;
			if (Utils.didFolderChange(treeFolder)) {
				return Utils.getImage("icons/folder_changed.gif");
			}
			return Utils.getImage("icons/folder.gif");
		}

		TreeFile file = (TreeFile) obj;
		FileType ft = Utils.getFileType(file);

		switch (ft) {
		case BINARY:
			return Utils.getImage("icons/binary.gif");
		case BINARY_ADDED:
			return Utils.getImage("icons/binary_added.gif");
		case BINARY_CHANGED:
			return Utils.getImage("icons/binary_changed.gif");
		case JAVA:
			return Utils.getImage("icons/java.gif");
		case JAVA_ADDED:
			return Utils.getImage("icons/java_added.gif");
		case JAVA_CHANGED:
			return Utils.getImage("icons/java_changed.gif");
		case TEXT:
			return Utils.getImage("icons/text.gif");
		case TEXT_ADDED:
			return Utils.getImage("icons/text_added.gif");
		case TEXT_CHANGED:
			return Utils.getImage("icons/text_changed.gif");
		}

		return Utils.getImage("icons/text.gif");
	}

}
