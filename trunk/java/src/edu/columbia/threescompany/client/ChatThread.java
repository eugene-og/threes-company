package edu.columbia.threescompany.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import edu.columbia.threescompany.client.communication.AuthenticationException;
import edu.columbia.threescompany.graphics.Gui;

public class ChatThread extends Thread {
	
	private Socket socket;
	private BufferedReader in;
	private PrintWriter out;
	private List<String> sendBuffer;
	private Gui _gui;

	/**
	 * @throws IOException, java.net.ConnectException
	 * @throws InterruptedException 
	 */
	
	public ChatThread(Object[] streams) throws IOException, AuthenticationException, InterruptedException {
		in = (BufferedReader) streams[0];
		out = (PrintWriter) streams[1];
		sendBuffer = new ArrayList<String>();
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
	                	for (String line : sendBuffer)
							out.println(line);
	                	sendBuffer.clear();
	                }
	                //if something was received
	                while (in.ready()) {
	                	str = in.readLine();
	                	_gui.addChatLine(str);
	                }
	            } catch (InterruptedException e){
	            	e.printStackTrace(System.out);
	            }
	        }

		} catch (IOException e) {
			e.printStackTrace(System.out);
		} catch (Exception e) {
			e.printStackTrace(System.out);
		} finally {
			try {
				socket.close();
			} catch (IOException e) {
				System.err.println("Socket not closed");
			}
		}
	}
	
	public void stopThread() {
		stop();
	}

	public void sendLine(String text) {
		sendBuffer.add(text);
	}

}
