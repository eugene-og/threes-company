package edu.columbia.threescompany.server;

import java.io.IOException;

import org.quickserver.net.server.ClientHandler;
import org.quickserver.net.server.QuickAuthenticator;


public class BlobsServerQuickAuthenticator extends QuickAuthenticator {
	public boolean askAuthorisation(ClientHandler clientHandler) throws IOException {
		BlobsGameState gameState = BlobsGameState.instance();
		String username = askStringInput(clientHandler, "User Name :");
		String password = askStringInput(clientHandler, "Password :"); 
		if(username==null || password ==null) {
			sendString(clientHandler, "Auth Failed - Username or Password empty");
			return false; 
		} else if (gameState.isHandleTaken(username)) {
			sendString(clientHandler, "Auth Failed - Handle in use");
			return false;
		} else if(username.equals(password) && !gameState.isHandleTaken(username)) {
			sendString(clientHandler, "Auth OK");
			PlayerServerData newPlayer = (PlayerServerData) clientHandler.getClientData();
			newPlayer.setHandle(username);
			newPlayer.setClient(clientHandler);
			newPlayer.setIsReadyToPlay(false);
			newPlayer.setHasTurn();
			gameState.addPlayer(newPlayer.getClientId(), newPlayer);
			return true;
		} else {
			sendString(clientHandler, "Auth Failed - Incorrect password");
			return false;
		}
	}
}