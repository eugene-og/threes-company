package edu.columbia.threescompany.client;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import edu.columbia.threescompany.graphics.Gui;

public class ChatThread extends Thread {
	private Socket socket;
	private BufferedReader in;
	private PrintWriter out;
	private List<String> sendBuffer;

	public ChatThread() {
		InetAddress addr;
		sendBuffer = new ArrayList<String>();
		try {
			addr = InetAddress.getByName("localhost");
		} catch (UnknownHostException e1) {
			System.err.println("Unknown host : " + e1);
			return;
		}
		int port = 1000;
		try {
			socket = new Socket(addr, port);
		} catch (IOException e) {
			System.err.println("Socket failed : " + e);
			return;
		}

		try {
			in = new BufferedReader(new InputStreamReader(socket
					.getInputStream()));
			// Enable auto-flush:
			out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(
					socket.getOutputStream())), true);
			// authentication, random handles
			Random r = new Random();
			int randint = r.nextInt(1000);
			in.readLine();
			out.println("User"+Integer.toString(randint));
			in.readLine();
			out.println("User"+Integer.toString(randint));
			
			start();
		} catch (IOException e) {
			System.out.println("Socket Error : " + e);
			try {
				socket.close();
			} catch (IOException e2) {
				System.err.println("Socket not closed");
			}
		}
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
	                	Gui.getInstance().addChatLine(str);
	                }
	            } catch (InterruptedException e){
	            }
	        }

		} catch (IOException e) {
			System.err.println("IO Exception : " + e);
		} catch (Exception e) {
			System.err.println("Exception : " + e);
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
