package edu.columbia.threescompany.client.communication;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.List;

import edu.columbia.threescompany.client.LocalGameState;
import edu.columbia.threescompany.game.GameMove;
import edu.columbia.threescompany.game.Player;

public class ServerConnection {
	private Socket sock;
	public static final int DEFAULT_PORT = 3444;
	
	public ServerConnection() throws UnknownHostException, IOException {
		this("localhost", DEFAULT_PORT);
	}
	
	public ServerConnection(String hostName, int port)
	throws UnknownHostException, IOException {
		sock = new Socket(InetAddress.getByName(hostName), port);
		_ooStream = new ObjectOutputStream(sock.getOutputStream());
	}
	
	public ServerConnection(String host)
	throws UnknownHostException, IOException {
		this(host, DEFAULT_PORT);
	}

	/* Block until a message is received from the server. */
	public ServerMessage receiveMessage()
	throws IOException, ClassNotFoundException {
		// TODO make sure we block
		if (_oiStream == null)
			_oiStream = new ObjectInputStream(sock.getInputStream());
		ServerMessage msg = (ServerMessage) _oiStream.readObject();
		return msg;
	}

	public void sendMove(GameMove move, LocalGameState state) throws IOException {
		writeObject(new MoveStatePair(move, state));
	}

	private void writeObject(Serializable obj) throws IOException {
		_ooStream.writeObject(obj);
	}

	public void sendPlayers(List<Player> players) throws IOException {
		// This has to be the first step in the conn process
		Player[] array = new Player[players.size()];
		for (int i = 0; i < players.size(); i++)
			array[i] = players.get(i);
		writeObject(array);
	}
	
	/**
	 * Associates a chat thread connection with the given player(s). 
	 * @throws a PlayerDoesNotExistException if there isn't at least one player in the list which already exists in 
	 * the server's player list. 
	 */
	public void chatForPlayers(List<Player> players) throws PlayerDoesNotExistException {
		
	}
	
	/**
	 * Checks if the given Player's name is not in use. If not, adds it to the server's list of players. 
	 * Throws an exception if it's in use.
	 */
	public void addPlayer(Player player) throws PlayerNameInUseException, IOException {
		// TODO
	}
	
	private ObjectOutputStream _ooStream;
	private ObjectInputStream _oiStream;
}
