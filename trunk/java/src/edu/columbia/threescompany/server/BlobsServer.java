package edu.columbia.threescompany.server;

import java.io.File;

import org.quickserver.net.AppException;
import org.quickserver.net.server.QuickServer;

public class BlobsServer {
	
	public static String VERSION = "0.1";
	
	public static void main(String args[])	{
		QuickServer blobsServer = new QuickServer();
		String confFile = "conf" + File.separator + "BlobsServer.xml";
		Object config[] = new Object[] { confFile };
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
}