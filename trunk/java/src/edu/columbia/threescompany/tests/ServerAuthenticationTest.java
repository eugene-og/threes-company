package edu.columbia.threescompany.tests;

import java.util.ArrayList;
import java.util.List;

import edu.columbia.threescompany.client.BlobsClient;
import edu.columbia.threescompany.client.ChatThread;
import edu.columbia.threescompany.client.communication.ServerConnection;
import edu.columbia.threescompany.game.Player;
import edu.columbia.threescompany.server.BlobsServer;
import edu.columbia.threescompany.server.CommunicationConstants;

public class ServerAuthenticationTest extends BaseTestCase {
	public void testServerConnection() throws Exception {
		// FIXME must be the same as server's static config, because
		// server doesn't read args[] yet.
		final int TEST_PORT = 3444;

		Thread serverThread = new Thread(new Runnable() {
				public void run() {
					try {
						BlobsServer.main(new String[] { String.valueOf(TEST_PORT) });
					} catch (Exception exception) {
						throw new RuntimeException(exception);
					}
				};
		});
		
		serverThread.start();
		Thread.sleep(1000);	// FIXME dumb
		
		List<Player> players1 = new ArrayList<Player>();
		players1.add(new Player("Mike"));
		List<Player> players2 = new ArrayList<Player>();
		players2.add(new Player("Bill"));
		
		String[] args = new String[] {"localhost",String.valueOf(TEST_PORT)};
		
		ChatThread chatThread1 = new ChatThread(BlobsClient.authenticatePlayers(args, players1));
		ChatThread chatThread2 = new ChatThread(BlobsClient.authenticatePlayers(args, players2));
		
		new ServerConnection("localhost", TEST_PORT);
		new ServerConnection("localhost", TEST_PORT);
		
		chatThread1.sendLine(CommunicationConstants.READY);
		chatThread2.sendLine(CommunicationConstants.READY);
		
		Thread.sleep(2000);
		serverThread.stop();
		chatThread1.stopThread();
		chatThread2.stopThread();
	}
}
