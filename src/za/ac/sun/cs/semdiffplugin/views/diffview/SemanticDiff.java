package za.ac.sun.cs.semdiffplugin.views.diffview;

import org.eclipse.swt.graphics.Point;

import za.ac.sun.cs.semdiff.SemDiff;
import za.ac.sun.cs.semdiff.utils.PrintDiff;
import za.ac.sun.cs.semdiff.utils.Summary;
import za.ac.sun.cs.semdiffplugin.views.Utils;

public class SemanticDiff {

	protected static void visuallyImpairedSemanticDiff() {
		SemDiff sd = new SemDiff(DiffViews.original_text,
				DiffViews.revised_text);

		String or = sd.getOriginalSummary().toString();
		or += PrintDiff.printDiff(sd.getOriginalAST());
		DiffViews.original_document.set(or);

		String re = sd.getRevisedSummary().toString();
		re += PrintDiff.printDiff(sd.getRevisedAST());
		DiffViews.revised_document.set(re);
	}

	protected static void semanticDiff() {

		// TODO something with the messages/errors

		if (Utils.isUserVisuallyImpaired()) {
			visuallyImpairedSemanticDiff();
			return;
		}

		SemDiff sd = new SemDiff(DiffViews.original_text,
				DiffViews.revised_text);

		Summary s = sd.getOriginalSummary();
		DiffViews.original_map = s.getMap();
		String or = s.toString();
		int offset = s.toString().length();
		or += sd.getOriginalAST().getSource();
		DiffViews.original_document.set(or);
		StyleSemDiff.styleSemDiff(DiffViews.svOriginal, sd.getOriginalAST(),
				offset);

		for (Point p : DiffViews.original_map.keySet()) {
			Point toChange = DiffViews.original_map.get(p);
			toChange.x += offset;
		}

		s = sd.getRevisedSummary();
		DiffViews.revised_map = s.getMap();
		String re = s.toString();
		offset = s.toString().length();
		re += sd.getRevisedAST().getSource();
		DiffViews.revised_document.set(re);
		StyleSemDiff.styleSemDiff(DiffViews.svRevised, sd.getRevisedAST(),
				offset);

		for (Point p : DiffViews.revised_map.keySet()) {
			Point toChange = DiffViews.revised_map.get(p);
			toChange.x += offset;
		}
	}

}
