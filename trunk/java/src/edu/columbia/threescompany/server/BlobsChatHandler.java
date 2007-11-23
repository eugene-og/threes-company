package edu.columbia.threescompany.server;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.Iterator;

import org.quickserver.net.server.ClientCommandHandler;
import org.quickserver.net.server.ClientHandler;

public class BlobsChatHandler implements ClientCommandHandler {
	
	public void lostConnection(ClientHandler handler) throws IOException {
		handler.sendSystemMsg("Connection lost : "+handler.getHostAddress());
		BlobsGameState.instance().removePlayerServerData(handler.getClientData());
	}
	
	public void closingConnection(ClientHandler handler) throws IOException {
		handler.sendSystemMsg("Connection closed: "+handler.getHostAddress());
		BlobsGameState.instance().removePlayerServerData(handler.getClientData());
	}
	
	public void handleCommand(ClientHandler handler, String msg) 
			throws SocketTimeoutException, IOException {
		BlobsGameState gameState = BlobsGameState.instance();
		
		if (msg.startsWith("$")) {
			doCommand(gameState, handler, msg);
		} else {
			broadcastMessage(handler, gameState, ((PlayerServerData)handler.getClientData()).getHandle() + ": " + msg);
		}
	}

	private void doCommand(BlobsGameState gameState, ClientHandler handler, String command) throws IOException {
		if (command.equals("$ready")) {
			((PlayerServerData) handler.getClientData()).setIsReadyToPlay(true);
		} else if (command.equals("$not ready")) {
			((PlayerServerData) handler.getClientData()).setIsReadyToPlay(false);
		} else if (command.equals("$status")) {
			sendPlayersReadyStatus(gameState, handler);
		} else {
			handler.sendClientMsg("Invalid command.");
		}
		
	}

	private void sendPlayersReadyStatus(BlobsGameState gameState, ClientHandler handler) throws IOException {
		String msg = "";
		for (Iterator<PlayerServerData> iterator = gameState.getAllPlayers().iterator(); iterator.hasNext();) {
			PlayerServerData currentPlayer = (PlayerServerData) iterator.next();
			msg += currentPlayer.getHandle() + " : " + String.valueOf(currentPlayer.isReadyToPlay());
		}
		handler.sendClientMsg(msg);
	}
	
	private void broadcastMessage(ClientHandler handler, BlobsGameState gameState, String msg) throws IOException {
		for (Iterator iterator = gameState.getAllPlayers().iterator(); iterator.hasNext();) {
			PlayerServerData toPlayer = (PlayerServerData) iterator.next();
			ClientHandler toHandler = toPlayer.getChatClientHandler();
			if (!toHandler.equals(handler)) {
				toHandler.sendClientMsg(msg);
			}
		}
	}
		
}
