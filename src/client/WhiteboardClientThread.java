package client;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Random;

import server.WhiteboardServer;

public class WhiteboardClientThread {
	private final Socket socket;
	private final WhiteboardServer server;
	private final int ID;
	private DataInputStream dataIn;
	private DataOutputStream dataOut;
	
	public WhiteboardClientThread(Socket socket, WhiteboardServer server) {
		this.socket = socket;
		this.server = server;
		this.ID = Math.abs((new Random()).nextInt());
	}
	
	public void runSocket() throws IOException {
		System.out.println("Thread " + this.ID + " now running.");
		while (true) {
			//TODO: parse server message and update board
		}
	}
	
	public void openSocket() throws IOException {
		this.dataIn = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
		this.dataOut = new DataOutputStream(new BufferedOutputStream(socket.getOutputStream()));
	}
	
	public void closeSocket() throws IOException {
		if (this.socket != null) {
			this.socket.close();
		}
		if (this.dataIn != null) {
			this.dataIn.close();
		}
		if (this.dataOut != null) {
			this.dataOut.close();
		}
	}
}
