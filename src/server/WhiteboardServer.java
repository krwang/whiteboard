package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ConcurrentHashMap;

import client.Canvas;

public class WhiteboardServer {
	public static final int SERVER_PORT = 5050;

	private ConcurrentHashMap<String,ArrayList<String>> canvasMovesMap;
	private ConcurrentHashMap<String,ArrayList<Socket>> sockets;
	private ConcurrentHashMap<String, ArrayList<String>> usersOnCanvas;
	private final ArrayBlockingQueue<Object[]> queue;
	private final Thread thread;
	private ServerSocket serverSocket;
	/**
	 * Make a WhiteboardServer that listens for connections on port.
	 * It also takes commands put in the queue and sends the output
	 * of them back to the client
	 * @param port port number, requires 0 <= port <= 65535.
	 */
	public WhiteboardServer(int port) throws IOException{
		System.out.println("server started in port " + port);
		serverSocket = new ServerSocket(port);

		canvasMovesMap = new ConcurrentHashMap<String, ArrayList<String>>();
		sockets = new ConcurrentHashMap<String,ArrayList<Socket>>();
		usersOnCanvas = new ConcurrentHashMap<String, ArrayList<String>>();
		queue = new ArrayBlockingQueue<Object[]>(1000);

		thread = new Thread(new Runnable(){

			@Override
			public void run() {
				while(true){
					try{
						sendOutput(queue.take());
					}catch(Exception e){
						e.printStackTrace();
					}
				}

			}

		});
		thread.start();
	}

	/**
	 * Run the server, listening for client connections and handling them.  
	 * Never returns unless an exception is thrown.
	 * @throws IOException if the main server socket is broken
	 * (IOExceptions from individual clients do *not* terminate serve()).
	 * 
	 */
	public void serve() throws IOException{
		while(true){
			final Socket socket = serverSocket.accept();
			//creating a new thread for each client
			Thread thread = new Thread(new Runnable() {
				public void run() {
					try {
						handle(socket);
					} catch(Exception e) {
						e.printStackTrace();
					} finally {
						try {
							socket.close();
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				}
			});
			thread.start();
		}
	}

	/**
	 * Handles a single client connection and puts all client inputs 
	 * into the queue.
	 * 
	 * TODO: EntryGUI communication
	 * 
	 * @param socket     socket through which the client is connected
	 * @throws IOException
	 * @throws InterruptedException
	 */
	private void handle(Socket socket) throws IOException, InterruptedException{
		System.out.println("client connected");
		System.out.println("USERS: " + usersOnCanvas);
		try{
			BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			PrintWriter out = new PrintWriter(socket.getOutputStream(),true);
			try {
				// each request from the client is whiteboardName
				for (String line = in.readLine(); line != null; line = in.readLine()) {
					System.err.println("request: " + line);

					queue.put(new Object[]{line, socket, out});
				}
			} finally {
				out.close();
				in.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Is called when an update is retrieved and removed from the queue,
	 * calls handleRequest on the update and depending on what is returned
	 * by handleRequest, sends necessary output back to clients
	 * 
	 * handleRequest returns a String array of the form [sendAll, output,
	 * filename]:
	 * 		sendAll - true if output should be sent to all sockets reading
	 * from or writing to that file, false if output should only be sent to
	 * the current socket
	 * 		output - output String to be sent to sockets, but if output is
	 * "bye," then the socket is removed from the ArrayList of sockets
	 * mapped to that File
	 * 		fileName - name of file currently being edited
	 * 
	 * @param obj
	 * @throws IOException
	 */
	@SuppressWarnings("unchecked")
	public void sendOutput(Object[] obj) throws IOException {
		System.out.println("send output");
		String input = (String)obj[0];
		Socket socket = (Socket)obj[1];
		PrintWriter out = (PrintWriter)obj[2];

		Object[] outputParsed = handleRequest(input, socket);
		for (Object object: outputParsed) {
			System.out.println("outputParsed: " + object);
		}
		ArrayList<String> output = (ArrayList<String>)outputParsed[0];
		System.out.println(output);
		String boardName = "";
		if (outputParsed.length > 1) {
			boardName = (String)outputParsed[1];
		}

		if (!output.isEmpty()) {
			if (output.equals("username good") || output.equals("contains") || output.equals("unavailable")) {
				System.out.println("username: " + output);
				out.println(output);
			}
			else {
				for (String str: output) {
					System.out.println("String: " + str);
					out.println(str);
				}
			}
		}
		else {
			out.println("");
		}
		
		out.println("endinit");
		String[] tokens = input.split(" ");

		if (tokens[0].equals("bye")) {
			String userName = tokens[2];
			
			ArrayList<Socket> connected = sockets.get(boardName);
			System.out.println("board name: " + boardName);
			for (Socket otherSocket: connected) {
				PrintWriter socketOut = new PrintWriter(otherSocket.getOutputStream(), true);
				socketOut.println(input);
			}
		
			System.out.println("board sockets " + connected);
			connected.remove(socket);
			usersOnCanvas.get(boardName).remove(userName);
		} else if (tokens[0].equals("add")) {
			String username = tokens[2];
			System.out.println("add " + username);            
			ArrayList<Socket> connected = sockets.get(boardName);
			for(Socket otherSocket: connected) {
				PrintWriter socketOut = new PrintWriter(otherSocket.getOutputStream(),true);
		        for (String user: usersOnCanvas.get(boardName)) {
		        	socketOut.println("add " + boardName + " " + user);
	            	System.out.println("sending add command: " + "add " + boardName + " " + user);
		        }

			}
		} else if (tokens[0].equals("draw")) {
			ArrayList<Socket> connected = sockets.get(boardName);
			System.out.println("draw socket boardname " + boardName);
			for(Socket otherSocket: connected) {
				PrintWriter socketOut = new PrintWriter(otherSocket.getOutputStream(),true);
				socketOut.println(input);
				System.out.println("sending draw command: " + input);
			}
		}
	}

	/**
	 * open takes in whiteboard name and username
	 * boardName must be valid already when this is called
	 * handles the client's input and returns the corresponding output
	 * puts out moves, boardname
	 * @param input
	 * @return
	 */
	private Object[] handleRequest(String input, Socket socket) {
		System.out.println("handleRequest");
		System.out.println("input: " + input);
		String regex = "(add \\w+ \\w+)|(draw \\d+ \\d+ \\d+ \\d+ \\d+ \\d+ \\w+)|(bye \\w+ \\w+)|"
				+ "(username \\w+ \\w+)";
	
		if ( ! input.matches(regex)) {
			// invalid input
			return null;
		}

		String[] tokens = input.split(" ");

		if(tokens[0].equals("add")) {
			/*
			 * If the whiteboard already exists, then the new socket gets added
			 * to the hashmap mapping whiteboard names to sockets
			 * 
			 * If the whiteboard does not exist, then the whiteboard gets added
			 * to the hashmap of whiteboard names to canvases. Also, the client
			 * socket will be added to hashmap of whiteboard names to sockets
			 */
			System.out.println("add");
			String boardName = tokens[1];
			String userName = tokens[2];

			System.out.println("add boardName " + boardName);

			//add socket to boardname
			ArrayList<Socket> socketValue = new ArrayList<Socket>();
			socketValue.add(socket);
			sockets.putIfAbsent(boardName, socketValue);

			ArrayList<Socket> priorSocketValue = sockets.get(boardName);
			if (!priorSocketValue.contains(socket)) {
				priorSocketValue.add(socket);
				sockets.put(boardName, priorSocketValue);
			}

			canvasMovesMap.putIfAbsent(boardName, new ArrayList<String>());

			//sends over moves from existing canvas
			ArrayList<String> canvasMoves = canvasMovesMap.get(boardName);
			if (!userName.equals("")) {
				ArrayList<String> users = new ArrayList<String>();
				users.add(userName);
				usersOnCanvas.putIfAbsent(boardName, users);

				ArrayList<String> currentUsers = usersOnCanvas.get(boardName);
				if (!currentUsers.contains(userName)) {
					currentUsers.add(userName);
					usersOnCanvas.put(boardName, currentUsers);
				}
			}

			return new Object[]{canvasMoves, boardName, userName};
		} else if(tokens[0].equals("draw")) {
			/*
			 * draws the draw input from the client onto the master copy of the
			 * whiteboard that the client is referencing to update the master copy
			 */

			System.out.println("draw");
			int color = Integer.parseInt(tokens[1]);//<--will be the color represented as an int
			int size = Integer.parseInt(tokens[2]);//size represented as an int
			int x1 = Integer.parseInt(tokens[3]);
			int y1 = Integer.parseInt(tokens[4]);
			int x2 = Integer.parseInt(tokens[5]);
			int y2 = Integer.parseInt(tokens[6]);
			System.out.println("draw color " + color);
			System.out.println("draw size " + size);
			System.out.println("draw x1 " + x1);
			System.out.println("draw y1 " + y1);
			System.out.println("draw x2 " + x2);
			System.out.println("draw y2 " + y2);

			String boardName = tokens[7];

			System.out.println("boardName " + boardName);

			ArrayList<String> pastCanvasMoves = canvasMovesMap.get(boardName);
			pastCanvasMoves.add(input);
			canvasMovesMap.put(boardName, pastCanvasMoves);

			return new Object[]{pastCanvasMoves, boardName};

		} else if(tokens[0].equals("bye")) {
			System.out.println("bye");
			//			String userName = tokens[1];
			String boardName = tokens[1];
			String username = tokens[2];
			usersOnCanvas.get(boardName).remove(username);
			sockets.get(boardName).remove(socket);
			ArrayList<String> output = new ArrayList<String>();
			output.add(input);
			return new Object[]{output, boardName};

		} else if(tokens[0].equals("username")){
			System.out.println("username");
			String boardName = tokens[1];
			String username = tokens[2];
			ArrayList<String> output = new ArrayList<String>();
			System.out.println("usersonvancas " + usersOnCanvas);
			System.out.println(usersOnCanvas.get(boardName));
			System.out.println("username "+ username);

			if(usersOnCanvas.get(boardName) != null) {
				if (usersOnCanvas.get(boardName).contains(username)){
					output.add("contains");
					return new Object[]{output};
				}
			}
			output.add("username good");
			return new Object[]{output};
		}
		else {
			throw new UnsupportedOperationException();
		}		
	}

	/**
	 * Start a WhiteboardServer running on the default port.
	 */
	public static void main(String[] args) {
		try {
			WhiteboardServer server = new WhiteboardServer(SERVER_PORT);
			server.serve();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
