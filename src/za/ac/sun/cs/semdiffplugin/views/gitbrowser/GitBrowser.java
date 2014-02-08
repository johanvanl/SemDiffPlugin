package za.ac.sun.cs.semdiffplugin.views.gitbrowser;

import org.eclipse.core.runtime.preferences.InstanceScope;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.ViewPart;
import org.eclipse.ui.preferences.ScopedPreferenceStore;

import za.ac.sun.cs.semdiffplugin.Constants;
import za.ac.sun.cs.semdiffplugin.tree.TreeCommit;
import za.ac.sun.cs.semdiffplugin.tree.TreeFile;
import za.ac.sun.cs.semdiffplugin.tree.TreeLabelProvider;
import za.ac.sun.cs.semdiffplugin.tree.TreeRepo;

public class GitBrowser extends ViewPart {

	private TreeViewer viewer = null;
	private ViewContentProvider vcp = null;

	public GitBrowser() {
	}

	/**
	 * This is a callback that will allow us to create the viewer and initialize
	 * it.
	 */
	public void createPartControl(Composite parent) {
		viewer = new TreeViewer(parent, SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL);
		// drillDownAdapter = new DrillDownAdapter(viewer);
		vcp = new ViewContentProvider(this);
		viewer.setContentProvider(vcp);
		viewer.setLabelProvider(new TreeLabelProvider());
		viewer.setInput(getViewSite());

		// Create the help context id for the viewer's control
		PlatformUI.getWorkbench().getHelpSystem()
				.setHelp(viewer.getControl(), "semdiffPlugin.view");

		hookContextMenu();

		viewer.addDoubleClickListener(new IDoubleClickListener() {
			public void doubleClick(DoubleClickEvent event) {
				DoubleClickOnFileAction action = new DoubleClickOnFileAction(
						GitBrowser.this);
				action.run();
			}
		});

		ScopedPreferenceStore store = new ScopedPreferenceStore(
				InstanceScope.INSTANCE, Constants.PLUGIN_ID);
		store.addPropertyChangeListener(new IPropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent event) {
				// Can do more specific things for which preference changed.
				// if (event.getProperty() == PreferenceConstants.DEPTH) {}
				getViewContentProvider().fullRefresh();
				getTreeViewer().refresh(true);
			}
		});

		contributeToActionBars();
	}

	private void hookContextMenu() {
		MenuManager menuMgr = new MenuManager("#PopupMenu");
		menuMgr.setRemoveAllWhenShown(false);
		menuMgr.addMenuListener(new IMenuListener() {
			public void menuAboutToShow(IMenuManager manager) {

				if (viewer.getSelection().isEmpty()) {
					return;
				}

				if (viewer.getSelection() instanceof IStructuredSelection) {
					IStructuredSelection selection = (IStructuredSelection) viewer
							.getSelection();
					Object object = (Object) selection.getFirstElement();

					if (object instanceof TreeRepo) {
						TreeRepo repo = (TreeRepo) object;
						manager.add(new DeleteAction(GitBrowser.this, repo));
					}

					if (object instanceof TreeCommit) {
						TreeCommit commit = (TreeCommit) object;
						manager.add(new AddViewCommitMessageAction(
								GitBrowser.this, commit));
					}

					if (object instanceof TreeFile) {
						TreeFile file = (TreeFile) object;
						manager.add(new DiffAgainstAction(GitBrowser.this, file));
					}

				}

				GitBrowser.this.fillContextMenu(manager);
			}
		});
		Menu menu = menuMgr.createContextMenu(viewer.getControl());
		menuMgr.setRemoveAllWhenShown(true);
		viewer.getControl().setMenu(menu);
		getSite().registerContextMenu(menuMgr, viewer);
	}

	private void contributeToActionBars() {
		IActionBars bars = getViewSite().getActionBars();
		fillLocalPullDown(bars.getMenuManager());
		fillLocalToolBar(bars.getToolBarManager());
	}

	private void fillLocalPullDown(IMenuManager manager) {
	}

	private void fillContextMenu(IMenuManager manager) {
	}

	private void fillLocalToolBar(IToolBarManager manager) {
		manager.add(new AddRefreshAction(this));
		manager.add(new AddRepoAction(this));
	}

	/**
	 * Passing the focus request to the viewer's control.
	 */
	public void setFocus() {
		viewer.getControl().setFocus();
	}

	public TreeViewer getTreeViewer() {
		return this.viewer;
	}

	public ViewContentProvider getViewContentProvider() {
		return this.vcp;
	}

}