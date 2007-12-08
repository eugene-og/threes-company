package edu.columbia.threescompany.server;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.List;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.UIManager;
import org.quickserver.net.AppException;
import org.quickserver.net.qsadmin.gui.QSAdminGUI;
import org.quickserver.net.qsadmin.gui.QSAdminMain;
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
	private QuickServer blobsServer;
	private static int _port = 3444;
	
	private static List<Player> _playerList;
	
	public static void main(String args[]) {		
		try {
			
			BlobsServer server = new BlobsServer();

            try {
				UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
			} catch (Exception e) {
				System.out.println("Non-fatal error setting look and feel.");
				e.printStackTrace();
			}
			QSAdminMain admin = new QSAdminMain();
			JFrame frame = new JFrame("Blobs Standalone Server");
			QSAdminGUI adminGui = new QSAdminGUI(admin, frame);
			frame.getContentPane().add(adminGui);
			frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			frame.pack();
			frame.setSize(700, 450);
			frame.setVisible(true);
			adminGui.updateConnectionStatus(false);
			JOptionPane.showMessageDialog(null, "Something's wrong here and this tool is not connecting to " +
					"the server. The server still works though.", "Broken", JOptionPane.WARNING_MESSAGE);
			//admin.doLogin("localhost", _port, "admin", "admin");
			//adminGui.updateConnectionStatus(true);
			
			server.run();
		} catch (Exception e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, e.toString(), "Error", JOptionPane.ERROR_MESSAGE);
			System.exit(1);
		}
	}
	
	public BlobsServer() throws IOException, AppException {
		blobsServer = new QuickServer("edu.columbia.threescompany.server.BlobsChatHandler");
		blobsServer.setName("BlobsServer");
		blobsServer.setBindAddr("0.0.0.0");
		blobsServer.setPort(_port);
		blobsServer.setClientData("edu.columbia.threescompany.server.PlayerServerData");
		blobsServer.setClientAuthenticationHandler("edu.columbia.threescompany.server.BlobsServerQuickAuthenticator");
		blobsServer.setClientObjectHandler("edu.columbia.threescompany.server.BlobsClientObjectHandler");
		blobsServer.setTimeout(-1);
		
		blobsServer.setQSAdminServerPort(4445);
		blobsServer.setServerBanner("QSAdminServer Started on port : 4445");
		
		/* DB: I switched from using a config file to hardcoded values because we couldn't load the config file nicely
		 * from in a jar. I translated most things to here, but couldn't figure out how the reproduce the piece below. 
		 * It seems to work without it.
		 * <command-shell>
		 *   <enable>true</enable>
		 * </command-shell>
		 * <communication-logging>
		 *   <enable>false</enable>
		 * </communication-logging>
		 */
		
		blobsServer.startServer();
	}
	
	public void run() throws IOException {
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

	private void broadcastMessage(BlobsGameState gameState, QuickServer server, String msg) throws IOException {
		for (PlayerServerData toPlayer : gameState.getAllPlayerServerData()) {
			ClientHandler toHandler = toPlayer.getChatClientHandler();
			toHandler.sendClientMsg(msg);	
		}
	}
	
	private void mainServerLoop() throws IOException {
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

	private void sendGameOver() throws IOException {
		sendMessageToAllPlayers(new GameOverMessage());
	}

	public void sendStateToAllPlayers(LocalGameState gameState) throws IOException {
		sendMessageToAllPlayers(new UpdateStateMessage(gameState));
	}

	private void sendMessageToAllPlayers(ServerMessage msg) throws IOException {
		for (Player player : getPlayers()) {
			sendMessage(player.getName(), msg);
		}
	}

	private List<Player> getPlayers() {
		return BlobsGameState.instance().getAllPlayers();
	}
	
	public int sendMoveToAllPlayersExcept(String playerId, GameMove move, LocalGameState initialState) throws IOException {
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
	
	public MoveStatePair receiveMoveAndState() {
		return MoveStatePairQueue.instance().blockForNextPair();
	}

	public void sendMessage(String playerName, ServerMessage msg) throws IOException {
		ObjectOutputStream ooStream = getObjectOutputStreamFor(playerName);
		if (ooStream == null)
			throw new RuntimeException("Player " + playerName + "doesn't have an output stream!");
		ooStream.writeObject(msg);
	}

	private ObjectOutputStream getObjectOutputStreamFor(String playerName) {
		ClientHandler handler = BlobsGameState.instance().getPlayerServerData(playerName).getGameClientHandler();
		return handler.getObjectOutputStream();
	}
}