package za.ac.sun.cs.semdiffplugin.views;

import java.lang.reflect.Type;
import java.util.List;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.preferences.InstanceScope;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Shell;
import org.osgi.framework.Bundle;
import org.osgi.service.prefs.BackingStoreException;
import org.osgi.service.prefs.Preferences;

import za.ac.sun.cs.semdiffplugin.Constants;
import za.ac.sun.cs.semdiffplugin.preferences.PreferenceConstants;
import za.ac.sun.cs.semdiffplugin.tree.TreeCommit;
import za.ac.sun.cs.semdiffplugin.tree.TreeFile;
import za.ac.sun.cs.semdiffplugin.tree.TreeFolder;
import za.ac.sun.cs.semdiffplugin.tree.TreeRepo;

import com.gitblit.utils.JGitUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class Utils {

	private Utils() {
	}

	public static List<String> getListOfRepositoryLocations() {
		Preferences preferences = InstanceScope.INSTANCE
				.getNode(Constants.PLUGIN_ID);
		String string_list = preferences.get(
				PreferenceConstants.REPOSITORY_LIST,
				PreferenceConstants.REPOSITORY_LIST_DEFAULT);

		Gson gson = new Gson();
		Type listOfString = new TypeToken<List<String>>() {
		}.getType();
		List<String> list = gson.fromJson(string_list, listOfString);
		return list;
	}

	public static void saveListOfRepositoryLocations(List<String> locations) {
		Gson gson = new Gson();
		Type listOfString = new TypeToken<List<String>>() {
		}.getType();
		String string = gson.toJson(locations, listOfString);

		Preferences preferences = InstanceScope.INSTANCE
				.getNode(Constants.PLUGIN_ID);
		preferences.put(PreferenceConstants.REPOSITORY_LIST, string);
		try {
			preferences.flush();
		} catch (BackingStoreException e) {
		}
	}

	public static void removeRepository(String location) {
		List<String> locations = getListOfRepositoryLocations();
		locations.remove(location);
		saveListOfRepositoryLocations(locations);
	}

	public static void addNewRepository(String location) {
		List<String> repositories = getListOfRepositoryLocations();
		repositories.add(location);
		saveListOfRepositoryLocations(repositories);
	}

	public static boolean isRepoAlreadyPresent(String location) {
		List<String> repositories = getListOfRepositoryLocations();
		return repositories.contains(location);
	}

	public static void showMessage(Shell shell, String title, String message) {
		MessageDialog.openInformation(shell, title, message);
	}

	public static TreeCommit getTreeCommitFromTreeFile(TreeFile file) {
		TreeCommit commit = null;
		if (file.getParent() instanceof TreeCommit) {
			commit = (TreeCommit) file.getParent();
		} else {
			commit = (TreeCommit) file.getParent().getParent();
		}
		return commit;
	}

	public static boolean isText(String text) {

		if (text == null) {
			return false;
		}
		
		if (text.equals("")) {
			return true;
		}

		// delete all readable text
		String rep = text.replaceAll(
				"[a-zA-Z0-9ßöäü\\.\\*!\"§\\$\\%&/()=\\?@~'#:,;\\"
						+ "+><\\|\\[\\]\\{\\}\\^°²³\\\\ \\n\\r\\t_\\-`´âêîô"
						+ "ÂÊÔÎáéíóàèìòÁÉÍÓÀÈÌÒ©‰¢£¥€±¿»«¼½¾™ª]", "");

		// ratio of text signs in the text
		double d = (double) (text.length() - rep.length())
				/ (double) (text.length());

		return d > 0.95;
	}

	public static boolean isUserVisuallyImpaired() {
		Preferences preferences = InstanceScope.INSTANCE
				.getNode(Constants.PLUGIN_ID);
		boolean visually_impaired = preferences.getBoolean(
				PreferenceConstants.VISUALLY_IMPAIRED_USER,
				PreferenceConstants.VISUALLY_IMPAIRED_USER_DEFAULT);
		return visually_impaired;
	}

	/**
	 * Returns the String content of this file.
	 * 
	 * @param treeRepo
	 * @param treeCommit
	 * @param treeFile
	 * @return
	 */
	public static String getText(TreeRepo treeRepo, TreeCommit treeCommit,
			TreeFile treeFile) {
		String text = "";
		try {
			text = JGitUtils.getStringContent(treeRepo.getRepo(),
					treeCommit.getCommit(), treeFile.getFilename());
		} catch (Exception e) {
			text = "";
		}
		return text;
	}

	/**
	 * Returns the String content of this file. Returns null if file doesn't
	 * exist.
	 * 
	 * @param treeRepo
	 * @param treeCommit
	 * @param treeFile
	 * @return
	 */
	public static String getParentText(TreeRepo treeRepo,
			TreeCommit treeCommit, TreeFile treeFile) {

		String text = null;
		if (treeCommit.getCommit().getParentCount() > 0) {
			RevCommit parent = treeCommit.getCommit().getParent(0);
			try {
				text = JGitUtils.getStringContent(treeRepo.getRepo(), parent,
						treeFile.getFilename());
			} catch (Exception e) {
			}
		}
		return text;
	}

	public static ImageDescriptor getImageDescriptor(String fn) {
		Bundle bundle = Platform.getBundle(Constants.PLUGIN_ID);
		ImageDescriptor id = ImageDescriptor.createFromURL(FileLocator.find(
				bundle, new Path(fn), null));
		return id;
	}

	public static Image getImage(String fn) {
		Bundle bundle = Platform.getBundle(Constants.PLUGIN_ID);
		ImageDescriptor id = ImageDescriptor.createFromURL(FileLocator.find(
				bundle, new Path(fn), null));
		return id.createImage();
	}

	public static FileType getFileType(TreeFile treeFile) {
		TreeCommit treeCommit = Utils.getTreeCommitFromTreeFile(treeFile);
		TreeRepo treeRepo = (TreeRepo) treeCommit.getParent();

		String revised_text = getText(treeRepo, treeCommit, treeFile);
		String original_text = getParentText(treeRepo, treeCommit, treeFile);

		// it is binary
		if (!isText(revised_text)) {
			if (original_text == null) {
				return FileType.BINARY_ADDED;
			}
			if (!original_text.equals(revised_text)) {
				return FileType.BINARY_CHANGED;
			}
			return FileType.BINARY;
		}

		// Java file
		if (treeFile.getFilename().endsWith(".java")) {
			if (original_text == null) {
				return FileType.JAVA_ADDED;
			}
			if (!original_text.equals(revised_text)) {
				return FileType.JAVA_CHANGED;
			}
			return FileType.JAVA;
		}

		if (original_text == null) {
			return FileType.TEXT_ADDED;
		}

		if (!original_text.equals(revised_text)) {
			return FileType.TEXT_CHANGED;
		}

		return FileType.TEXT;
	}
	
	public static boolean didFolderChange(TreeFolder treeFolder) {
		List<TreeFile> files = treeFolder.getFiles();
		for (TreeFile file : files) {
			FileType ft = Utils.getFileType(file);
			switch (ft) {
			case BINARY_ADDED:
			case BINARY_CHANGED:
			case JAVA_ADDED:
			case JAVA_CHANGED:
			case TEXT_ADDED:
			case TEXT_CHANGED:
				return true;
			default:
				break;
			}
		}
		return false;
	}
	
}
