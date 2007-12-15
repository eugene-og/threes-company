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
	private JPanel _mainPane, _contentPane;
	private static JTextField _serverAddress;
	private static JTextField _serverPort;
	
	private PlayerInfoGui(final int numPlayers) {
		super("Hotseat Player Setup");
		_mainPane = (JPanel) this.getContentPane();
		_mainPane.setLayout(new BorderLayout());
		_contentPane = new JPanel();
		_contentPane.setLayout(new GridLayout(4, 2));
		
		_mainPane.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8), 
				   BorderFactory.createCompoundBorder(BorderFactory.createTitledBorder("Network Setup"), 
													  BorderFactory.createEmptyBorder(5, 5, 5, 5))));
		
		for (int i = 0; i < numPlayers; i++) {
			addTextField(_playerNames, i);
		}
		addServerAndPortFields();
		_mainPane.add(_contentPane, BorderLayout.NORTH);
		
		JButton startGame = new JButton("Start");
		startGame.setPreferredSize(new Dimension(50,30));
		startGame.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				for (int i = 0; i < numPlayers; i++) {
					_players.add(new Player(_playerNames.get(i).getText()));
				}
				dispose();
			}
		});
		_mainPane.add(startGame, BorderLayout.SOUTH);
		
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

	private void addServerAndPortFields() {
		JLabel label = new JLabel("Server address:");
		label.setFont(GuiConstants.CHAT_FONT);
		_contentPane.add(label);
		_serverAddress = new JTextField("localhost");
		_contentPane.add(_serverAddress);
		label = new JLabel("Server port:");
		label.setFont(GuiConstants.CHAT_FONT);
		_contentPane.add(label);
		_serverPort = new JTextField("3444");
		_contentPane.add(_serverPort);
	}
	private void addTextField(final List<JTextField> playerNames, int i) {
		JLabel label = new JLabel("Player " + (i + 1) + " enter your handle");
		label.setFont(GuiConstants.CHAT_FONT);
		_contentPane.add(label);
		playerNames.add(i, new JTextField("Player "+(i+1), 12));
		_contentPane.add(playerNames.get(i));
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

	public static String getServerAddress() {
		return _serverAddress.getText();
	}

	public static String getServerPort() {
		return _serverPort.getText();
	}
	
	
}
