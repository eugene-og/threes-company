package edu.columbia.threescompany.graphics;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GraphicsEnvironment;
import java.awt.GridLayout;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import edu.columbia.threescompany.client.GameType;

public class PreGameGui extends JFrame {
	/* Zach: This needs to be wrapped properly into BlobsClient, so we can
	 * keep all our data in one place. TODO on my part. This will pretty much
	 * work the same, so nobody needs to make code changes yet.
	 */
	
	private static final long serialVersionUID = -4557239773540965286L;
	private GameType _gameType = null;
	
	private PreGameGui() {
		super("Welcome to Blobs");
		JPanel mainPane = (JPanel)this.getContentPane();
		mainPane.setLayout(new GridLayout(5, 1));
		mainPane.add(new JLabel(""));
		
		mainPane.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8), 
						   BorderFactory.createCompoundBorder(BorderFactory.createTitledBorder("Pre-game Setup"), 
															  BorderFactory.createEmptyBorder(5, 5, 5, 5))));
		
		JPanel contentPane = new JPanel(new BorderLayout());
		
		addGameButton(contentPane, "Network", "Challenge a player over the internet", GameType.NETWORK);
		mainPane.add(contentPane);
		mainPane.add(new JLabel(""));
		
		contentPane = new JPanel(new BorderLayout());
		addGameButton(contentPane, "Hotseat", "Switch between turns", GameType.HOTSEAT);
		
		mainPane.add(contentPane);
		mainPane.add(new JLabel(""));
		
        // Get coordinates such that window's centered on screen
		Rectangle rect = GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds();
		int xPos = (int)(rect.getWidth() - GuiConstants.PREGAME_GUI_WIDTH)/2;
		int yPos = (int)(rect.getHeight() - GuiConstants.PREGAME_GUI_HEIGHT)/2;
		
		setPreferredSize(new Dimension(GuiConstants.PREGAME_GUI_WIDTH, GuiConstants.PREGAME_GUI_HEIGHT));
		setLocation(xPos, yPos);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		pack();
		setVisible(true);
	}
	
	private void addGameButton(JPanel pane, String name, String description, final GameType type) {
		JButton btn = new JButton(name);
		btn.setPreferredSize(new Dimension(100,10));
		btn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				_gameType = type;
				dispose();
			}
		});
		pane.add(btn, BorderLayout.WEST);
		
		JLabel label = new JLabel(description);
		label.setFont(GuiConstants.CHAT_FONT);
		pane.add(label, BorderLayout.EAST);
	}
	
	public static GameType getGameType() {
		PreGameGui gui = new PreGameGui();
		while (gui._gameType == null) {
			try {			
				// TODO I feel hackish
				Thread.sleep(50);
			} catch (InterruptedException e) {}
		}
		return gui._gameType;
	}
}
