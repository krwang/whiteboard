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
	private HashSet<String> usernames;
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
	    usernames = new HashSet<String>();
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

	//where will the server get it's input from??what will read to it?
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
		System.out.println("USERS: " + usernames);
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
//		for (String str: output) {
//			PrintWriter socketOut = new PrintWriter(socket.getOutputStream(),true);
//            socketOut.println(str);
//            System.out.println(str);
//		}
		System.out.println(output);
		String boardName = (String)outputParsed[1];
		//okay, right now i'm passing out the whole list of all the moves for that particular canvas..idk if that's the right way to go about this

		if (!output.isEmpty()) {
			for (String str: output) {
				System.out.println("String: " + str);
				out.println(str);
				out.flush();//<-might not need this
			}
		}
		else {
			out.println("");
		}
		String[] tokens = input.split(" ");

		if (tokens[0].equals("bye")) {
			//where do i close the input stream???
			//			out.close();
			sockets.get(socket).remove(socket);
		} else if (tokens[0].equals("add")) {
		    for (String action : output) {
	            PrintWriter socketOut = new PrintWriter(socket.getOutputStream(),true);
	            socketOut.println(action);
	            System.out.println(action);
	        }
		} else if (tokens[0].equals("draw")) {
		    ArrayList<Socket> connected = sockets.get(boardName);
		    for(Socket otherSocket: connected) {
		        PrintWriter socketOut = new PrintWriter(otherSocket.getOutputStream(),true);
		        socketOut.println(input);
		        System.out.println(input);
		    }
		}
	}

	/**
	 * open takes in whiteboard name and username
	 * boardName must be valid already when this is called
	 * handles the client's input and returns the corresponding output
	 * 
	 * @param input
	 * @return
	 */
	private Object[] handleRequest(String input, Socket socket) {
		System.out.println("handleRequest");
		System.out.println("input: " + input);
		String regex = "(add \\w+ \\w+)|(draw \\d+ \\d+ \\d+ \\d+ \\d+ \\d+ \\w+)|(bye \\w+ \\w+)";
//		Object[] output;
//		Canvas canvas;
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
			System.out.println("add userName " + userName);
			
//			//add canvas to boardname
////			canvas = canvasMap.putIfAbsent(boardName, new Canvas());//if hasn't been created
//			canvasMap.putIfAbsent(boardName, new Canvas());//if hasn't been created
//
////			if(canvas== null){
////				canvas = canvasMap.get(boardName);
////			}
//			canvas = canvasMap.get(boardName);//if has alrady been created

			
			//add socket to boardname
			ArrayList<Socket> socketValue = new ArrayList<Socket>();
			socketValue.add(socket);
			sockets.putIfAbsent(boardName, socketValue);
			
			ArrayList<Socket> priorSocketValue = sockets.get(boardName);
			if (!priorSocketValue.contains(socket)) {
				priorSocketValue.add(socket);
				sockets.put(boardName, priorSocketValue);
			}
			
			canvasMovesMap.putIfAbsent(boardName, new ArrayList<String>());//is this null right now??
//			if(canvasMovesMap.get(boardName)== null){
//				canvasMovesMap.put(boardName, value)
//			}
			
			//sends over moves from existing canvas
			ArrayList<String> canvasMoves = canvasMovesMap.get(boardName);
			usernames.add(userName);
			
			return new Object[]{canvasMoves, boardName};
		} else if(tokens[0].equals("draw")) {
		    /*
	         * draws the draw input from the client onto the master copy of the
	         * whiteboard that the client is referencing to update the master copy
	         */
		    
			System.out.println("draw");
			//assuming canvas is already initialized, as in the open method 
			//has run first(is that bad?)
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
			System.out.println("boardname " + boardName);
//			canvas = canvasMap.get(boardName);
//			System.out.println("canvas name " + canvas);
//			canvas.drawLineSegment(color,size, x1,y1,x2,y2);
//			output = new Object[]{"draw", tokens[1], tokens[2], tokens[3], tokens[4],
//					tokens[5], tokens[6]};
//			System.out.println("draw output");
//			for(int i = 0; i < output.length; i++){
//				System.out.print(output[i] + ",");
//			}

			System.out.println("boardName " + boardName);
			
			ArrayList<String> pastCanvasMoves = canvasMovesMap.get(boardName);
			pastCanvasMoves.add(input);
			canvasMovesMap.put(boardName, pastCanvasMoves);
			
			return new Object[]{pastCanvasMoves, boardName};
		} else if(tokens[0].equals("bye")) {
			System.out.println("bye");
//			String userName = tokens[1];
			String boardName = tokens[2];
			return new Object[]{input, boardName};
		} else {
			throw new UnsupportedOperationException();
		}		
	}

//	public static Canvas getBoard(String boardName, String userName){
//		canvasMap.putIfAbsent(boardName, new Canvas());
//		usernames.add(userName);
//		Canvas canvas = canvasMap.get(boardName);
////		System.out.println(canvasMap.get(boardName));
//		return canvas;
//	}

//	public static boolean containsUsername(String username){
//		return(usernames.contains(username));
//	}

//	public static ArrayList<String> getCanvasMapNames(){
//		ArrayList<String> names = new ArrayList<String>();
//		for(String key : canvasMap.keySet()){
//			names.add(key);
//		}
//		return names;
//	}
	
	/*
	 * make tokens so that first word is the command, words after are the parameter
	 * username  name
	 * draw     color, size
	 * erase    color, size
	 * whiteboard   whiteboardname
	 * somehow need to send location as well
	 */
	
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
