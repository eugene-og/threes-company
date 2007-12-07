package edu.columbia.threescompany.server;

import java.io.File;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.List;

import org.quickserver.net.AppException;
import org.quickserver.net.server.ClientHandler;
import org.quickserver.net.server.QuickServer;

import edu.columbia.threescompany.client.LocalGameState;
import edu.columbia.threescompany.client.communication.ExecuteMoveMessage;
import edu.columbia.threescompany.client.communication.GameOverMessage;
import edu.columbia.threescompany.client.communication.MoveStatePair;
import edu.columbia.threescompany.client.communication.ServerMessage;
import edu.columbia.threescompany.client.communication.TurnChangeMessage;
import edu.columbia.threescompany.client.communication.UpdateStateMessage;
import edu.columbia.threescompany.game.GameMove;
import edu.columbia.threescompany.game.Player;
import edu.columbia.threescompany.sound.SoundEngine;

public class BlobsServer {
	public static String VERSION = "0.1";
	private static String _confFile = "conf" + File.separator + "BlobsServer.xml";
	
	private static List<Player> _playerList;
	
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
		} while (serverGameState.getPlayerCount() < 2);
		
		broadcastMessage(serverGameState, blobsServer, "Game Started!");
		mainServerLoop();
		broadcastMessage(serverGameState, blobsServer, "Game Over!");
	}

	private static void broadcastMessage(BlobsGameState gameState, QuickServer server, String msg) throws IOException {
		for (PlayerServerData toPlayer : gameState.getAllPlayerServerData()) {
			ClientHandler toHandler = toPlayer.getChatClientHandler();
			toHandler.sendClientMsg(msg);	
		}
	}
	
	private static void mainServerLoop() throws IOException {
		_playerList = getPlayers();
		LocalGameState gameState = LocalGameState.getInitialGameState(_playerList);
		sendStateToAllPlayers(gameState);
		
		int activePlayer = -1;
		while (!gameState.gameOver()) {
			activePlayer = (activePlayer + 1) % _playerList.size();
			// TODO do this better -- Zach
			Player player = _playerList.get(activePlayer);
			sendMessage(player.getName(), new TurnChangeMessage(player));

			MoveStatePair pair = receiveMoveAndState();
			int sent = sendMoveToAllPlayersExcept(player.getName(), pair._move, pair._state);

			//if (sent == 0) continue;
			gameState = pair._state;
			gameState.executeMove(pair._move, null);
			sendStateToAllPlayers(gameState);
		}
		sendGameOver();
		SoundEngine.play(SoundEngine.GAMEOVER);
	}

	private static void sendGameOver() throws IOException {
		sendMessageToAllPlayers(new GameOverMessage());
	}

	public static void sendStateToAllPlayers(LocalGameState gameState) throws IOException {
		sendMessageToAllPlayers(new UpdateStateMessage(gameState));
	}

	private static void sendMessageToAllPlayers(ServerMessage msg) throws IOException {
		for (Player player : getPlayers()) {
			sendMessage(player.getName(), msg);
		}
	}

	private static List<Player> getPlayers() {
		return BlobsGameState.instance().getAllPlayers();
	}
	
	public static int sendMoveToAllPlayersExcept(String playerId, GameMove move, LocalGameState initialState) throws IOException {
		int count = 0;
		ServerMessage msg = new ExecuteMoveMessage(move, initialState);
		List<Player> players = getPlayers();
		for (Player player : players) {
			List<String> playerNames = BlobsGameState.instance().getPlayerServerData(player.getName()).getHandlesList();
			if (!playerNames.contains(playerId)) {
				count++;
				sendMessage(player.getName(), msg);
			}
		}
		return count;
	}
	
	public static MoveStatePair receiveMoveAndState() {
		return MoveStatePairQueue.instance().blockForNextPair();
	}

	public static void sendMessage(String playerName, ServerMessage msg) throws IOException {
		ObjectOutputStream ooStream = getObjectOutputStreamFor(playerName);
		if (ooStream == null)
			throw new RuntimeException("Player " + playerName + "doesn't have an output stream!");
		ooStream.writeObject(msg);
	}

	private static ObjectOutputStream getObjectOutputStreamFor(String playerName) {
		ClientHandler handler = BlobsGameState.instance().getPlayerServerData(playerName).getGameClientHandler();
		return handler.getObjectOutputStream();
	}
}