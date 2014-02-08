package za.ac.sun.cs.semdiffplugin.views.diffview;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.custom.StyledText;

import za.ac.sun.cs.semdiffplugin.lcs.LcsTextFile;
import za.ac.sun.cs.semdiffplugin.lcs.LongestCommonSubsequence.DiffEntry;
import za.ac.sun.cs.semdiffplugin.views.Utils;

public class TextualDiff {

	private static void visuallyImpairedSideBySide() {
		LcsTextFile lcsObject = new LcsTextFile(DiffViews.original_text,
				DiffViews.revised_text);
		List<DiffEntry<String>> lcs_list = lcsObject.diff();

		StringBuilder original_sb = new StringBuilder();
		StringBuilder revised_sb = new StringBuilder();

		for (DiffEntry<String> entry : lcs_list) {
			if (entry.isRemoved()) {
				original_sb.append("<\t").append(entry.getValue()).append("\n");
				continue;
			}

			if (entry.isAdded()) {
				revised_sb.append(">\t").append(entry.getValue()).append("\n");
				continue;
			}

			original_sb.append("\t").append(entry.getValue()).append("\n");
			revised_sb.append("\t").append(entry.getValue()).append("\n");
		}

		DiffViews.original_document.set(original_sb.toString());
		DiffViews.revised_document.set(revised_sb.toString());
	}

	protected static void sideBySideTextualDiff() {

		if (Utils.isUserVisuallyImpaired()) {
			visuallyImpairedSideBySide();
			return;
		}

		LcsTextFile lcsObject = new LcsTextFile(DiffViews.original_text,
				DiffViews.revised_text);
		List<DiffEntry<String>> lcs_list = lcsObject.diff();

		StringBuilder original_sb = new StringBuilder();
		StringBuilder revised_sb = new StringBuilder();

		List<Integer> added = new ArrayList<Integer>();
		List<Integer> deleted = new ArrayList<Integer>();

		int added_index = 0;
		int deleted_index = 0;
		for (DiffEntry<String> entry : lcs_list) {

			if (entry.isAdded()) {
				added.add(added_index);
				added_index++;

				revised_sb.append(entry.getValue()).append("\n");
				continue;
			}

			if (entry.isRemoved()) {
				deleted.add(deleted_index);
				deleted_index++;

				original_sb.append(entry.getValue()).append("\n");
				continue;
			}

			original_sb.append(entry.getValue()).append("\n");
			revised_sb.append(entry.getValue()).append("\n");

			added_index++;
			deleted_index++;
		}

		DiffViews.original_document.set(original_sb.toString());
		DiffViews.revised_document.set(revised_sb.toString());

		StyledText st = DiffViews.svRevised.getTextWidget();
		for (int line : added) {
			st.setLineBackground(line, 1, DiffViews.added_color);
		}

		st = DiffViews.svOriginal.getTextWidget();
		for (int line : deleted) {
			st.setLineBackground(line, 1, DiffViews.deleted_color);
		}

	}

}
