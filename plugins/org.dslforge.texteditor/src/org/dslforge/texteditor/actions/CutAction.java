/**
 * <copyright>
 *
 * Copyright (c) 2015 PlugBee. All rights reserved.
 * 
 * This program and the accompanying materials are made available 
 * under the terms of the Eclipse Public License v1.0 which 
 * accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Amine Lajmi - Initial API and implementation
 *
 * </copyright>
 */
package org.dslforge.texteditor.actions;

import org.dslforge.styledtext.BasicText;
import org.dslforge.styledtext.ITextSelection;
import org.dslforge.styledtext.TextSelection;
import org.dslforge.styledtext.TextSelectionListenerAction;
import org.dslforge.texteditor.BasicTextEditor;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.actions.ActionFactory;

public class CutAction extends TextSelectionListenerAction {

	private static final long serialVersionUID = 1L;
	private BasicTextEditor activeEditor;

	public CutAction(String text) {
		super(text);
	}
	
	@Override
	public String getId() {
		return ActionFactory.CUT.getId();
	}
	
	@Override
	public void run() {
		if(activeEditor!=null) {
			BasicText widget = activeEditor.getViewer().getTextWidget();
			TextSelection selection = (TextSelection) widget.getSelection();
			activeEditor.performCut(selection);
		}
	}
	
	@Override
	public boolean updateSelection(ITextSelection selection) {
		return super.updateSelection(selection);
	}

	public void setActiveWorkbenchPart(IEditorPart activeEditor) {
		if (activeEditor instanceof BasicTextEditor)
			this.activeEditor = (BasicTextEditor) activeEditor;
	}
}
