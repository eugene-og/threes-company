package edu.columbia.threescompany.graphics;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GraphicsEnvironment;
import java.awt.GridLayout;
import java.awt.Rectangle;
import java.awt.image.BufferStrategy;
import java.util.LinkedList;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.UIManager;

import edu.columbia.threescompany.game.GameMove;
import edu.columbia.threescompany.game.Player;
import edu.columbia.threescompany.gameobjects.GameObject;

public class Gui extends JFrame {
	
	private int xPos, yPos;
	private Board _board;
		
	public Gui()
	{
		super("Welcome to Blobs!");
		try{ UIManager.setLookAndFeel( UIManager.getSystemLookAndFeelClassName() ); }
        catch( Exception e ) { e.printStackTrace(); }
        
        // Get coordinates such that window's centered on screen
		Rectangle rect = GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds();
		xPos = (int)(rect.getWidth() - GuiConstants.GUI_WIDTH)/2;
		yPos = (int)(rect.getHeight() - GuiConstants.GUI_HEIGHT)/2;        

        JPanel _mainpane = (JPanel)this.getContentPane();
		_mainpane.setLayout(new BorderLayout());
		
		JPanel _boardpane = new JPanel();
		_board = new Board();
		_boardpane.add(_board);
		_mainpane.add(_boardpane, BorderLayout.WEST);

		JPanel _controlspane = new JPanel();
		JTextArea txtArea = new JTextArea("A/S/L?");
		txtArea.setRows(5);
		txtArea.setColumns(30);
		txtArea.setEditable(false);
		JScrollPane scrollPane = new JScrollPane(txtArea);
		_controlspane.add(scrollPane);	
		_mainpane.add(_controlspane, BorderLayout.EAST); // 9th slot		
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLocation(xPos, yPos);
		//setResizable(false);
		pack();
		setVisible(true);

		_board.initGraphicsBuffer();
	}
	
	public void drawState(List<GameObject> gameState)
	{
		_board.drawState(gameState);
	}

	public GameMove getMove() {
		// TODO UI returns a list of actions
		return null;
	}

	public Player getPlayer() {
		// TODO Ask the user what they want, send it back
		// (name, color, etc.)
		return null;
	}
	
}
