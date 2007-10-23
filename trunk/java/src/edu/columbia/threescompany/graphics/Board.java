package edu.columbia.threescompany.graphics;

import java.awt.BasicStroke;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferStrategy;
import java.util.List;

import edu.columbia.threescompany.gameobjects.GameObject;

public class Board extends Canvas {

	private BufferStrategy strategy;
	
	public Board()
	{
		setBounds(0,0, GuiConstants.BOARD_LENGTH, GuiConstants.BOARD_LENGTH);
		setIgnoreRepaint(true);
	}
	
	/**
	 * Board must be added to the frame or windows before this is called. 
	 */
	public void initGraphicsBuffer()
	{
		createBufferStrategy(2);
		strategy = getBufferStrategy();
	}
	
	public void drawState(List<GameObject> list)
	{
		Graphics2D surface = (Graphics2D) strategy.getDrawGraphics();
		surface.setColor(Color.black);
		surface.setStroke(new BasicStroke(3.0f));
		surface.setColor(Color.blue);
		surface.draw(new Ellipse2D.Double(50,50,20,200));
		surface.drawLine(0, 0, 400,400);
		surface.drawString("Yay strings!", 200, 200);
		
		surface.dispose();
		strategy.show();
	}
}
