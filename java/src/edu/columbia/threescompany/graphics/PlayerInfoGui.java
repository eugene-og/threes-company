package edu.columbia.threescompany.graphics;

import java.awt.GraphicsEnvironment;
import java.awt.GridLayout;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import edu.columbia.threescompany.game.Player;

public class PlayerInfoGui extends JFrame {
	private static final long serialVersionUID = -8656693214492658606L;
	private List<Player> _players = new ArrayList<Player>();
	private List<JTextField> _playerNames= new ArrayList<JTextField>();;
	
	private PlayerInfoGui(final int numPlayers) {
		JPanel _mainpane = (JPanel) this.getContentPane();
		_mainpane.setLayout(new GridLayout(3, 2));
		
		for (int i = 0; i < numPlayers; i++)
			addTextField(_mainpane, _playerNames, i);

		final JButton startGame = new JButton("Start");
		startGame.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				for (int i = 0; i < numPlayers; i++)
					_players.add(new Player(i, _playerNames.get(i).getText()));
				dispose();
			}
		});
		_mainpane.add(startGame);
		
        // Get coordinates such that window's centered on screen
		Rectangle rect = GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds();
		int xPos = (int)(rect.getWidth() - getPreferredSize().width)/2;
		int yPos = (int)(rect.getHeight() - getPreferredSize().height)/2;
		
		setLocation(xPos, yPos);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		pack();
		setVisible(true);
	}

	private void addTextField(JPanel _mainpane, final List<JTextField> playerNames, int i) {
		_mainpane.add(new JLabel("Player " + (i + 1)));
		playerNames.add(i, new JTextField("Name"));
		_mainpane.add(playerNames.get(i));
	}
	
	public static List<Player> getPlayers(int numPlayers) {
		PlayerInfoGui gui = new PlayerInfoGui(numPlayers);
		while (gui._players.isEmpty()) {
			try {			
				// TODO I feel hackish
				Thread.sleep(50);
			} catch (InterruptedException e) {}
		}
		return gui._players;
	}
}
