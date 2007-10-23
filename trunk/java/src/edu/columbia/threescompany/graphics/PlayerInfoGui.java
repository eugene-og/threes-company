package edu.columbia.threescompany.graphics;

import java.awt.GridLayout;
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
				Thread.sleep(50);
			} catch (InterruptedException e) {}
		}
		return gui._players;
	}
}
