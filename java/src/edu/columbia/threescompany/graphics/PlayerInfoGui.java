package edu.columbia.threescompany.graphics;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GraphicsEnvironment;
import java.awt.GridLayout;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
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
	private JPanel _mainPane;
	
	private PlayerInfoGui(final int numPlayers) {
		_mainPane = (JPanel) this.getContentPane();
		_mainPane.setLayout(new GridLayout(5, 1));
		
		_mainPane.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8), 
				   BorderFactory.createCompoundBorder(BorderFactory.createTitledBorder("Network Setup"), 
													  BorderFactory.createEmptyBorder(5, 5, 5, 5))));
		
		JPanel contentPane;
		_mainPane.add(new JLabel(""));
		
		for (int i = 0; i < numPlayers; i++) {
			contentPane = new JPanel(new BorderLayout());
			addTextField(contentPane, _playerNames, i);
			_mainPane.add(contentPane);
		}

		_mainPane.add(new JLabel(""));
		
		JButton startGame = new JButton("Start");
		startGame.setPreferredSize(new Dimension(50,10));
		startGame.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				for (int i = 0; i < numPlayers; i++)
					_players.add(new Player(i, _playerNames.get(i).getText()));
				dispose();
			}
		});
		_mainPane.add(startGame);
		
        // Get coordinates such that window's centered on screen
		Rectangle rect = GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds();
		int xPos = (int)(rect.getWidth() - GuiConstants.PLAYER_GUI_WIDTH)/2;
		int yPos = (int)(rect.getHeight() - GuiConstants.PLAYER_GUI_HEIGHT)/2;
		
		setPreferredSize(new Dimension(GuiConstants.PLAYER_GUI_WIDTH, GuiConstants.PLAYER_GUI_HEIGHT));
		setLocation(xPos, yPos);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		pack();
		setVisible(true);
	}

	private void addTextField(JPanel contentPane, final List<JTextField> playerNames, int i) {
		JLabel label = new JLabel("Player " + (i + 1) + " enter your handle");
		label.setFont(GuiConstants.CHAT_FONT);
		contentPane.add(label, BorderLayout.WEST);
		playerNames.add(i, new JTextField("username"+(i+1), 12));
		contentPane.add(playerNames.get(i), BorderLayout.EAST);
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
