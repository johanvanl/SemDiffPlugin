package za.ac.sun.cs.semdiffplugin.views.diffview;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.eclipse.jface.text.source.SourceViewer;
import org.eclipse.swt.custom.StyleRange;
import org.eclipse.swt.custom.StyledText;

import za.ac.sun.cs.semdiff.ast.DiffCompilationUnit;
import za.ac.sun.cs.semdiff.ast.DiffNode;
import za.ac.sun.cs.semdiff.ast.body.DiffBodyDeclaration;
import za.ac.sun.cs.semdiff.utils.GetDifferenceNodes;

public class StyleSemDiff {

	protected static void styleSemDiff(SourceViewer sv,
			DiffCompilationUnit AST, int offset) {

		StyledText st = sv.getTextWidget();
		List<StyleRange> styles_list = new ArrayList<StyleRange>();

		GetDifferenceNodes diff = new GetDifferenceNodes(AST);

		List<DiffNode> added = diff.getAddedNodes();
		for (DiffNode node : added) {
			styles_list.add(new StyleRange(node.getStart() + offset, node
					.getLength(), null, DiffViews.added_color));
		}

		List<DiffNode> deleted = diff.getDeletedNodes();
		for (DiffNode node : deleted) {
			styles_list.add(new StyleRange(node.getStart() + offset, node
					.getLength(), null, DiffViews.deleted_color));
		}

		List<DiffNode> renamed = diff.getRenamedNodes();
		for (DiffNode node : renamed) {
			if (node instanceof DiffBodyDeclaration) {
				DiffBodyDeclaration body = (DiffBodyDeclaration) node;
				styles_list.add(new StyleRange(body.getIdentifier().getStart()
						+ offset, body.getIdentifier().getLength(), null,
						DiffViews.rename_color));
				continue;
			}
			styles_list.add(new StyleRange(node.getStart() + offset, node
					.getLength(), null, DiffViews.rename_color));
		}

		List<DiffNode> moved = diff.getMovedNodes();
		for (DiffNode node : moved) {
			if (node instanceof DiffBodyDeclaration) {
				DiffBodyDeclaration body = (DiffBodyDeclaration) node;
				styles_list.add(new StyleRange(body.getIdentifier().getStart()
						+ offset, body.getIdentifier().getLength(), null,
						DiffViews.move_color));
				continue;
			}
			styles_list.add(new StyleRange(node.getStart() + offset, node
					.getLength(), null, DiffViews.move_color));
		}

		if (styles_list.size() == 0) {
			return;
		}

		Collections.sort(styles_list, new Comparator<StyleRange>() {
			@Override
			public int compare(StyleRange sr0, StyleRange sr1) {
				int comp = Integer.compare(sr0.start, sr1.start);
				if (comp != 0) {
					return comp;
				}
				// If two starts are equal we want the biggest one first so that
				// the smaller ones can be removed, by the precautions to
				// follow.
				return -1 * Integer.compare(sr0.length, sr1.length);
			}
		});

		// Just some precautions to prevent exceptions

		// Short circuit to prevent errors
		while (styles_list.size() > 0 && styles_list.get(0).start < 0) {
			styles_list.remove(0);
		}

		int index = 0;
		while (index < styles_list.size() - 1) {
			StyleRange sr = styles_list.get(index);
			if (styles_list.get(index + 1).start < sr.start + sr.length) {
				styles_list.remove(index + 1);
			} else {
				index++;
			}
		}

		StyleRange[] styles = styles_list.toArray(new StyleRange[styles_list
				.size()]);
		st.setStyleRanges(styles);
	}

}
