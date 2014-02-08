package za.ac.sun.cs.semdiffplugin.views.diffview;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.jface.text.Document;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.source.CompositeRuler;
import org.eclipse.jface.text.source.LineNumberRulerColumn;
import org.eclipse.jface.text.source.SourceViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.part.ViewPart;

import za.ac.sun.cs.semdiffplugin.views.Utils;

public class DiffViews extends ViewPart {

	public static Color added_color = new Color(null, 115, 230, 115);
	public static Color deleted_color = new Color(null, 255, 71, 71);
	public static Color rename_color = new Color(null, 102, 204, 255);
	public static Color move_color = new Color(null, 255, 89, 172);

	private String fn = "";

	protected static String original_text = "";
	protected static SourceViewer svOriginal = null;
	protected static IDocument original_document = null;
	protected static Map<Point, Point> original_map = new HashMap<Point, Point>();

	protected static String revised_text = "";
	protected static SourceViewer svRevised = null;
	protected static IDocument revised_document = null;
	protected static Map<Point, Point> revised_map = new HashMap<Point, Point>();

	public DiffViews() {
	}

	@Override
	public void createPartControl(Composite parent) {
		original_document = new Document();
		revised_document = new Document();

		original_document.set("");
		revised_document.set("");

		CompositeRuler original_ruler = new CompositeRuler();
		original_ruler.addDecorator(0, new LineNumberRulerColumn());
		svOriginal = new SourceViewer(parent, original_ruler, SWT.BORDER
				| SWT.V_SCROLL | SWT.H_SCROLL);
		svOriginal.setEditable(false);
		svOriginal.setDocument(original_document);

		CompositeRuler revised_ruler = new CompositeRuler();
		revised_ruler.addDecorator(0, new LineNumberRulerColumn());
		svRevised = new SourceViewer(parent, revised_ruler, SWT.BORDER
				| SWT.V_SCROLL | SWT.H_SCROLL);
		svRevised.setEditable(false);
		svRevised.setDocument(revised_document);

		svOriginal.getTextWidget().addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDoubleClick(MouseEvent e) {
				Point p = original_map.get(svOriginal.getSelectedRange());
				if (p != null) {
					svOriginal.setSelectedRange(p.x, p.y);
					svOriginal.revealRange(p.x, p.y);
				}
			}
		});

		svRevised.getTextWidget().addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDoubleClick(MouseEvent e) {
				Point p = revised_map.get(svRevised.getSelectedRange());
				if (p != null) {
					svRevised.setSelectedRange(p.x, p.y);
					svRevised.revealRange(p.x, p.y);
				}
			}
		});

	}

	public void setFileName(String fn) {
		this.fn = fn;
	}

	public void setOriginaltext(String text) {
		original_text = text;
	}

	public void setRevisedtext(String text) {
		revised_text = text;
	}

	public void diff() {
		original_map.clear();
		revised_map.clear();

		if (!Utils.isText(original_text) || !Utils.isText(revised_text)) {
			original_document.set("Binary files!!!");
			revised_document.set("");
			return;
		}

		if (revised_text.equals("")) {
			return;
		}

		if (original_text.equals("")) {
			original_document.set("");
			revised_document.set(revised_text);
			return;
		}

		if (fn.endsWith(".java")) {
			SemanticDiff.semanticDiff();
		} else {
			TextualDiff.sideBySideTextualDiff();
		}
	}

	@Override
	public void setFocus() {
	}

}
