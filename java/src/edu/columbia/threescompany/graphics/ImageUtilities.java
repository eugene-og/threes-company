package edu.columbia.threescompany.graphics;

import java.awt.Component;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.MediaTracker;
import java.awt.image.BufferedImage;
import java.net.URL;
import java.util.HashMap;

import edu.columbia.threescompany.gameobjects.APCIPoint;
import edu.columbia.threescompany.gameobjects.AnchorPoint;
import edu.columbia.threescompany.gameobjects.DeathRayBlob;
import edu.columbia.threescompany.gameobjects.ExplodingBlob;
import edu.columbia.threescompany.gameobjects.GameObject;
import edu.columbia.threescompany.gameobjects.Hole;
import edu.columbia.threescompany.gameobjects.PullBlob;
import edu.columbia.threescompany.gameobjects.PushBlob;
import edu.columbia.threescompany.gameobjects.SlipperyBlob;
import edu.columbia.threescompany.gameobjects.SlipperySpot;

public class ImageUtilities {
	
	protected static HashMap<Class<? extends GameObject>, String> imageMap = new HashMap<Class<? extends GameObject>, String>();
	
	public static BufferedImage getBufferedImage(URL url, Component c) {
		Image image = c.getToolkit().getImage(url);
		waitForImage(image, c);
		BufferedImage bufferedImage = new BufferedImage(image.getWidth(c),
				image.getHeight(c), BufferedImage.TYPE_INT_RGB);
		Graphics2D g2d = bufferedImage.createGraphics();
		g2d.drawImage(image, 0, 0, c);
		return (bufferedImage);
	}
	
	public static BufferedImage getBufferedImage(GameObject gameObject, Component c) {
		Class<? extends GameObject> gameObjectType = gameObject.getClass();
		return getBufferedImage(getImageURLForType(gameObjectType), c);
	}

	private static URL getImageURLForType(Class<? extends GameObject> gameObjectClass) {
		//if not initialized, initialize image map
		if (imageMap.isEmpty()) {
			imageMap.put(PushBlob.class, GuiConstants.PUSH_BLOB_IMAGE);
			imageMap.put(PullBlob.class, GuiConstants.PULL_BLOB_IMAGE);
			imageMap.put(ExplodingBlob.class, GuiConstants.EXPLODING_BLOB_IMAGE);
			imageMap.put(SlipperyBlob.class, GuiConstants.SLIPPERY_BLOB_IMAGE);
			imageMap.put(DeathRayBlob.class, GuiConstants.DEATHRAY_BLOB_IMAGE);
			imageMap.put(AnchorPoint.class, GuiConstants.ANCHOR_POINT_IMAGE);
			imageMap.put(APCIPoint.class, GuiConstants.APCI_POINT_IMAGE);
			imageMap.put(Hole.class, GuiConstants.HOLE_IMAGE);
			imageMap.put(SlipperySpot.class, GuiConstants.SLIPPERY_SPOT_IMAGE);
		}
		return gameObjectClass.getResource("/icons/"+imageMap.get(gameObjectClass));
	}
	
	private static boolean waitForImage(Image image, Component c) {
		MediaTracker tracker = new MediaTracker(c);
		tracker.addImage(image, 0);
		try {
			tracker.waitForAll();
		} catch (InterruptedException ie) {
		}
		return (!tracker.isErrorAny());
	}

}