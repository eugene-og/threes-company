package edu.columbia.threescompany.graphics;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

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
		JPanel mainPane = (JPanel)this.getContentPane();
		mainPane.setLayout(new GridLayout(4, 1));
		mainPane.add(new JLabel("Blobs"));
		
		addGameButton(mainPane, "Network", GameType.NETWORK);
		addGameButton(mainPane, "Hotseat", GameType.HOTSEAT);
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		pack();
		setVisible(true);
	}
	
	private void addGameButton(JPanel pane, String name, final GameType type) {
		JButton btn = new JButton(name);
		btn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				_gameType = type;
				dispose();
			}
		});
		pane.add(btn);
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
