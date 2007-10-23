package edu.columbia.threescompany.graphics;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.LinkedList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import edu.columbia.threescompany.client.MainGameThread;
import edu.columbia.threescompany.players.LocalPlayer;
import edu.columbia.threescompany.players.Player;

public class HotSeatGui extends JFrame {
	private static final long serialVersionUID = -8656693214492658606L;
	private JTextField player1Name;
	private JTextField player2Name;
	
	public HotSeatGui() {
		JPanel _mainpane = (JPanel)this.getContentPane();
		_mainpane.setLayout(new GridLayout(3, 2));
		
		_mainpane.add(new JLabel("Player 1"));
		player1Name = new JTextField("Player 1");
		_mainpane.add(player1Name);

		_mainpane.add(new JLabel("Player 2"));
		player2Name = new JTextField("Player 2");
		_mainpane.add(player2Name);

		JButton startGame = new JButton("Start");
		startGame.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				List<Player> players = new LinkedList<Player>();
				players.add(new LocalPlayer(player1Name.getText()));
				players.add(new LocalPlayer(player2Name.getText()));
				Thread gameLoopThread = new Thread(new MainGameThread(players));
				gameLoopThread.start();
				dispose();
			}
		});
		_mainpane.add(startGame);
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		pack();
		setVisible(true);
	}
}
