package edu.columbia.threescompany.client.communication;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

import edu.columbia.threescompany.client.LocalGameState;
import edu.columbia.threescompany.game.GameMove;

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
		// TODO this classcast is fucked
		ObjectInputStream stream = (ObjectInputStream) sock.getInputStream();
		ServerMessage msg = (ServerMessage) stream.readObject();

		return msg;
	}

	public void sendMove(GameMove move, LocalGameState state) throws IOException {
		// TODO this classcast is also fucked
		ObjectOutputStream stream = (ObjectOutputStream) sock.getOutputStream();
		stream.writeObject(move);
	}
}
