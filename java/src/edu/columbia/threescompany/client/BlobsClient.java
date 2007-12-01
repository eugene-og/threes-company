package edu.columbia.threescompany.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.ConnectException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.List;

import javax.swing.JOptionPane;

import edu.columbia.threescompany.client.communication.AuthenticationException;
import edu.columbia.threescompany.client.communication.AuthenticationObject;
import edu.columbia.threescompany.client.communication.ExecuteMoveMessage;
import edu.columbia.threescompany.client.communication.ServerConnection;
import edu.columbia.threescompany.client.communication.ServerMessage;
import edu.columbia.threescompany.client.communication.TurnChangeMessage;
import edu.columbia.threescompany.client.communication.UpdateStateMessage;
import edu.columbia.threescompany.game.GameMove;
import edu.columbia.threescompany.game.Player;
import edu.columbia.threescompany.graphics.Gui;
import edu.columbia.threescompany.graphics.PlayerInfoGui;
import edu.columbia.threescompany.graphics.PreGameGui;
import edu.columbia.threescompany.server.BlobsServer;
import edu.columbia.threescompany.server.CommunicationConstants;

public class BlobsClient {
	private static LocalGameState _gameState;
	private static ServerConnection _serverConnection;
	private static ChatThread _chatThread;
	private static Gui _gui;
	private static List<Player> _players;
	
	public static void main(String[] args) throws Exception {
		doPlayerSetup();
		if (args.length == 0)
			args = new String[] {PlayerInfoGui.getServerAddress(), PlayerInfoGui.getServerPort()};
		
		try {
			Object[] streams = authenticatePlayers(args);
			_chatThread = new ChatThread(streams);
		} catch (ConnectException e) {
				JOptionPane.showMessageDialog(null, "Blobs could not connect to the server. You need a server running " +
				                              "even for a hotseat game.");
				return;
		} catch (AuthenticationException e) {
			JOptionPane.showMessageDialog(null,e.getMessage());
			System.exit(0);
		}
		_gui = Gui.getInstance(_chatThread, _players);

		_chatThread.setGui(_gui);
		_chatThread.start();

		connectToServer(args);
		
		ServerMessage message;
		while ((message = _serverConnection.receiveMessage()) != null)
			handleMessage(message);
		
		gameOverDialog();
	}

	public static Object[] authenticatePlayers(String[] args, List<Player> players) throws NumberFormatException, AuthenticationException, IOException {
		_players = players;
		return authenticatePlayers(args);
	}
	
	private static Object[] authenticatePlayers(String[] args) throws AuthenticationException, NumberFormatException, IOException {
		InetAddress addr;
		try {
			addr = InetAddress.getByName(args[0]);
		} catch (UnknownHostException e1) {
			throw new AuthenticationException("Unknown host : " + e1);
		}
		
		Socket socket = new Socket(addr, Integer.valueOf(args[1]));
		Object[] streams = new Object[]{null,null};
		
		try {
			streams = startStreams(socket);
		} catch (IOException e) {
			try {
				socket.close();
			} catch (IOException e2) {
				System.err.println("Socket not closed");
			}
			throw(e);
		} catch (InterruptedException e) {}
		
		AuthenticationObject ao = new AuthenticationObject(_players.toArray(), true);
		ObjectOutputStream ooStream = new ObjectOutputStream(socket.getOutputStream());
		ooStream.writeObject(ao);
		
		BufferedReader in = (BufferedReader) streams[0];
		while (!in.ready()) {
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {}
		}
		String response = in.readLine();
     	if (!response.equals(CommunicationConstants.AUTHENTICATION_OK)) {
     		throw new AuthenticationException(response);
     	}
     	return streams;
	}
	
	private static Object[] startStreams(Socket socket) throws IOException, InterruptedException {
		BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		// Enable auto-flush:
		PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
		return new Object[] {in,out};
	}
	
	private static void connectToServer(String[] args) throws UnknownHostException, IOException {
		if (args.length == 2)
			_serverConnection = connectFromHostAndPort(args[0], args[1]);
		else if (args.length == 1)
			_serverConnection = connectFromHost(args[0]);
		else
			_serverConnection = new ServerConnection();

		_serverConnection.sendPlayers(_players, false);
	}

	private static ServerConnection connectFromHost(String host)
	throws IOException {
		return new ServerConnection(host);
	}

	private static ServerConnection connectFromHostAndPort(String host, String port)
	throws IOException {
		return new ServerConnection(host, Integer.valueOf(port));
	}

	private static void gameOverDialog() {
		JOptionPane.showMessageDialog(null, "You didn't lose, you just weren't the winner", 
											"Game over",
											JOptionPane.INFORMATION_MESSAGE );
	}

	private static void doPlayerSetup() {
		GameType gameType = PreGameGui.getGameType();
		
		if (gameType == GameType.HOTSEAT) {
			new Thread(new Runnable() {
				public void run() {
					try {
						BlobsServer.main(new String[0]);
					} catch (IOException e) {
						System.err.println("Embedded server crashed.");
						e.printStackTrace();
					}
				}
			}).start();
			_players = PlayerInfoGui.getPlayers(2);
		} else if (gameType == GameType.NETWORK) {
			_players = PlayerInfoGui.getPlayers(1);
		} else {
			throw new RuntimeException("Unknown game type!");
		}
	}
	
	private static void handleMessage(ServerMessage message) throws IOException {
		if (message instanceof UpdateStateMessage) {
			updateState(message);
		} else if (message instanceof TurnChangeMessage) {
			String activePlayer = ((TurnChangeMessage) message).whoseTurn();
			_gameState.updateActivePlayer(activePlayer);
			if (isLocalPlayer(activePlayer)) {
				yourMove(activePlayer);
			} else {
				notYourTurnDialog();
			}
		} else if (message instanceof ExecuteMoveMessage) {
			if (_players.size() > 1)
				throw new RuntimeException("Shouldn't be receiving ExecuteMoveMessages in hotseat play!");
			
			GameMove move = ((ExecuteMoveMessage) message).getMove();
			_gameState = ((ExecuteMoveMessage) message).getInitialState();
			System.err.println("Received move: " + move);
			_gameState.executeMove(move, _gui);
		}
	}

	private static boolean isLocalPlayer(String activePlayer) {
		for (Player player : _players)
		if (activePlayer.equals(player.getName())) return true;
		return false;
	}

	private static void updateState(ServerMessage message) {
		/* This should *never* differ from our state in simulation -- unless
		 * we are still waiting for our move to simulate. */
	
		if (_gameState == null)
			_gameState = ((UpdateStateMessage) message).getGameState();
		_gui.drawState(_gameState);
		
//		System.err.println("Received new state: " + _gameState);
//		if (! _gameState.equals(((UpdateStateMessage) message).getGameState()))
//			throw new RuntimeException("Incorrect game state received!");
	}

	private static void yourMove(String activePlayer) throws IOException {
		final GameMove move = new GameMove(_gui.getMoveFor(activePlayer));
		new Thread(new Runnable() {
			public void run() {
				try {
					_serverConnection.sendMove(move, _gameState);
				} catch (IOException e) {
					throw new RuntimeException(e);
				}
			}
		}).start();
		_gameState.executeMove(move, _gui);
	}

	private static void notYourTurnDialog() {
		JOptionPane.showMessageDialog(null, "Not your turn", 
											"Hold your horses!",
											JOptionPane.ERROR_MESSAGE );
	}

}
