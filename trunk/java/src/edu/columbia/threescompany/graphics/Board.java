package edu.columbia.threescompany.graphics;

import java.awt.BasicStroke;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.TexturePaint;
import java.awt.Toolkit;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RectangularShape;
import java.awt.image.BufferedImage;
import java.net.URL;
import java.text.DecimalFormat;

import edu.columbia.threescompany.client.LocalGameState;
import edu.columbia.threescompany.common.Coordinate;
import edu.columbia.threescompany.gameobjects.APCIPoint;
import edu.columbia.threescompany.gameobjects.AnchorPoint;
import edu.columbia.threescompany.gameobjects.Blob;
import edu.columbia.threescompany.gameobjects.DeathRayBlob;
import edu.columbia.threescompany.gameobjects.ExplodingBlob;
import edu.columbia.threescompany.gameobjects.GameObject;
import edu.columbia.threescompany.gameobjects.GameParameters;
import edu.columbia.threescompany.gameobjects.Hole;
import edu.columbia.threescompany.gameobjects.ImmovableGameObject;
import edu.columbia.threescompany.gameobjects.PullBlob;
import edu.columbia.threescompany.gameobjects.PushBlob;
import edu.columbia.threescompany.gameobjects.SlipperyBlob;
import edu.columbia.threescompany.gameobjects.SlipperySpot;

public class Board extends Canvas {
	private static final long serialVersionUID = 3523741675646270283L;
	
	private LocalGameState _gameState;
	private GraphicalGameState _graphicalState;
	Graphics offscreenSurface;
	Image offscreenImage = null;
	private URL _backgroundUrl;

	private double _movementCost;

	private Coordinate _mousePosition;
	
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
		// This is now a little more clumsy than it needs to be, but I want all translations between world and screen 
		// coords to go through screenToWorld and worldToScreen and this is the best way I could figure out to use 
		// them.
		double worldLeft = centerX - radius;
		double worldTop = centerY - radius;
		double worldRight = centerX + radius;
		double worldBottom = centerY + radius;
		Coordinate screenTopLeft = worldToScreen(new Coordinate(worldLeft, worldTop));
		Coordinate screenBottomRight = worldToScreen(new Coordinate(worldRight, worldBottom));
		double screenWidth = screenBottomRight.x - screenTopLeft.x;
		Ellipse2D circle = new Ellipse2D.Double(screenTopLeft.x, screenTopLeft.y, screenWidth, screenWidth);
		return circle;
	}
	
    public void update(Graphics g) {
    	// Adapted from http://home.comcast.net/~jml3on/java/tricks/double-buffering.html
    	
    	// create the offscreen buffer and associated Graphics
    	if (offscreenImage == null)
    		offscreenImage = createImage(getWidth(), getHeight());
    	
		offscreenSurface = offscreenImage.getGraphics();
		Rectangle clippingRegion = g.getClipBounds();
		
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
		DecimalFormat df = new DecimalFormat();
		df.setMinimumFractionDigits(1);
		df.setMaximumFractionDigits(1);

		drawBackground(surface);
		
		//surface.scale(this.getWidth()/GameParameters.BOARD_SIZE, this.getHeight()/GameParameters.BOARD_SIZE);
		surface.setColor(Color.black);
		surface.setStroke(new BasicStroke(0.1f));
		surface.setFont(new Font("Tahoma", Font.BOLD, 10));
		
		drawBoardBorder(surface);
		
		turnAntiAliasingOn(surface);
		if (_gameState == null) {
			surface.drawString("Waiting for start...", 1, getHeight()/2);
			return;
		}
		
		// Draw holes and slippery spots first so they're below blobs
		for (GameObject item : _gameState.getObjects()) {
			if (item instanceof Hole) {
				drawHole(surface, (Hole) item);
			} else if (item instanceof SlipperySpot) {
				drawSlipperySpot(surface, (SlipperySpot) item);
			}
		}
		for (GameObject item : _gameState.getObjects()) {
			if (item.isDead()) continue;
			if (item instanceof AnchorPoint) {
				surface.setColor(Color.black);
				drawAnchorPoint(surface, (AnchorPoint) item);
				continue;
			} else if (item instanceof APCIPoint) {
				surface.setColor(Color.cyan);
				drawAPCIPoint(surface, (APCIPoint) item);
				continue;
			} else if (item instanceof Hole || item instanceof SlipperySpot) {
				continue;
			}
//			else if (item instanceof DeathRayBlob) {
//				surface.setColor(Color.black);
//				drawDeathRay(surface, (DeathRayBlob) item, Color.white);
//				continue;
//			}
			
			Color textColor = null; //TODO This may change depending on the fill color/texture of the blob
			if (item instanceof PushBlob) {
				textColor = Color.white;
			} else if (item instanceof PullBlob) {
				textColor = Color.white;
			} else if (item instanceof DeathRayBlob) {
				textColor = Color.white;
			} else if (item instanceof ExplodingBlob) {
				textColor = Color.white;
			} else if (item instanceof SlipperyBlob) {
				textColor = Color.white;
			}
			//TODO Need to find better icons and way to differentiate between 2 players
			BufferedImage bi = ImageUtilities.getBufferedImage(item, this);
			Coordinate pos = item.getPosition();
			Ellipse2D blobToDraw = circle(pos.x, pos.y, item.getRadius());
			fillShapeWithImage(surface, blobToDraw, bi);
			
			if (item instanceof DeathRayBlob) {
				// draw barrel
				Coordinate facing = ((DeathRayBlob)item).getThetaCoordinate();
				Coordinate screenCenter = worldToScreen(item.getPosition());
				Coordinate screenFacing = worldToScreen(facing);
				Line2D barrel = new Line2D.Double(screenCenter.x, screenCenter.y, screenFacing.x, screenFacing.y);
				surface.setColor(Color.black);
				surface.setStroke(new BasicStroke(4));
				surface.draw(barrel);
			}
			
			surface.setStroke(new BasicStroke(3.0f));
			if (item.getOwner().getName().equals(_gameState.getPlayers().get(0).getName())) {
				surface.setColor(Color.red);
			} else {
				surface.setColor(Color.blue);
			}
			
			surface.draw(blobToDraw);
				
			surface.setColor(textColor);
			Coordinate stringScreenPos = worldToScreen(pos);
			double x = stringScreenPos.x - 16 / 2; // 16 = approximate width of string
			double y = stringScreenPos.y + 8 / 2; // 8 = approximate height of string
			surface.drawString(df.format(item.getRadius()*(5/GameParameters.BLOB_SIZE_LIMIT)), (float)x, (float)y);
			
			if (item instanceof Blob) {
				if (((Blob)item).isAnchored()) drawAnchoredIndicator(surface, (Blob)item);
				if (((Blob)item).isEnergized()) drawEnergizedIndicator(surface, (Blob)item);
			}
			
			if (_graphicalState.getSelectedBlob() == item) {
				surface.setColor(Color.orange);
				Ellipse2D selectionIndicator = circle(pos.x, pos.y, item.getRadius() + 0.8);
				surface.setStroke(new BasicStroke(2.0f));
				surface.draw(selectionIndicator);
			}
			
			if (_mousePosition != null) {
				turnAntiAliasingOff(surface);
				drawMoveCost(surface, worldToScreen(_mousePosition), _movementCost, true); // TODO check if able
			}
			turnAntiAliasingOn(surface);
		}
	}
	
	private void drawEnergizedIndicator(Graphics2D surface, Blob blob) {
		surface.setColor(Color.yellow);
		Ellipse2D selectionIndicator = circle(blob.getPosition().x, blob.getPosition().y, blob.getRadius() + 0.2);
		surface.setStroke(new BasicStroke(2.0f));
		surface.draw(selectionIndicator);
	}

	private void drawAnchoredIndicator(Graphics2D surface, Blob blob) {
		surface.setColor(Color.black);
		Ellipse2D selectionIndicator = circle(blob.getPosition().x, blob.getPosition().y, blob.getRadius() + 0.2);
		surface.setStroke(new BasicStroke(2.0f));
		surface.draw(selectionIndicator);
	}

	public Coordinate worldToScreen(Coordinate position) {
		return new Coordinate(position.x * getWidth() / GameParameters.BOARD_SIZE, 
		                      position.y * getHeight() / GameParameters.BOARD_SIZE);
	}
	
	public Coordinate screenToWorld(Coordinate screen) {
		return new Coordinate(screen.x * GameParameters.BOARD_SIZE / getWidth(), 
		                      screen.y * GameParameters.BOARD_SIZE / getHeight());
	}
	
	@SuppressWarnings("unused") // Disabled until we resolve framerate
	private void turnAntiAliasingOn(Graphics2D surface) {
		surface.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
	}
	
	@SuppressWarnings("unused") // Disabled until we resolve framerate
	private void turnAntiAliasingOff(Graphics2D surface) {
		surface.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
	}

	private void drawBoardBorder(Graphics2D surface) {
		surface.drawLine(0, 0, getWidth(), 0);
		surface.drawLine(getWidth(), 0, getWidth(), getHeight());
		surface.drawLine(getWidth(), getHeight(), 0, getHeight());
		surface.drawLine(0, getHeight(), 0, 0);
	}

	private void drawBackground(Graphics2D surface) {
		try {
			if (_backgroundUrl == null) {
				return;
			}
			
			// TODO Store these images somewhere when we first load them in Gui so we don't have to reload them.
			Image displayImage = Toolkit.getDefaultToolkit().getImage(_backgroundUrl);
			if (displayImage.getWidth(this) < 0) {
				// I don't know why this can happen, but it does when the board first loads, so bail out
				return;
			}
			BufferedImage bi = ImageUtilities.getBufferedImage(_backgroundUrl, this);

			Rectangle2D rectangle = new Rectangle2D.Float(0, 0, displayImage
					.getWidth(this), displayImage.getHeight(this));
			TexturePaint tp = new TexturePaint(bi, rectangle);

			surface.setPaint(tp);
			surface.fill(new Rectangle2D.Float(0, 0, getWidth(), getHeight()));			
		}
		catch (Exception e) {
			System.out.println("Error in drawBackground():");
			e.printStackTrace();
		}
	}
	
	private void drawAnchorPoint(Graphics2D surface, ImmovableGameObject item) {
		BufferedImage bi = ImageUtilities.getBufferedImage(item, this);
		Ellipse2D anchorPointToDraw = circle(item.getPosition().x, item.getPosition().y, GameParameters.BLOB_INITIAL_SIZE);
		fillShapeWithImage(surface, anchorPointToDraw, bi);

		//draw the outline
		surface.setStroke(new BasicStroke(3.0f));
		surface.draw(anchorPointToDraw);
		surface.setColor(Color.black);
	}

	private void drawAPCIPoint(Graphics2D surface, ImmovableGameObject item) {
		BufferedImage bi = ImageUtilities.getBufferedImage(item, this);
		Ellipse2D apciPointToDraw = circle(item.getPosition().x, item.getPosition().y, GameParameters.BLOB_INITIAL_SIZE);
		fillShapeWithImage(surface, apciPointToDraw, bi);
	}
	
	private void drawHole(Graphics2D surface, ImmovableGameObject item) {
		BufferedImage bi = ImageUtilities.getBufferedImage(item, this);
		Ellipse2D holeToDraw = circle(item.getPosition().x, item.getPosition().y, item.getRadius());
		fillShapeWithImage(surface, holeToDraw, bi);
	}
	
	private void drawSlipperySpot(Graphics2D surface, ImmovableGameObject item) {
		BufferedImage bi = ImageUtilities.getBufferedImage(item, this);
		Ellipse2D slipperySpotToDraw = circle(item.getPosition().x, item.getPosition().y, item.getRadius());
		fillShapeWithImage(surface, slipperySpotToDraw, bi);
	}
	
	private void fillShapeWithImage(Graphics2D surface, RectangularShape shape, BufferedImage bi) {
		TexturePaint paint = new TexturePaint(bi,shape.getFrame());
		surface.setPaint(paint);
		surface.fill(shape);
	}
	
	public void drawState(LocalGameState gameState)
	{
		_gameState = gameState;		
		// The board gets repainted constantly in RedrawThread
	}
	
	public void setBackground(URL backgroundUrl) {
		_backgroundUrl = backgroundUrl;
		repaint();
	}
	
	public void drawMoveCost(Graphics2D surface, Coordinate pos, double cost, boolean able) {
		DecimalFormat df = new DecimalFormat();
		df.setMinimumFractionDigits(1);
		df.setMaximumFractionDigits(1);
		if (able) surface.setColor(Color.BLACK);
		else surface.setColor(Color.RED);
		surface.setFont(new Font("Tahoma", Font.BOLD, 10));
		surface.drawString(df.format(cost), (float)(pos.x), (float)(pos.y));
	}

	/**
	 * Sets the cost that the board will draw at the given position in world coordinates.
	 * If mousePos is set to null, the board won't draw a cost.
	 */
	public void setMovementCost(Coordinate mousePos, double cost) {
		_movementCost = cost;
		_mousePosition = mousePos;
	}
}