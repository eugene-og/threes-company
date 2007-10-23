package edu.columbia.threescompany.server;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.Iterator;

import org.quickserver.net.server.ClientCommandHandler;
import org.quickserver.net.server.ClientHandler;

public class BlobsCommandHandler implements ClientCommandHandler {
	
	public void lostConnection(ClientHandler handler) throws IOException {
		handler.sendSystemMsg("Connection lost : "+handler.getHostAddress());
		BlobsGameState.instance().removePlayer(handler.getClientData());
	}
	
	public void closingConnection(ClientHandler handler) throws IOException {
		handler.sendSystemMsg("Connection closed: "+handler.getHostAddress());
		BlobsGameState.instance().removePlayer(handler.getClientData());
	}
	
	public void handleCommand(ClientHandler handler, String command) 
			throws SocketTimeoutException, IOException {
		BlobsGameState gameState = BlobsGameState.instance();
		if(command.equals("quit")) {
			handler.sendClientMsg("bye");
			handler.closeConnection();
		} else if (command.equals("ready")) {
			((PlayerServerData) handler.getClientData()).setIsReadyToPlay(true);
		} else if (command.equals("not ready")) {
			((PlayerServerData) handler.getClientData()).setIsReadyToPlay(false);
		} else if (command.equals("status")) {
			sendPlayersReadyStatus(gameState, handler);
		} else {
			broadcastMessage(handler, ((PlayerServerData)handler.getClientData()).getHandle() + ": " + command);
		}
		
		/* check if all players are ready */
		if (gameState.allPlayersReady()) {
			broadcastGameStart(handler);
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
	
	private void broadcastGameStart(ClientHandler handler) throws IOException {
		for (Iterator iterator = handler.getServer().findAllClient(); iterator.hasNext();) {
			ClientHandler toHandler = (ClientHandler) iterator.next();
			toHandler.sendClientMsg("START GAME!!");	
		}
	}
	
	private void broadcastMessage(ClientHandler handler, String msg) throws IOException {
		for (Iterator iterator = handler.getServer().findAllClient(); iterator.hasNext();) {
			ClientHandler toHandler = (ClientHandler) iterator.next();
			if (!toHandler.equals(handler)) {
				toHandler.sendClientMsg(msg);
			}
		}
	}
		
}
