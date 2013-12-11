package client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import server.WhiteboardServer;

public class WhiteboardClientThread extends Thread {
	private final Socket socket;
	private final int ID;
	private final WhiteboardClient client;
	private BufferedReader dataIn;
	private PrintWriter dataOut;
	
	public WhiteboardClientThread(Socket socket, WhiteboardClient client) {
		this.socket = socket;
		this.client = client;
		this.ID = WhiteboardServer.threadCounter.incrementAndGet();
		System.out.println(WhiteboardServer.threadCounter.get());
		//this.ID = Math.abs((new Random()).nextInt()); //TODO: check server for duplicates
	}
	
	@Override
	public void run() {
		System.out.println("Thread " + this.ID + " now running.");
		try {
			open();
			while (true) {
				client.handle(dataIn.readLine());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void open() throws IOException {
		this.dataIn = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		this.dataOut = new PrintWriter(socket.getOutputStream(), true);
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
		WhiteboardServer.threadCounter.decrementAndGet();
		System.out.println(WhiteboardServer.threadCounter.get());
	}
}
