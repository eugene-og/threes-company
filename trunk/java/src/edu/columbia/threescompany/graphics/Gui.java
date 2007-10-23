package edu.columbia.threescompany.graphics;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GraphicsEnvironment;
import java.awt.GridLayout;
import java.awt.Rectangle;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.UIManager;

import edu.columbia.threescompany.client.ChatThread;
import edu.columbia.threescompany.client.LocalGameState;
import edu.columbia.threescompany.game.GameMove;

public class Gui extends JFrame {
	
	private static final long serialVersionUID = -5234906655320340040L;
	private int _xPos, _yPos;
	private Board _board;
	private JTextField _txtLine;
	private JTextArea _txtArea;
	private JPanel[] _ap_panes;
	private ChatThread _chatThread;
		
	/**
	 * GuiHolder is loaded on the first execution of Gui.getInstance() 
	 * or the first access to GuiHolder.INSTANCE, not before.
	 */
	private static class GuiHolder { 
		private final static Gui INSTANCE = new Gui();
	}
	
	public static Gui getInstance() {
		return GuiHolder.INSTANCE;
	}
	  
	private Gui() {
		super("Welcome to Blobs!");
		try{ UIManager.setLookAndFeel( UIManager.getSystemLookAndFeelClassName() ); }
        catch( Exception e ) { e.printStackTrace(); }
        
        // Get coordinates such that window's centered on screen
		Rectangle rect = GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds();
		_xPos = (int)(rect.getWidth() - GuiConstants.GUI_WIDTH)/2;
		_yPos = (int)(rect.getHeight() - GuiConstants.GUI_HEIGHT)/2;        

		// TODO: variable naming sucks here. Put gui creation into methods for each UI piece.
        JPanel mainpane = getMainPane(); 
		JPanel boardpane = getBoardPane();
		
		mainpane.add(boardpane, BorderLayout.WEST);
		mainpane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		
		JPanel controlspane = new JPanel(new BorderLayout());
		JPanel ap_pane = new JPanel(new GridLayout(1,10));
		_ap_panes = new JPanel[10];
		Color[] ap_colors = {Color.RED, Color.RED, Color.RED, 
							 Color.YELLOW, Color.YELLOW, Color.YELLOW,
							 Color.GREEN, Color.GREEN, Color.GREEN, Color.GREEN};
		
		for (int i=0; i<10; i++) {
			_ap_panes[i] = new JPanel();
			_ap_panes[i].setPreferredSize(new Dimension(20,20));
			_ap_panes[i].setBackground(ap_colors[i]);
			_ap_panes[i].setBorder(BorderFactory.createLineBorder(Color.WHITE, 1));
			ap_pane.add(_ap_panes[i]);
		}
		ap_pane.setBorder(	BorderFactory.createCompoundBorder(
							BorderFactory.createTitledBorder(
							BorderFactory.createLineBorder(Color.GRAY), "AP"),
							BorderFactory.createEmptyBorder(0, 5, 5, 0)));
		ap_pane.setBackground(GuiConstants.BG_COLOR);
		controlspane.add(ap_pane, BorderLayout.NORTH);
		
		JPanel insideControlsPane = new JPanel(new BorderLayout());
		_txtArea = new JTextArea();
		_txtArea.setRows(8);
		_txtArea.setColumns(25);
		_txtArea.setEditable(false);
		_txtArea.setFont(GuiConstants.CHAT_FONT);
		_txtArea.setForeground(Color.GRAY);
		JScrollPane scrollPane = new JScrollPane(_txtArea);
		insideControlsPane.add(scrollPane, BorderLayout.NORTH);
		
		insideControlsPane.add(new JLabel("<html>&nbsp;</html>"), BorderLayout.CENTER);
		
		_txtLine = new JTextField("Send message to player");
		_txtLine.setFont(GuiConstants.CHAT_FONT);
		_txtLine.setForeground(Color.GRAY);
		_txtLine.addFocusListener(new ChatFocusListener());
		insideControlsPane.add(_txtLine, BorderLayout.SOUTH);
		_txtLine.addKeyListener(new ChatSendListener());
		insideControlsPane.setBorder(	BorderFactory.createCompoundBorder(
										BorderFactory.createTitledBorder(
										BorderFactory.createLineBorder(Color.GRAY), "Chat"),
										BorderFactory.createEmptyBorder(5, 5, 5, 5)));
		insideControlsPane.setBackground(GuiConstants.BG_COLOR);
		controlspane.add(insideControlsPane, BorderLayout.CENTER);
		controlspane.setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 0));
		
		JPanel buttonpane = new JPanel();
		JButton donebutton = new JButton("Done");
		donebutton.setMnemonic(KeyEvent.VK_D);
		donebutton.setPreferredSize(new Dimension(160, 25));
		donebutton.setFont(new Font("Tahoma", Font.BOLD, 14));
		buttonpane.add(donebutton);
		buttonpane.setBorder(BorderFactory.createEmptyBorder(0, 0, 175, 0));
		buttonpane.setBackground(GuiConstants.BG_COLOR);
		
		controlspane.add(buttonpane, BorderLayout.SOUTH);
		controlspane.setBackground(GuiConstants.BG_COLOR);
		
		mainpane.add(controlspane, BorderLayout.EAST);
		
		
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLocation(_xPos, _yPos);
		setResizable(false);
		pack();
		setVisible(true);

		_board.initGraphicsBuffer();
		_chatThread = new ChatThread();
	}

	private JPanel getBoardPane() {
		JPanel pane = new JPanel();
		_board = new Board();
		pane.add(_board);
		
		return pane;
	}

	private JPanel getMainPane() {
		JPanel mainpane = (JPanel)this.getContentPane();
		mainpane.setLayout(new BorderLayout());
		mainpane.setBackground(GuiConstants.BG_COLOR);
		
		return mainpane;
	}
	
	
	private void setAP(int ap) {
		/* Dan|John, this AP count is now valid and does not require thought. -- Zach */
//		for (int i=9; i >= 0; i++)
//			_ap_panes[i].setBackground(GuiConstants.BG_COLOR);
	}
	
	/**
	 * Is called from main game thread and gui processing thread.
	 */
	public synchronized void drawState(LocalGameState gameState)
	{
		_board.drawState(gameState.getObjects());
		setAP(gameState.getAP());
	}

	private class ChatFocusListener implements FocusListener {
		public void focusGained(FocusEvent e) {
			if (_txtLine.getText().equals("Send message to player"))
				_txtLine.setText("");
			_txtLine.setForeground(Color.BLACK);
		}

		public void focusLost(FocusEvent e) {
			_txtLine.setForeground(Color.GRAY);
		}
	}
	
	private class ChatSendListener implements KeyListener {
		public void keyPressed(KeyEvent e) {}
		public void keyTyped(KeyEvent e) {}
		
		public void keyReleased(KeyEvent e) {
			if (e.getKeyCode() == KeyEvent.VK_ENTER) {
				addChatLine(_txtLine.getText());
				_chatThread.sendLine(_txtLine.getText());
				_txtLine.setText("");
				// TODO: pass off text to new chat event
			}
		}
	}
	
	public void addChatLine(String line) {
		_txtArea.setText(_txtArea.getText() + line + "\n");
	}

	public GameMove getMoveFor(int activePlayer) {
		// TODO Dan and John, we need to discuss what GameMove is -- Zach
		return null;
	}
	
}
