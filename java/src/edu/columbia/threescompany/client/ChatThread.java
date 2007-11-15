package edu.columbia.threescompany.client;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import edu.columbia.threescompany.graphics.Gui;

public class ChatThread extends Thread {
	private static final int SERVER_PORT = 3444;		// FIXME
	
	private Socket socket;
	private BufferedReader in;
	private PrintWriter out;
	private List<String> sendBuffer;
	private Gui _gui;

	public ChatThread(List players) throws IOException {
		InetAddress addr;
		sendBuffer = new ArrayList<String>();
		try {
			addr = InetAddress.getByName("localhost");
		} catch (UnknownHostException e1) {
			System.err.println("Unknown host : " + e1);
			return;
		}
		
		socket = new Socket(addr, SERVER_PORT);
		ObjectOutputStream ooStream = new ObjectOutputStream(socket.getOutputStream());
		ooStream.writeObject(players.toArray());
		
		try {
			startStreams();
		} catch (IOException e) {
			try {
				socket.close();
			} catch (IOException e2) {
				System.err.println("Socket not closed");
			}
			throw(e);
		}
	}

	private void startStreams() throws IOException {
		in = new BufferedReader(new InputStreamReader(socket
				.getInputStream()));
		// Enable auto-flush:
		out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(
				socket.getOutputStream())), true);
	}
	
	public void setGui(Gui gui) {
		_gui = gui;
	}

	public void run() {
		String str = "";
		try {
			while (true) {
	            try {
	                Thread.sleep(500);
	                //if there is text to send
	                if (!sendBuffer.isEmpty()) {
	                	for (Iterator<String> iterator = sendBuffer.iterator(); iterator.hasNext();) {
							String line = (String) iterator.next();
							out.println(line);
						}
	                	sendBuffer.clear();
	                }
	                //if something was received
	                while (in.ready()) {
	                	str = in.readLine();
	                	_gui.addChatLine(str);
	                }
	            } catch (InterruptedException e){
	            }
	        }

		} catch (IOException e) {
			e.printStackTrace(System.err);
		} catch (Exception e) {
			e.printStackTrace(System.err);
		} finally {
			try {
				socket.close();
			} catch (IOException e) {
				System.err.println("Socket not closed");
			}
		}
	}

	public void sendLine(String text) {
		sendBuffer.add(text);
	}
}
