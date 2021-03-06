package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * This class contains code for a WhiteboardServer.
 * A WhiteboardServer enables users to edit the same canvas across
 * a network. It will store actions performed on each canvas opened
 * on the server and will keep these changes as long as the server 
 * is running.
 * 
 * Thread Safety - WhiteboardServer is threadsafe for the following reasons:
 * 
 * 1. There cannot be any deadlocks because WhiteboardClients are not dependent on other WhiteboardClients
 * 	  and no WhiteboardClients are dependent on the server or other classes in the package
 * 2. Race conditions between WhiteboardClients sending actions on the canvas are allowed to occur naturally
 * 	  This will only affect the order in which the actions are placed on the server action queue, and this
 * 	  order will be reflected for all clients, regardless of what the local client version displayed.
 * 3. Interleaving is protected against in the server action queue. All local client canvases will be updated
 * 	  in the order that the server queue receives the action, so all local versions will display
 * 	  the same image.
 * 
 * For the testing strategy, see WhiteboardServerTest.java
 */
public class WhiteboardServer {
	public static final int SERVER_PORT = 5050;
	public static AtomicInteger threadCounter = new AtomicInteger(0);

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
		serverSocket = new ServerSocket(port);

		canvasMovesMap = new ConcurrentHashMap<String, ArrayList<String>>();
		sockets = new ConcurrentHashMap<String,ArrayList<Socket>>();
		usersOnCanvas = new ConcurrentHashMap<String, ArrayList<String>>();
		queue = new ArrayBlockingQueue<Object[]>(1000);

		thread = new Thread(new Runnable() {
			@Override
			public void run() {
				while (true) {
					try {
						sendOutput(queue.take());
					} catch(Exception e) {
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
	 * @param socket is the socket through which the client is connected
	 * @throws IOException
	 * @throws InterruptedException
	 */
	private void handle(Socket socket) throws IOException, InterruptedException{
		try{
			BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			PrintWriter out = new PrintWriter(socket.getOutputStream(),true);
			try {
				// each request from the client is whiteboardName
				for (String line = in.readLine(); line != null; line = in.readLine()) {
					queue.put(new Object[] {line, socket, out});
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
	 * by handleRequest, sends necessary output back to all clients that are all
	 * referencing the same boardName
	 * 
	 * @param obj   parsed input from the handleRequest method
	 * @throws IOException
	 */
	@SuppressWarnings("unchecked")
	public void sendOutput(Object[] obj) throws IOException {
		String input = (String) obj[0];
		Socket socket = (Socket) obj[1];
		PrintWriter out = (PrintWriter) obj[2];

		Object[] outputParsed = handleRequest(input, socket);
		ArrayList<String> output = (ArrayList<String>)outputParsed[0];
		String boardName = "";
		if (outputParsed.length > 1) {
			boardName = (String)outputParsed[1];
		}

		String[] tokens = input.split(" ");

		if (tokens[0].equals("bye")) {
			// signal to client to close connection
			out.println("end");
			
			ArrayList<Socket> connected = sockets.get(boardName);
			for (Socket otherSocket: connected) {
				PrintWriter socketOut = new PrintWriter(otherSocket.getOutputStream(), true);
				socketOut.println(input);
			}
		} else if (tokens[0].equals("add")) {
			ArrayList<Socket> connected = sockets.get(boardName);
			for(Socket otherSocket: connected) {
				PrintWriter socketOut = new PrintWriter(otherSocket.getOutputStream(),true);
				for (String user: usersOnCanvas.get(boardName)) {
					socketOut.println("add " + boardName + " " + user);
				}
			}
		} else if (tokens[0].equals("draw")) {
			ArrayList<Socket> connected = sockets.get(boardName);
			for(Socket otherSocket: connected) {
				PrintWriter socketOut = new PrintWriter(otherSocket.getOutputStream(),true);
				socketOut.println(input);
			}
		} else if (tokens[0].equals("username")) {
			String resp = output.get(0);
			if (resp.equals("username good") || resp.equals("contains")) {
				out.println(resp);
			}
		} else if (tokens[0].equals("get")) {
		    if (output != null) {
		        for (String str: output) {
		            out.println(str);
		        }
		    }
		    out.println("endinit");
		}
	}

	/**
	 * handleRequest takes in the input, performs the necessary operations 
	 * on the master copies of the boardnames to their sockets, canvas moves, 
	 * and usernames
	 * 
	 * @param input  input from the client
	 * @param socket socket through which the server is connected to the client
	 * @return output to be sent back to the client as an arraylist
	 * 
	 * for the input to be processed correctly, it must be entered in the following 
	 * fashion with the following pieces of information: 
	 * 		- add boardname userName
	 * 		- draw color size x1 y1 x2 y2 boardName
	 * 		- bye boardName userName
	 * 		- username boardName userName
	 * 		-get board boardName | get thread boardName
	 */
	private Object[] handleRequest(String input, Socket socket) {
		String regex = "(add \\w+ \\w+)|"
	                 + "(draw \\d+ \\d+ \\d+ \\d+ \\d+ \\d+ \\w+)|"
	                 + "(bye \\w+ \\w+)|"
	                 + "(username \\w+ \\w+)|"
	                 + "(get \\w+)";

		if (!input.matches(regex)) {
			// invalid input
			return null;
		}

		String[] tokens = input.split(" ");

		if(tokens[0].equals("add")) {
			/*
			 * If the whiteboard already exists, then the new socket gets added
			 * to the hashmap mapping whiteboard names to sockets, the past moves 
			 * on that canvas are retrieved, and the username is added to a hashmap
			 * mapping the board name to the usernames of those currently accessing
			 * the board
			 * 
			 * If the whiteboard does not exist, then the new socket gets added
			 * to the hashmap mapping whiteboard names to sockets, the past moves are
			 * empty (blank canvas), and the username is added to a hashmap
			 * mapping the board name to the usernames of those currently accessing
			 * the board
			 */
			String boardName = tokens[1];
			String userName = tokens[2];

			//add socket to boardname in the sockets hashmap
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
			
			//adds users to boardname in the boardname to users hashmap
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
			 * copies the input into the arraylist of past moves conducted on this board
			 * name
			 */
			String boardName = tokens[7];

			ArrayList<String> pastCanvasMoves = canvasMovesMap.get(boardName);
			pastCanvasMoves.add(input);
			canvasMovesMap.put(boardName, pastCanvasMoves);
			
			return new Object[]{pastCanvasMoves, boardName};
			
		} else if(tokens[0].equals("bye")) {
			/*
			 * removes the username from the board to username hashmap
			 */
			String boardName = tokens[1];
			String username = tokens[2];
			usersOnCanvas.get(boardName).remove(username);
			sockets.get(boardName).remove(socket);
			ArrayList<String> output = new ArrayList<String>();
			output.add(input);
			return new Object[] {output, boardName};
			
		} else if(tokens[0].equals("username")) {
			/*
			 *  checks if the username input is already contained in the hashmap
			 *  of the boardname to the usernames of all clients currently accessing 
			 *  the whitebard
			 */
			String boardName = tokens[1];
			String username = tokens[2];
			ArrayList<String> output = new ArrayList<String>();

			if(usersOnCanvas.get(boardName) != null) {
				if (usersOnCanvas.get(boardName).contains(username)) {
					output.add("contains");
					return new Object[] {output};
				}
			}
			output.add("username good");
			return new Object[] {output};
			
		} else if(tokens[0].equals("get")) {
			return new Object[] {canvasMovesMap.get(tokens[1])};
		} else {
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
