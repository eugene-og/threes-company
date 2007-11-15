package edu.columbia.threescompany.tests;

import edu.columbia.threescompany.client.BlobsClient;
import edu.columbia.threescompany.server.BlobsServer;
import edu.columbia.threescompany.tests.BaseTestCase;

public class ServerClientAcceptanceTest extends BaseTestCase {
	public void testBasicFunctionality() throws Exception {		
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
		
		Thread clientThread = new Thread(new Runnable() {
			public void run() {
				try {
					BlobsClient.main(new String[] { String.valueOf(TEST_PORT) });
				} catch (Exception exception) {
					throw new RuntimeException(exception);
				}
			};
		});
		
		serverThread.run();
		clientThread.run();
		
		
	}
}
