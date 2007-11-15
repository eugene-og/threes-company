package edu.columbia.threescompany.client.communication;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.collections.ListUtils;

import edu.columbia.threescompany.client.LocalGameState;
import edu.columbia.threescompany.game.GameMove;
import edu.columbia.threescompany.game.Player;

public class ServerConnection {
	private Socket sock;
	
	public ServerConnection() throws UnknownHostException, IOException {
		/* FIXME: temporary, since port not known 
		 * get from cmdline later */
		this(3444);
	}
	
	public ServerConnection(int port) throws UnknownHostException, IOException {
		sock = new Socket(InetAddress.getLocalHost(), port);
	}
	
	/* Block until a message is received from the server. */
	public ServerMessage receiveMessage() throws IOException, ClassNotFoundException {
		// TODO make sure we block
		ObjectInputStream stream = new ObjectInputStream(sock.getInputStream());
		ServerMessage msg = (ServerMessage) stream.readObject();

		return msg;
	}

	public void sendMove(GameMove move, LocalGameState state) throws IOException {
		writeObject(move);
	}

	private void writeObject(Serializable obj) throws IOException {
		ObjectOutputStream stream = new ObjectOutputStream(sock.getOutputStream());
		stream.writeObject(obj);
	}

	public void sendPlayers(List<Player> players) throws IOException {
		// This has to be the first step in the conn process
		Player[] array = new Player[players.size()];
		for (int i = 0; i < players.size(); i++)
			array[i] = players.get(i);
		writeObject(array);
	}
}
