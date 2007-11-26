package edu.columbia.threescompany.graphics;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GraphicsEnvironment;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.UIManager;

import edu.columbia.threescompany.client.ChatThread;
import edu.columbia.threescompany.client.LocalGameState;
import edu.columbia.threescompany.common.Coordinate;
import edu.columbia.threescompany.game.graphics.GUIGameMove;
import edu.columbia.threescompany.gameobjects.Blob;
import edu.columbia.threescompany.gameobjects.GameObject;
import edu.columbia.threescompany.gameobjects.GameParameters;

public class Gui extends JFrame {
	
	private static final long serialVersionUID = -5234906655320340040L;
	private int _xPos, _yPos;
	private Board _board;
	private BoardMouseListener _boardMouseListener;
	private JTextField _txtLine;
	private JTextArea _txtArea;
	private JPanel[] _ap_panes;
	private Color[] _ap_colors = {Color.RED, Color.RED, Color.RED, 
			 Color.YELLOW, Color.YELLOW, Color.YELLOW,
			 Color.GREEN, Color.GREEN, Color.GREEN, Color.GREEN};
	private ChatThread _chatThread;
	private LocalGameState _gameState;
	private Map<Blob, Coordinate> _blobMoves; // final positions
	private Blob _selectedBlob;
	private Integer _activePlayer; // Null means no one's turn
	public TurnEndCoordinator _turnEndCoordinator; // This seems like overkill, but I don't know how else to use wait 
	                                               // and notify across classes
	
	private static Gui _instance;
	
	public static Gui getInstance(ChatThread thread) {
		if (_instance == null) _instance = new Gui(thread);
		return _instance;
	}

	private Gui(ChatThread chatThread) {
		super("Welcome to Blobs!");
		
		_gameState = null;
		_turnEndCoordinator = new TurnEndCoordinator();
		_activePlayer = null;
		
		try{ UIManager.setLookAndFeel( UIManager.getSystemLookAndFeelClassName() ); }
        catch( Exception e ) { e.printStackTrace(); }
        
		_chatThread = chatThread;
        
        // Get coordinates such that window's centered on screen
		Rectangle rect = GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds();
		_xPos = (int)(rect.getWidth() - GuiConstants.GUI_WIDTH)/2;
		_yPos = (int)(rect.getHeight() - GuiConstants.GUI_HEIGHT)/2;
		
		// TODO canvas draws on top of menus?? wtf
		// Begin creating the File menu
        JMenuBar menubar = new JMenuBar();
        JMenu fileMenu = new JMenu("File");
        fileMenu.setMnemonic('F');

        // Setup exit menuitem with 'X' icon
        JMenuItem menuitem = new JMenuItem(" Exit", 'E');
        menuitem.setIcon(new ImageIcon(GuiConstants.IMAGES_MENU_DIR+"exit16.gif"));
        Insets in = menuitem.getInsets(); in.left -= 16;
		menuitem.setMargin(in);
		menuitem.setActionCommand("Exit");
        menuitem.addActionListener(new MenuItemListener());
        fileMenu.add(menuitem);
        menubar.add(fileMenu);

        // Setup the Help menu
        JMenu helpMenu = new JMenu("Help");
        helpMenu.setMnemonic('H');

//        // Setup contents menuitem with question mark icon
//        menuitem = new JMenuItem(" Contents", 'C');
//        menuitem.setIcon(new ImageIcon(GuiConstants.IMAGES_MENU_DIR+"help16.gif"));
//        in = menuitem.getInsets(); in.left -= 16;
//		menuitem.setMargin(in);
//		menuitem.setActionCommand("Contents");
//		menuitem.addActionListener(new MenuItemListener());
//        menuitem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_H, ActionEvent.CTRL_MASK));
//        
//        helpMenu.add(menuitem);
//        helpMenu.addSeparator();

        // Setup the About menuitem with the exclamation icon
        menuitem = new JMenuItem(" About...",'A');
        menuitem.setIcon(new ImageIcon(GuiConstants.IMAGES_MENU_DIR+"about16.gif"));
        in = menuitem.getInsets(); in.left -= 16;
		menuitem.setMargin(in);
        menuitem.setActionCommand("About...");
        menuitem.addActionListener(new MenuItemListener());
        helpMenu.add(menuitem);

        menubar.add(helpMenu);

		// TODO: variable naming sucks here. Put gui creation into methods for each UI piece.
        JPanel mainpane = getMainPane(); 
		JPanel boardpane = getBoardPane();
		
		mainpane.add(boardpane, BorderLayout.WEST);
		mainpane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		
		JPanel controlspane = new JPanel(new BorderLayout());
		JPanel ap_pane = new JPanel(new GridLayout(1,10));
		_ap_panes = new JPanel[10];
		
		for (int i=0; i<10; i++) {
			_ap_panes[i] = new JPanel();
			_ap_panes[i].setPreferredSize(new Dimension(20,20));
			_ap_panes[i].setBackground(_ap_colors[i]);
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
		donebutton.addActionListener(new DoneButtonListener());
		buttonpane.add(donebutton);
		buttonpane.setBorder(BorderFactory.createEmptyBorder(0, 0, 175, 0));
		buttonpane.setBackground(GuiConstants.BG_COLOR);
		
		controlspane.add(buttonpane, BorderLayout.SOUTH);
		controlspane.setBackground(GuiConstants.BG_COLOR);
		
		mainpane.add(controlspane, BorderLayout.EAST);
		
		
		setJMenuBar(menubar);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLocation(_xPos, _yPos);
		setResizable(false);
		pack();
		setVisible(true);

		_board.initGraphicsBuffer();
	}

	private JPanel getBoardPane() {
		JPanel pane = new JPanel();
		_board = new Board();
		_boardMouseListener = new BoardMouseListener();
		_board.addMouseListener(_boardMouseListener);
		_board.addMouseMotionListener(new BoardMouseMotionListener());
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
		for (int i=0; i < ap; i++)
			_ap_panes[i].setBackground(_ap_colors[i]);
	}
	
	public void drawState(LocalGameState gameState)
	{
		_gameState = gameState;
		_board.drawState(gameState);
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
	
    /**
     * Listener for JMenuBar menu items
     */
    private class MenuItemListener implements ActionListener 
    {
        public void actionPerformed( ActionEvent e ) {
            if( e.getActionCommand().equals( "Exit" ) ) {
            	// TODO want to broadcast message that i'm exiting?
            	System.exit(0);
			}
            else if( e.getActionCommand().equals( "About..." ) ) // Display about dialog box
                JOptionPane.showMessageDialog(null, GuiConstants.HELP_ABOUT,
                									"About...",
                									JOptionPane.INFORMATION_MESSAGE );
            else if( e.getActionCommand().equals( "Contents" ) ) // Display license dialog
            	JOptionPane.showMessageDialog(null, "Help?  Get playing!", "Contents", 
													JOptionPane.INFORMATION_MESSAGE );
        }
    }
    
    private class BoardMouseListener implements MouseListener
    {
		public void mouseClicked(MouseEvent e) {
			// TODO Make movement input right. This is a very rough first pass to get things moving.
			Point p = e.getPoint();
			addChatLine("Clicked: ("+p.x+","+p.y+")");
			if (_gameState == null) { // drawBoard hasn't been called yet
				return;
			}
			if (_activePlayer == null) { // It's no one's turn
				return;
			}
			// The world variables are locations in world/game space (as opposed to screen space)
			double worldX = (double)p.x * (int)GameParameters.BOARD_SIZE / _board.getWidth();
			double worldY = (double)p.y * (int)GameParameters.BOARD_SIZE / _board.getHeight();
			Coordinate worldClick = new Coordinate(worldX, worldY);
			// TODO have screen to world and world to screen in only one place
			// TODO only allow selecting blobs belonging to activePlayer
			Blob newSelection = blobClickedOn(worldClick);
			if (newSelection != null) { // clicked a blob
				_selectedBlob = newSelection;
				addChatLine("Selected blob " + _selectedBlob.toString());
			} else if (_selectedBlob != null) { // clicked a destination for a blob
				_blobMoves.put(_selectedBlob, worldClick);
				addChatLine("Queueing move of blob " + _selectedBlob.toString() + " to " + worldClick.toString());
			}
		}
		
		/** not needed */
		public void mouseEntered(MouseEvent arg0) {}
		public void mouseExited(MouseEvent arg0) {}
		public void mousePressed(MouseEvent arg0) {}
		public void mouseReleased(MouseEvent arg0) {}
		
	    private Blob blobClickedOn(Coordinate worldClick) {
			for (GameObject object : _gameState.getObjects()) {
				if (object instanceof Blob) {
					double clickObjectDistance = object.getPosition().distanceFrom(worldClick);
					if (clickObjectDistance <= object.getRadius()) {
						return (Blob)object;
					}
				}
			}
	    	return null;
	    }
    }
    
    private class BoardMouseMotionListener implements MouseMotionListener {

		public void mouseDragged(MouseEvent e) {
			Point p = e.getPoint();
			if (p.x > 0 && p.x < GuiConstants.BOARD_LENGTH
					&& p.y > 0 && p.y < GuiConstants.BOARD_LENGTH) {
				addChatLine("mouseDragged: ("+p.x+","+p.y+")");
				
				// TODO Send this Point to real-time physics engine for line drawing
				
				// TODO get data back from physics to draw line for projected path
			}
				
			
		}

		public void mouseMoved(MouseEvent e) {}
    	
    }
    
	private class DoneButtonListener implements ActionListener {
		public void actionPerformed(ActionEvent event) {
			// I'm assuming this is only clicks. If not we need to check the event type.
			_turnEndCoordinator.turnDone();
		}
	}
	
	public void addChatLine(String line) {
		_txtArea.setText(_txtArea.getText() + line + "\n");
	}

	public GUIGameMove getMoveFor(int activePlayer) {
		// TODO Moves need a lot of work
		addChatLine("It's player " + activePlayer + "'s turn.");
		_turnEndCoordinator.turnStart();
		_blobMoves = new HashMap<Blob, Coordinate>();
		_selectedBlob = null;
		_activePlayer = activePlayer;
		_turnEndCoordinator.waitUntilTurnDone();
		_activePlayer = null;
		return new GUIGameMove(_blobMoves, new ArrayList<Blob>());
	}
	
}
