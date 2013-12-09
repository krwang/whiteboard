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
	private static HashSet<String> usernames = new HashSet<String>();
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
	public void sendOutput(Object[] obj) throws IOException{
		String input = (String)obj[0];
		Socket socket = (Socket)obj[1];
		PrintWriter out = (PrintWriter)obj[2];

		Object[] outputParsed = handleRequest(input, socket);
		String[] output = (String[])outputParsed[0];
		String boardName = (String)outputParsed[1];

//		out.println(output);
//		out.flush();<-might not need this

		if(output[0].equals("bye")){
			//where do i close the input stream???
			out.close();
			sockets.get(socket).remove(socket);
		}
		else{
			for(Socket otherSocket: sockets.get(boardName)){
				PrintWriter socketOut = new PrintWriter(otherSocket.getOutputStream(),true);
				socketOut.println(output);
				socketOut.flush();
			}
		}



	}

	/**
	 * open takes in whiteboard name and username
	 * handles the client's input and returns the corresponding output
	 * @param input
	 * @return
	 */
	private Object[] handleRequest(String input, Socket socket){
		String regex = "(open \\w+ \\w+)|(draw \\w+ \\w+ \\d+ \\d+ \\d+ \\d+)|"
				+ "(erase \\w+ \\w+ \\d+ \\d+ \\d+ \\d+)|(bye \\w+)";
		Object[] output;
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
			output = new Object[]{new Object[]{"open",boardName}, boardName};
			return output;

		}

		/**
		 * draws the draw input from the client onto the master copy of the
		 * whiteboard that the client is referencing to update the master copy
		 */
		else if(tokens[0].equals("draw")){
			//assuming canvas is already initialized, as in the open method 
			//has run first(is that bad?)
			int color = Integer.parseInt(tokens[1]);//<--will be the color represented as an int
			int size = Integer.parseInt(tokens[2]);//size represented as an int
			int x1 = Integer.parseInt(tokens[3]);
			int y1 = Integer.parseInt(tokens[4]);
			int x2 = Integer.parseInt(tokens[5]);
			int y2 = Integer.parseInt(tokens[6]);
			String boardName = tokens[7];
			canvas = canvasMap.get(boardName);
			canvas.drawLineSegment(User.DRAW, color,size, x1,y1,x2,y2);
			output = new Object[]{"draw", tokens[1], tokens[2], tokens[3], tokens[4],
					tokens[5], tokens[6]};
			return new Object[]{output, boardName};

		}

		/**
		 * updates the master copy of the whiteboard the client is referencing 
		 * by performing the erase input from the client on the master whiteboard
		 */
		else if(tokens[0].equals("erase")){
			int color = Integer.parseInt(tokens[1]);//<--will be the color represented as an int
			//what to do with this color thing?????
			int size = Integer.parseInt(tokens[2]);//size represented as an int
			int x1 = Integer.parseInt(tokens[3]);
			int y1 = Integer.parseInt(tokens[4]);
			int x2 = Integer.parseInt(tokens[5]);
			int y2 = Integer.parseInt(tokens[6]);
			String boardName = tokens[7];
			canvas = canvasMap.get(boardName);
			canvas.drawLineSegment(User.ERASE, color,size, x1,y1,x2,y2);
			output = new String[]{"erase", tokens[1], tokens[2], tokens[3], tokens[4],
					tokens[5], tokens[6]};
			return new Object[]{output, boardName};
			//repeat code...this might be bad......
		}
		else if(tokens[0].equals("bye")){
			return new String[]{"bye"};
		}
		else{
			throw new UnsupportedOperationException();
		}
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
