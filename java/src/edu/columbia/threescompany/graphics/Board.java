package edu.columbia.threescompany.graphics;

import java.awt.BasicStroke;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.MediaTracker;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.File;

import javax.swing.ImageIcon;

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
	
	private LocalGameState _gameState;
	private GraphicalGameState _graphicalState;
	Graphics offscreenSurface;
	Image offscreenImage = null;

	public Board(GraphicalGameState graphicalState)
	{
		if (graphicalState == null) {
			throw new NullPointerException();
		}
		setBounds(0,0, GuiConstants.BOARD_LENGTH, GuiConstants.BOARD_LENGTH);
		_gameState = null;
		_graphicalState = graphicalState;
		setFont(new Font("Arial", 0, 1)); // Default font is huge
	}
	
	/**
	 * Board must be added to the frame or windows before this is called. 
	 */
	public void initGraphicsBuffer() {
		// I'm leaving the method here because we may need it to initialize some better double buffering stuff.
	}
	
	/**
	 * The x and y that Ellipse2D takes are its upper left corner. It's more convenient for us to specify them as the
	 * center, so this takes them in our form and returns the appropriate Ellipse2D.
	 */
	private Ellipse2D circle(double centerX, double centerY, double radius) {
		// TODO We might need to subtract sqrt(radius ^ 2 + radius ^ 2) instead of radius to properly align them. 
		// Needs testing to check. 
		return new Ellipse2D.Double(centerX - radius, centerY - radius, radius * 2, radius * 2);
	}
	
    public void update(Graphics g) {
    	// Adapted from http://home.comcast.net/~jml3on/java/tricks/double-buffering.html
    	// TODO Do this more efficiently - don't create a new back buffer each time
		// create the offscreen buffer and associated Graphics
		offscreenImage = createImage(getWidth(), getHeight());
		offscreenSurface = offscreenImage.getGraphics();
		//Rectangle clippingRegion = g.getClipBounds();
		// TODO We'll redraw the whole thing for now to make sure we're not leaving junk around we shouldn't be
		Rectangle clippingRegion = new Rectangle(getWidth(), getHeight());
		// clear the exposed area
		offscreenSurface.setColor(getBackground());
		offscreenSurface.fillRect(0, 0, clippingRegion.width, clippingRegion.height);
		offscreenSurface.setColor(getForeground());
		// do normal redraw
		offscreenSurface.translate(-clippingRegion.x, -clippingRegion.y);
		paint(offscreenSurface);
		// transfer offscreen to window
		g.drawImage(offscreenImage, clippingRegion.x, clippingRegion.y, this);
	}
    
	public void paint(Graphics g)
	{
		// Graphics2D surface = (Graphics2D) strategy.getDrawGraphics();
		Graphics2D surface = (Graphics2D) g;
		
		surface.scale(this.getWidth()/GameParameters.BOARD_SIZE, this.getHeight()/GameParameters.BOARD_SIZE);
		surface.setColor(Color.black);
		surface.setStroke(new BasicStroke(0.1f));
		
		if (_gameState == null) {
			surface.drawString("Waiting for start...", 0, 10);
			return;
		}
		
		for (GameObject item : _gameState.getObjects()) {
			if (item.isDead()) {
				continue;
			}
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
			//System.out.println("x: "+pos.x+", y: "+pos.y);
			Ellipse2D blobToDraw = circle(pos.x, pos.y, item.getRadius());
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
				Ellipse2D selectionIndicator = circle(pos.x, pos.y, item.getRadius() + 0.8);
				surface.setStroke(new BasicStroke(0.1f));
				surface.draw(selectionIndicator);
			}
		}
		
//		MediaTracker media = new MediaTracker(this);
//		Image image = Toolkit.getDefaultToolkit().getImage(GuiConstants.IMAGES_DIR+"boom_black.png");
//		media.addImage(image, 0);
//		try { media.waitForID(0); }
//		catch (Exception e) {}
//		//surface.drawImage(image, 0, 0, this);

//		Image tmp = createImage(1000, 1000);
//		Graphics2D tmpgraphic = (Graphics2D)tmp.getGraphics();
		//tmpgraphic.scale(GameParameters.BOARD_SIZE/this.getWidth(), GameParameters.BOARD_SIZE/this.getHeight());
		//tmpgraphic.drawImage(image, 0, 0, null);
		
//		try {
//			// TODO make function that takes image and 0-20 board units and draws it.
//			BufferedImage image = javax.imageio.ImageIO.read(new File(GuiConstants.IMAGES_DIR+"boom_black.png")); 
//			AffineTransform xtrans = AffineTransform.getScaleInstance((double)GameParameters.BOARD_SIZE/this.getWidth(),
//																	  (double)GameParameters.BOARD_SIZE/this.getHeight());
//			AffineTransform xtranslate = AffineTransform.getTranslateInstance(5, 5);
//			xtranslate.concatenate(xtrans);
//			surface.drawRenderedImage(image, xtranslate);
//		}
//		catch (Exception e) {e.printStackTrace();}
		
	}
	
	public void drawState(LocalGameState gameState)
	{
		_gameState = gameState;
		repaint();
	}
}
