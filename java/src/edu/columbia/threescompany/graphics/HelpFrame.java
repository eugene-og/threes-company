package edu.columbia.threescompany.graphics;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.io.IOException;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.text.BadLocationException;
import javax.swing.text.StyledDocument;

public class HelpFrame extends JFrame {

	private static final long serialVersionUID = -3003280397568559407L;
	
	public HelpFrame() {
		super("Blobs Help");
		
		JTextPane help = new JTextPane();
		try {
			help.setPage(getClass().getResource("/help.html"));
		} catch (IOException e1) {
			e1.printStackTrace();
			StyledDocument doc = help.getStyledDocument();
			try {
				doc.insertString(0, "Error loading help.", null);
			} catch (BadLocationException e) {
				e.printStackTrace();
			}
			help.setDocument(doc);
		}
		
		//Put the editor pane in a scroll pane.
		JScrollPane editorScrollPane = new JScrollPane(help);
		editorScrollPane.setVerticalScrollBarPolicy(
		                JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		int width = (int) (screenSize.getWidth() * 0.75);
		int height = (int) (screenSize.getHeight() * 0.75);
		editorScrollPane.setPreferredSize(new Dimension(width, height));
		editorScrollPane.setMinimumSize(new Dimension(10, 10));
		add(editorScrollPane);
		
		pack();
		setLocationRelativeTo(null);
		setVisible(true);
	}
	
	public static void main(String[] args) {
		new HelpFrame();
	}

}
