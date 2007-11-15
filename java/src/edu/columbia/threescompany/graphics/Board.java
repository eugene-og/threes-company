package edu.columbia.threescompany.graphics;

import java.awt.BasicStroke;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferStrategy;
import java.util.ArrayList;
import java.util.List;

import javax.swing.ImageIcon;

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
	private List<GameObject> gameObjects;
	
	public Board()
	{
		setBounds(0,0, GuiConstants.BOARD_LENGTH, GuiConstants.BOARD_LENGTH);
		gameObjects = new ArrayList<GameObject>();
		//setIgnoreRepaint(true);
	}
	
	/**
	 * Board must be added to the frame or windows before this is called. 
	 */
	public void initGraphicsBuffer()
	{
		createBufferStrategy(2);
		strategy = getBufferStrategy();
	}
	
	public void paint(Graphics g)
	{
		//Graphics2D surface = (Graphics2D) strategy.getDrawGraphics();
		Graphics2D surface = (Graphics2D) g;
		surface.scale(this.getWidth()/GameParameters.BOARD_SIZE, this.getHeight()/GameParameters.BOARD_SIZE);
		surface.setColor(Color.black);
		surface.setStroke(new BasicStroke(0.1f));
		
		for (GameObject item : gameObjects) {
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
			surface.draw(new Ellipse2D.Double(pos.x, pos.y, item.getRadius(), item.getRadius()));
		}
//		ImageIcon icon = new ImageIcon("Bsdfall.png");
//		surface.drawImage(icon.getImage(), 3, 5, 2, 2, icon.getImageObserver());
	}
	
	public void drawState(List<GameObject> list)
	{
		gameObjects = list;
		repaint();
	}
}
