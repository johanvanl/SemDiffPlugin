package za.ac.sun.cs.semdiffplugin.preferences;

import org.eclipse.core.runtime.preferences.InstanceScope;
import org.eclipse.jface.preference.BooleanFieldEditor;
import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.jface.preference.IntegerFieldEditor;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;
import org.eclipse.ui.preferences.ScopedPreferenceStore;

import za.ac.sun.cs.semdiffplugin.Constants;

/**
 * This class represents a preference page that is contributed to the
 * Preferences dialog. By subclassing <samp>FieldEditorPreferencePage</samp>, we
 * can use the field support built into JFace that allows us to create a page
 * that is small and knows how to save, restore and apply itself.
 * <p>
 * This page is used to modify preferences only. They are stored in the
 * preference store that belongs to the main plug-in class. That way,
 * preferences can be accessed directly via the preference store.
 */

public class SemDiffPreferencePage extends FieldEditorPreferencePage implements
		IWorkbenchPreferencePage {

	public SemDiffPreferencePage() {
		super(GRID);
	}

	/**
	 * Creates the field editors. Field editors are abstractions of the common
	 * GUI blocks needed to manipulate various types of preferences. Each field
	 * editor knows how to save and restore itself.
	 */
	public void createFieldEditors() {
		addField(new IntegerFieldEditor(PreferenceConstants.DEPTH,
				"depth of the commits :", getFieldEditorParent()));
		addField(new BooleanFieldEditor(
				PreferenceConstants.VISUALLY_IMPAIRED_USER,
				"visually impaired user", getFieldEditorParent()));
	}

	public void init(IWorkbench workbench) {
		ScopedPreferenceStore store = new ScopedPreferenceStore(
				InstanceScope.INSTANCE, Constants.PLUGIN_ID);
		setPreferenceStore(store);
		setDescription("SemDiffPlugin preferences");

		// Set the defaults to the preference store
		store = new ScopedPreferenceStore(InstanceScope.INSTANCE,
				Constants.PLUGIN_ID);
		store.setDefault(PreferenceConstants.DEPTH,
				PreferenceConstants.DEPTH_DEFAULT);
		store.setDefault(PreferenceConstants.VISUALLY_IMPAIRED_USER,
				PreferenceConstants.VISUALLY_IMPAIRED_USER_DEFAULT);
	}

}