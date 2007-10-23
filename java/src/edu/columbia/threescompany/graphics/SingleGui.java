package edu.columbia.threescompany.graphics;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GraphicsEnvironment;
import java.awt.GridLayout;
import java.awt.Rectangle;
import java.awt.image.BufferStrategy;
import java.util.LinkedList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.UIManager;

//import edu.columbia.threescompany.game.GameMove;
import edu.columbia.threescompany.game.Player;
import edu.columbia.threescompany.gameobjects.GameObject;

public class SingleGui extends JFrame {
	
	private static final long serialVersionUID = -5234906655320340040L;
	private int _xPos, _yPos;
	private Board _board;
		
	public SingleGui()
	{
		super("Welcome to Blobs!");
		try{ UIManager.setLookAndFeel( UIManager.getSystemLookAndFeelClassName() ); }
        catch( Exception e ) { e.printStackTrace(); }
        
        // Get coordinates such that window's centered on screen
		Rectangle rect = GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds();
		_xPos = (int)(rect.getWidth() - GuiConstants.GUI_WIDTH)/2;
		_yPos = (int)(rect.getHeight() - GuiConstants.GUI_HEIGHT)/2;        

        JPanel mainpane = (JPanel)this.getContentPane();
		mainpane.setLayout(new BorderLayout());
		
		JPanel boardpane = new JPanel();
		_board = new Board();
		boardpane.add(_board);
		
		mainpane.add(boardpane, BorderLayout.WEST);
		mainpane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		
		JPanel controlspane = new JPanel(new BorderLayout());
		JPanel ap_pane = new JPanel(new GridLayout(1,10));
		JPanel[] ap_panes = new JPanel[10];
		Color[] ap_colors = {Color.RED, Color.YELLOW, Color.GREEN};
		
		for (int i=0; i<10; i++) {
			ap_panes[i] = new JPanel();
			ap_panes[i].setPreferredSize(new Dimension(20,20));
			ap_panes[i].setBackground(Color.red);
			//ap_panes[i].setVisible(true);
			ap_panes[i].setBorder(BorderFactory.createLineBorder(Color.CYAN, 1));
			ap_pane.add(ap_panes[i]);
		}
		ap_pane.setBorder(	BorderFactory.createCompoundBorder(
							BorderFactory.createTitledBorder(
							BorderFactory.createLineBorder(Color.GRAY), "AP"),
							BorderFactory.createEmptyBorder(5, 5, 5, 5)));
		controlspane.add(ap_pane, BorderLayout.NORTH);
		
		JPanel insideControlsPane = new JPanel(new BorderLayout());
		JTextArea txtArea = new JTextArea("Chat Window");
		txtArea.setRows(5);
		txtArea.setColumns(30);
		txtArea.setEditable(false);
		txtArea.setFont(GuiConstants.CHAT_FONT);
		txtArea.setForeground(Color.GRAY);
		JScrollPane scrollPane = new JScrollPane(txtArea);
		insideControlsPane.add(scrollPane, BorderLayout.NORTH);
		
		insideControlsPane.add(new JLabel("<html>&nbsp;</html>"), BorderLayout.CENTER);
		
		JTextField txtLine = new JTextField("Send message to play");
		txtLine.setFont(GuiConstants.CHAT_FONT);
		txtLine.setForeground(Color.GRAY);
		insideControlsPane.add(txtLine, BorderLayout.SOUTH);
		
		insideControlsPane.setBorder(	BorderFactory.createCompoundBorder(
										BorderFactory.createTitledBorder(
										BorderFactory.createLineBorder(Color.GRAY), "Controls"),
										BorderFactory.createEmptyBorder(5, 5, 5, 5)));
		controlspane.add(insideControlsPane, BorderLayout.SOUTH);
		controlspane.setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 0));
		
		mainpane.add(controlspane, BorderLayout.EAST);		
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLocation(_xPos, _yPos);
		setResizable(false);
		pack();
		setVisible(true);

		_board.initGraphicsBuffer();
	}
	
	/**
	 * Might be called from main game thread and gui processing thread. I'm not sure.
	 */
	public synchronized void drawState(List<GameObject> gameState)
	{
		_board.drawState(gameState);
	}

	public static void main(String[] args) {
		SingleGui gui = new SingleGui();
		while (true) gui.drawState(null);
	}
	
}
