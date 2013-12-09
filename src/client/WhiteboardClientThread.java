package client;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Random;

public class WhiteboardClientThread {
	private final Socket socket;
	private final int ID;
	private final WhiteboardClient client;
	private DataInputStream dataIn;
	private DataOutputStream dataOut;
	
	public WhiteboardClientThread(Socket socket, WhiteboardClient client) {
		this.socket = socket;
		this.client = client;
		this.ID = Math.abs((new Random()).nextInt()); //TODO: check server for duplicates
	}
	
	public void run() throws IOException {
		System.out.println("Thread " + this.ID + " now running.");
		while (true) {
			//TODO: parse server message and update board
			client.handle(dataIn.readUTF());
		}
	}
	
	public void open() throws IOException {
		this.dataIn = new DataInputStream(socket.getInputStream());
		this.dataOut = new DataOutputStream(socket.getOutputStream());
	}
	
	public void close() throws IOException {
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
