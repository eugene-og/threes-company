package edu.columbia.threescompany.graphics;

import java.awt.BasicStroke;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferStrategy;

import edu.columbia.threescompany.client.LocalGameState;
import edu.columbia.threescompany.common.Coordinate;
import edu.columbia.threescompany.gameobjects.DeathRayBlob;
import edu.columbia.threescompany.gameobjects.ExplodingBlob;
import edu.columbia.threescompany.gameobjects.GameObject;
import edu.columbia.threescompany.gameobjects.GameParameters;
import edu.columbia.threescompany.gameobjects.PullBlob;
import edu.columbia.threescompany.gameobjects.PushBlob;
import edu.columbia.threescompany.gameobjects.SlipperyBlob;

public class Board extends Canvas {
	private static final long serialVersionUID = 3523741675646270283L;
	
	private BufferStrategy strategy;
	private LocalGameState _gameState;
	private GraphicalGameState _graphicalState;
	
	public Board(GraphicalGameState graphicalState)
	{
		if (graphicalState == null) {
			throw new NullPointerException();
		}
		setBounds(0,0, GuiConstants.BOARD_LENGTH, GuiConstants.BOARD_LENGTH);
		_gameState = null;
		_graphicalState = graphicalState;
		//setIgnoreRepaint(true);
		setFont(new Font("Arial", 0, 1)); // Default font is huge
	}
	
	/**
	 * Board must be added to the frame or windows before this is called. 
	 */
	public void initGraphicsBuffer()
	{
		createBufferStrategy(2);
		strategy = getBufferStrategy();
	}
	
	/**
	 * The x and y that Ellipse2D takes are its upper left corner. It's more convenient for us to specify them as the
	 * center, so this takes them in our form and returns the appropriate Ellipse2D.
	 */
	private Ellipse2D circle(double centerX, double centerY, double radius) {
		return new Ellipse2D.Double(centerX - radius, centerY - radius, radius, radius);
	}
	
	public void paint(Graphics g)
	{
		//Graphics2D surface = (Graphics2D) strategy.getDrawGraphics();
		Graphics2D surface = (Graphics2D) g;
		surface.scale(this.getWidth()/GameParameters.BOARD_SIZE, this.getHeight()/GameParameters.BOARD_SIZE);
		surface.setColor(Color.black);
		surface.setStroke(new BasicStroke(0.1f));
		
		if (_gameState == null) {
			surface.drawString("Waiting for start...", 0, 10);
			return;
		}
		
		for (GameObject item : _gameState.getObjects()) {
			
			if (item instanceof PushBlob)
				surface.setColor(Color.blue);
			else if (item instanceof PullBlob)
				surface.setColor(Color.red);
			else if (item instanceof DeathRayBlob)
				surface.setColor(Color.black);
			else if (item instanceof ExplodingBlob)
				surface.setColor(Color.green);
			else if (item instanceof SlipperyBlob)
				surface.setColor(Color.yellow);
			Coordinate pos = item.getPosition();
			Ellipse2D.Double blobToDraw = new Ellipse2D.Double(pos.x, pos.y, item.getRadius(), item.getRadius());
			surface.setStroke(new BasicStroke(0.3f));
			surface.draw(blobToDraw);
			if (item.getOwner().getName().equals(_gameState.getPlayers().get(0).getName())) {
				surface.setColor(Color.lightGray);
			} else {
				surface.setColor(Color.darkGray);
			}
			surface.fill(blobToDraw);
			
			if (_graphicalState.getSelectedBlob() == item) {
				surface.setColor(Color.orange);
				Ellipse2D.Double selectionIndicator = new Ellipse2D.Double(pos.x, pos.y, item.getRadius() + 0.8, item.getRadius() + 0.8);
				surface.setStroke(new BasicStroke(0.1f));
				surface.draw(selectionIndicator);
			}
		}
//		ImageIcon icon = new ImageIcon("Bsdfall.png");
//		surface.drawImage(icon.getImage(), 3, 5, 2, 2, icon.getImageObserver());
	}
	
	public void drawState(LocalGameState gameState)
	{
		_gameState = gameState;
		repaint();
	}
}
