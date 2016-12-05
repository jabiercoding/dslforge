package org.dslforge.texteditor.consumer.demo.views;

import org.eclipse.swt.widgets.Composite;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.layout.FillLayout;

public class ConsumerComposite extends Composite {

	private static final long serialVersionUID = 1L;
	Browser browser;

	public ConsumerComposite(Composite parent, int style) {
		super(parent, style);
		super.setLayout(new FillLayout());
		browser = new Browser(this, SWT.BORDER);
		browser.setText(
				"<html><body>This part is an SWT Browser, edit the textual model and then click on Update to consume the generation result.</body></html>");
	}

	/**
	 * In this example we dump the content of the generation directory.
	 */
	public void consume() {
		StringBuffer sb = new StringBuffer();
		sb.append("<html><body>");
		File f = new File(ConsumerView.getHiddenOutputPath());
		sb.append("Timestamp: " + System.currentTimeMillis() + "<br/><br/>");
		if (f.exists() && f.isDirectory()) {
			sb.append("Number of files in output folder: " + f.list().length + "<br/><br/>");
			for (File generated : f.listFiles()) {
				sb.append("<b>Generated file: " + generated.getAbsolutePath() + "</b><br/><br/> ");
				if (generated.isFile()) {
					for (String line : getLinesOfFile(generated)) {
						sb.append(line);
						sb.append("<br/>");
					}
				}
			}
		}
		sb.append("</body></html>");
		browser.setText(sb.toString());
	}

	/**
	 * Get lines of a file
	 * 
	 * @param file
	 * @return list of strings
	 */
	public static List<String> getLinesOfFile(File file) {
		List<String> lines = new ArrayList<String>();
		try {
			FileInputStream fstream = new FileInputStream(file);
			DataInputStream in = new DataInputStream(fstream);
			BufferedReader br = new BufferedReader(new InputStreamReader(in));
			String strLine;
			while ((strLine = br.readLine()) != null) {
				lines.add(strLine);
			}
			in.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return lines;
	}

	/**
	 * Get string
	 * 
	 * @param file
	 * @return
	 */
	public static String getStringOfFile(File file) {
		StringBuilder string = new StringBuilder();
		for (String line : getLinesOfFile(file)) {
			string.append(line + "\n");
		}
		string.setLength(string.length() - 1);
		return string.toString();
	}
}
