package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ConcurrentHashMap;

import client.Canvas;
import client.User;

public class WhiteboardServer {
	private ConcurrentHashMap<String,Canvas> canvasMap;
	private ConcurrentHashMap<String,ArrayList<Socket>> sockets;//by using sockets, you can only 
											//really user one username the whole time
	private final ArrayBlockingQueue<Object[]> queue;
	private static HashSet<String> usernames;
	public static final int SERVER_PORT = 5050;

	private ServerSocket serverSocket;
	/**
	 * server will take in whiteboard name and output the corresponding
	 * whiteboard
	 */

	

	/**
	 * Make a SquareServer that listens for connections on port.
	 * It also takes commands put in the queue and sends the output
	 * of them back to the client
	 * @param port port number, requires 0 <= port <= 65535.
	 */
	public WhiteboardServer(int port) throws IOException{
		System.out.println("server started in port " + port);
		serverSocket = new ServerSocket(port);
		canvasMap= new ConcurrentHashMap<String,Canvas>();
		sockets = new ConcurrentHashMap<String,ArrayList<Socket>>();
		queue = new ArrayBlockingQueue<Object[]>(1000);
		
		Thread thread = new Thread(new Runnable(){

			@Override
			public void run() {
				while(true){
					try{
						output(queue.take());
					}
				}
				
			}
			
		});
		
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
			Thread thread = new Thread(new Runnable(){
				public void run(){
					try{
						handle(socket);
					}catch(IOException e){
						e.printStackTrace();
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}finally{
						try {
							socket.close();
						} catch (IOException e) {
							// TODO Auto-generated catch block
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
	 * @param socket     socket through which the client is connected
	 * @throws IOException
	 * @throws InterruptedException
	 */
	private void handle(Socket socket) throws IOException, InterruptedException{
		System.out.println("client connected");

		BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		PrintWriter out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));

		try {
			// each request from the client is whiteboardName
			for (String line = in.readLine(); line != null; line = in.readLine()) {
				System.err.println("request: " + line);
				try {
					queue.put(new Object[]{line, socket, out});
				} catch (Exception e) {
					// complain about ill-formatted request
					e.printStackTrace();
				}
			}
		} finally {        
			out.close();
			in.close();
		}
	}

	/**
	 * open takes in whiteboard name and username
	 * handles the client's input and returns the corresponding output
	 * @param input
	 * @return
	 */
	private String[] handleRequest(String input, Socket socket){
		String regex = "(open \\w+ \\w+)|(draw \\w+ \\w+ \\d+ \\d+ \\d+ \\d+)|"
				+ "(erase \\w+ \\w+ \\d+ \\d+ \\d+ \\d+)|(bye \\w+)";
		String[] output;
		Canvas canvas;
		 if ( ! input.matches(regex)) {
	            // invalid input
	            return null;
	     }
		
		 
		 String[] tokens = input.split(" ");
		 
		 /**
		  * If the whiteboard already exists, then the new socket gets added
		  * to the hashmap mapping whiteboard names to sockets
		  * 
		  * If the whiteboard does not exist, then the whiteboard gets added
		  * to the hashmap of whiteboard names to canvases. Also, the client
		  * socket will be added to hashmap of whiteboard names to sockets
		  */
		 	if(tokens[0].equals("open")){
		 		String boardName = tokens[1];
		 		
		 		if(canvasMap.containsKey(boardName)){
		 			ArrayList<Socket> socketValue = sockets.get(boardName);
		 			socketValue.add(socket);
		 			sockets.put(boardName, socketValue);
		 		}
		 		else{
			 		canvasMap.put(boardName, new Canvas());
			 		ArrayList<Socket> socketValue = new ArrayList<Socket>();
			 		socketValue.add(socket);
			 		sockets.put(boardName, socketValue);
		 		}
		 		usernames.add(tokens[2]);
		 		canvas = canvasMap.get(boardName);
		 		output = new String[]{"open",boardName};
		 		return output;
		 		
		 	}
		 	else if(tokens[0].equals("draw")){
		 		//assuming canvas is already initialized, as in the open method 
		 		//has run first(is that bad?)
		 		int color = Integer.parseInt(tokens[1]);//<--will be the color represented as an int
		 		int size = Integer.parseInt(tokens[2]);//size represented as an int
		 		int x1 = Integer.parseInt(tokens[3]);
		 		int y1 = Integer.parseInt(tokens[4]);
		 		int x2 = Integer.parseInt(tokens[5]);
		 		int y2 = Integer.parseInt(tokens[6]);
		 		canvas.drawLineSegment(User.DRAW, color,size, x1,y1,x2,y2);
		 		output = new String[]{"draw", color, size, x1, y1, x2, y2};
		 		
		 	}
		 	else if(tokens[0].equals("erase")){
		 		
		 	}
		 	else{//tokens[0].equals("bye")
		 		
		 	}
		 	
	        if (tokens[0].equals("username")) {
	        	String username = tokens[1];
	        	return username;
	        } else if(tokens[0].equals("whiteboard")){//.equals whiteboard
	        	String name = tokens[2];
	        	return name;
	        }else{// (tokens[0].equals("draw")) or erase {
//	        	Color color = getColor(tokens[1]); 
//	        	Size size = getStroke(tokens[2]);
	        	//can we access the user brush settings
	        	int x1 = Integer.parseInt(tokens[1]);
	        	int x2 = Integer.parseInt(tokens[2]);
	        	int y1 = Integer.parseInt(tokens[3]);
	        	int y2 = Integer.parseInt(tokens[4]);
	        	//or maybe it'll take in locations instead..need 
	        	//to figure out.....;
	        	return drawLineSegment(x1,y1,x2,y2);//will be in a central class
	        } 
	        // Should never get here--make sure to return in each of the valid cases above.
	        throw new UnsupportedOperationException();
	}
	
	public static boolean containsUsername(String username){
		return(usernames.contains(username));
	}
	/**
	 * make tokens so that first word is the command, words after are the parameter
	 * username  name
	 * draw     color, size
	 * erase    color, size
	 * whiteboard   whiteboardname
	 * somehow need to send location as well
	 * 
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
