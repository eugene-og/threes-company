package edu.columbia.threescompany.server;

import java.io.File;
import java.util.List;

import org.quickserver.net.AppException;
import org.quickserver.net.server.QuickServer;

import edu.columbia.threescompany.gameobjects.GameObject;

public class BlobsServer {
	public static String VERSION = "0.1";
	private static String _confFile = "conf" + File.separator + "BlobsServer.xml";
	
	public static List<GameObject> _blobs;
	
	public static void main(String args[])	{
		QuickServer blobsServer = new QuickServer();
		Object config[] = new Object[] { _confFile };
		if (blobsServer.initService(config) == true) {
			try {
				blobsServer.startServer();
				blobsServer.startQSAdminServer();
			} catch (AppException e) {
				System.err.println("Error in server : " + e);
				e.printStackTrace();
			}
		}
	}

	public static boolean gameOver() {
//		if (_blobs.size() == 0) return true;
//		Player firstPlayer = MainGameThread._blobs.get(0).getOwner();
//		
//		/* Do multiple players have blobs on the board? */
//		for (GameObject obj : MainGameThread._blobs)
//			if (obj.getOwner().id != Player.NOBODY && !obj.isDead() &&
//				!obj.getOwner().equals(firstPlayer))
//					return false;
//		
//		/* Nope, somebody won. */
//		return true;
		return false;
	}
}