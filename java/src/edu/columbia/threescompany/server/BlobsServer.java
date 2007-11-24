package edu.columbia.threescompany.server;

import java.io.File;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.quickserver.net.AppException;
import org.quickserver.net.server.ClientHandler;
import org.quickserver.net.server.QuickServer;

import edu.columbia.threescompany.client.LocalGameState;
import edu.columbia.threescompany.client.communication.ExecuteMoveMessage;
import edu.columbia.threescompany.client.communication.MoveStatePair;
import edu.columbia.threescompany.client.communication.ServerMessage;
import edu.columbia.threescompany.client.communication.TurnChangeMessage;
import edu.columbia.threescompany.client.communication.UpdateStateMessage;
import edu.columbia.threescompany.game.GameMove;
import edu.columbia.threescompany.game.Player;

public class BlobsServer {
	public static String VERSION = "0.1";
	private static String _confFile = "conf" + File.separator + "BlobsServer.xml";
	
	private static List<PlayerServerData> _playerServerDataList;
	
	public static void main(String args[]) throws IOException {
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
		
		BlobsGameState serverGameState = BlobsGameState.instance();
		do {
			try {
				Thread.sleep(100);
			} catch (InterruptedException exception) {
				/* nada */
			}
		} while (!serverGameState.allPlayersReady());
		
		broadcastGameStart(serverGameState, blobsServer);
		mainServerLoop();
	}

	private static void broadcastGameStart(BlobsGameState gameState, QuickServer server) throws IOException {
		for (Iterator iterator = gameState.getAllPlayers().iterator(); iterator.hasNext();) {
			PlayerServerData toPlayer = (PlayerServerData) iterator.next();
			ClientHandler toHandler = toPlayer.getChatClientHandler();
			toHandler.sendClientMsg("START GAME!!");	
		}
	}
	
	private static void mainServerLoop() throws IOException {
		_playerServerDataList = getPlayers();
		LocalGameState gameState = LocalGameState.getInitialGameState(playersFrom(_playerServerDataList));
		sendStateToAllPlayers(gameState);
		
		int activePlayer = -1;
		while (!gameState.gameOver()) {
//			try {
//				Thread.sleep(5000);
//			} catch (InterruptedException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
			activePlayer = (activePlayer + 1) % _playerServerDataList.size();
			// TODO do this better -- Zach
			String playerId = _playerServerDataList.get(activePlayer).getClientId();
			sendMessage(playerId, new TurnChangeMessage(playerId));

			MoveStatePair pair = receiveMoveAndState(playerId);
			sendMoveToAllPlayersExcept(playerId, pair._move);

			gameState = pair._state;
			sendStateToAllPlayers(gameState);
		}
	}
	
	private static List<Player> playersFrom(List<PlayerServerData> playerServerDataList) {
		List<Player> result = new ArrayList<Player>();
		for (PlayerServerData playerServerData : playerServerDataList)
			result.add(playerServerData.getPlayer());
		return result;
	}

	public static void sendStateToAllPlayers(LocalGameState gameState) throws IOException {
		ServerMessage msg = new UpdateStateMessage(gameState);
		List<PlayerServerData> players = getPlayers();
		for (PlayerServerData player : players) {
			sendMessage(player.getClientId(), msg);
		}
	}

	private static List<PlayerServerData> getPlayers() {
		return BlobsGameState.instance().getAllPlayers();
	}
	
	public static void sendMoveToAllPlayersExcept(String playerId, GameMove move) throws IOException {
		ServerMessage msg = new ExecuteMoveMessage(move);
		List<PlayerServerData> players = getPlayers();
		for (PlayerServerData player : players) {
			String id = player.getClientId();
			if (!id.equals(playerId)) sendMessage(id, msg);
		}
	}
	
	public static MoveStatePair receiveMoveAndState(String playerId) {
		return MoveStatePairQueue.instance().blockForNextPair();
	}

	public static void sendMessage(String playerId, ServerMessage msg) throws IOException {
		ObjectOutputStream ooStream = getObjectOutputStreamFor(playerId);
		if (ooStream == null)
			throw new RuntimeException("Player " + playerId + "doesn't have an output stream!");
		ooStream.writeObject(msg);
	}

	private static ObjectOutputStream getObjectOutputStreamFor(String playerId) {
		ClientHandler handler = BlobsGameState.instance().getPlayerServerData(playerId).getGameClientHandler();
		return handler.getObjectOutputStream();
	}
}