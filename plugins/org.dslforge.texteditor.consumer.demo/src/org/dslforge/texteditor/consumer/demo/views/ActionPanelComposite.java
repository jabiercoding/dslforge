package org.dslforge.texteditor.consumer.demo.views;

import java.io.File;

import org.dslforge.xtext.common.actions.BasicGenerateAction;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PlatformUI;

public class ActionPanelComposite extends Composite {

	private static final long serialVersionUID = 1L;
	private ConsumerComposite consumerComposite;

	public ActionPanelComposite(Composite parent, int style) {
		super(parent, style);
		GridLayout layout = new GridLayout();
		layout.numColumns = 1;
		layout.marginTop = 10;
		layout.marginBottom = 10;
		layout.marginLeft = 10;
		layout.verticalSpacing = 40;
		setLayout(layout);
		setLayoutData(new GridData(GridData.FILL_VERTICAL));
		// Create buttons
		Button refreshButton = new Button(this, SWT.PUSH);
		refreshButton.setText("Update");
		refreshButton.addSelectionListener(new SelectionListener(){
			private static final long serialVersionUID = 1L;

			@Override
			public void widgetSelected(SelectionEvent e) {
				
				// Save
				IWorkbenchPage activePage = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
				IEditorPart activeEditor = activePage.getActiveEditor();
				activeEditor.doSave(new NullProgressMonitor());
				
				// Generate
				BasicGenerateAction generateAction = new BasicGenerateAction();
				generateAction.doGenerate(new File(ConsumerView.getHiddenModelPath()), new File(ConsumerView.getHiddenOutputPath()));
				
				// Refresh
				consumerComposite.consume();
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				
			}
		});
	}

	public void register(ConsumerComposite consumerComposite) {
		this.consumerComposite = consumerComposite;
	}

}
