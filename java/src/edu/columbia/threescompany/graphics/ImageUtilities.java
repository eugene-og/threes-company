package edu.columbia.threescompany.graphics;

import java.awt.Component;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.MediaTracker;
import java.awt.image.BufferedImage;
import java.net.URL;

public class ImageUtilities {

	/** Create Image from a file, then turn that into a BufferedImage.
	 */

	public static BufferedImage getBufferedImage(URL url, Component c) {
		Image image = c.getToolkit().getImage(url);
		waitForImage(image, c);
		BufferedImage bufferedImage = new BufferedImage(image.getWidth(c),
				image.getHeight(c), BufferedImage.TYPE_INT_RGB);
		Graphics2D g2d = bufferedImage.createGraphics();
		g2d.drawImage(image, 0, 0, c);
		return (bufferedImage);
	}
	public static BufferedImage getBufferedImage(String path, Component c) {
		URL url = path.getClass().getResource(path);
		return getBufferedImage(url, c);
	}

	/** Take an Image associated with a file, and wait until it is
	 *  done loading. Just a simple application of MediaTracker.
	 *  If you are loading multiple images, don't use this
	 *  consecutive times; instead use the version that takes
	 *  an array of images.
	 */

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