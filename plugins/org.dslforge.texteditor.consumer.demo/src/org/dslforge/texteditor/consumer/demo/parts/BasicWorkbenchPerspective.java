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
package org.dslforge.texteditor.consumer.demo.parts;

import org.dslforge.texteditor.consumer.demo.IWorkbenchConstants;
import org.dslforge.texteditor.consumer.demo.views.ConsumerView;
import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.IPerspectiveFactory;

/**
 * Configures the perspective layout. This class is contributed 
 * through the plugin.xml.
 */
public class BasicWorkbenchPerspective implements IPerspectiveFactory {
	
	public void createInitialLayout(IPageLayout layout) {
		String editorArea = layout.getEditorArea();
		layout.setEditorAreaVisible(true);
		layout.addPerspectiveShortcut(IWorkbenchConstants.ID_PERSPECTIVE);
		layout.setFixed(true);
		
		layout.addStandaloneView(ConsumerView.ID, false, IPageLayout.RIGHT, 0.38f, editorArea);
		layout.getViewLayout(ConsumerView.ID).setCloseable(false);
	}
}