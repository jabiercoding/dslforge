package org.dslforge.texteditor.consumer.demo.views;

import java.io.File;
import java.io.IOException;

import org.dslforge.workspace.WorkspaceManager;
import org.eclipse.core.runtime.IPath;
import org.eclipse.emf.common.ui.URIEditorInput;
import org.eclipse.emf.edit.ui.util.EditUIUtil;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.rap.rwt.RWT;
import org.eclipse.rap.rwt.client.service.StartupParameters;
import org.eclipse.rap.rwt.service.UISession;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IEditorDescriptor;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.ViewPart;
import org.eclipse.emf.common.util.URI;

public class ConsumerView extends ViewPart {

	public static final String ID = "org.dslforge.texteditor.consumer.demo.view";
	
	private static final String SRC_GEN = "src-gen";
	private static final String MODEL_NAME = "model";
	private static String MODEL_EXTENSION;
	private static final String HIDDEN_PROJECT = "hiddenProject";

	public ConsumerView() {
		// Prepare hidden project and dsl file
		initialize();
		StartupParameters service = RWT.getClient().getService(StartupParameters.class);
		MODEL_EXTENSION = service.getParameter("language");
		if (MODEL_EXTENSION == null) {
			MessageDialog.openError(Display.getCurrent().getActiveShell(), "Error",
					"In the launch configuration, you should define the language extension in the Servlet path. For example /consumer?language=statemachine");
		}
	}

	ConsumerComposite consumerComposite;
	ActionPanelComposite actionPanelComposite;

	@Override
	public void createPartControl(Composite parent) {
		parent.setSize(900, 500);
		Composite composite = new Composite(parent, SWT.FILL);
		GridLayout layout = new GridLayout();
		layout.numColumns = 2;
		layout.marginWidth = 0;
		layout.makeColumnsEqualWidth = false;
		composite.setLayout(layout);
		createActionPanel(composite);
		createConsumerComposite(composite);
		actionPanelComposite.register(consumerComposite);
		openEditor();
	}

	private void createConsumerComposite(Composite composite) {
		consumerComposite = new ConsumerComposite(composite, SWT.NONE);
		consumerComposite.setLayoutData(new GridData(GridData.FILL_BOTH));
	}

	private void createActionPanel(Composite composite) {
		actionPanelComposite = new ActionPanelComposite(composite, SWT.CENTER);
	}

	/**
	 * initializes the workspace for the current session
	 */
	private void initialize() {
		final Display display = Display.getCurrent();
		final Runnable runnable = new Runnable() {
			public void run() {
				final UISession uiSession = RWT.getUISession(display);
				uiSession.exec(new Runnable() {
					public void run() {
						// Workspace root
						final IPath workspaceRoot = WorkspaceManager.INSTANCE.getWorkspaceRootPath();
						IPath stringFolderURI = workspaceRoot.append(HIDDEN_PROJECT);
						// create user project
						final File hiddenProjectFile = stringFolderURI.toFile();
						if (!hiddenProjectFile.exists()) {
							// create model
							try {
								boolean result = hiddenProjectFile.mkdir();
								if (result) {
									final File dslFile = new File(getHiddenModelPath());
									if (!dslFile.exists()) {
										result = dslFile.createNewFile();
										if (result) {
											final File srcgen = new File(hiddenProjectFile, SRC_GEN);
											if (!srcgen.exists()) {
												srcgen.mkdir();
											}
											System.out.println("[INFO] - Workspace is ready");
										}
									}
								}
							} catch (IOException e) {
								e.printStackTrace();
							}
						}
					}
				});
			}
		};
		new Thread(runnable).start();
	}

	@Override
	public void setFocus() {

	}

	private void openEditor() {
		final IWorkbench workbench = PlatformUI.getWorkbench();
		String path = getHiddenModelPath();
		final URI uri = URI.createFileURI(path);
		final IEditorPart openEditor = openEditor(workbench, uri);
		if (openEditor != null) {
			System.out.println("[INFO] - DSL file is open");
		}
	}

	public static IEditorPart openEditor(IWorkbench workbench, URI uri) {
		IWorkbenchWindow workbenchWindow = workbench.getActiveWorkbenchWindow();
		IWorkbenchPage page = workbenchWindow.getActivePage();
		IEditorDescriptor editorDescriptor = EditUIUtil.getDefaultEditor(uri.lastSegment());
		if (editorDescriptor == null) {
			MessageDialog.openError(workbenchWindow.getShell(), "Error",
					"There is no editor registered for the file " + uri.lastSegment());
			return null;
		}
		try {
			return page.openEditor(new URIEditorInput(uri), editorDescriptor.getId());
		} catch (PartInitException exception) {
			MessageDialog.openError(workbenchWindow.getShell(), "Open Editor", exception.getMessage());
			return null;
		}
	}

	public static String getHiddenModelPath() {
		IPath path = WorkspaceManager.INSTANCE.getWorkspaceRootPath().append(HIDDEN_PROJECT)
				.append(MODEL_NAME + "." + MODEL_EXTENSION);
		return path.toString();
	}

	public static String getHiddenOutputPath() {
		IPath path = WorkspaceManager.INSTANCE.getWorkspaceRootPath().append(HIDDEN_PROJECT).append(SRC_GEN);
		return path.toString();
	}

}
