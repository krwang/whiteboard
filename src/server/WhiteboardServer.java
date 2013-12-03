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

import canvas.Canvas;

public class WhiteboardServer {
	private HashMap<String,Canvas> canvasMap= new HashMap<String,Canvas>();
	
	/**
	 * server will take in whiteboard name and output the corresponding
	 * whiteboard
	 */

	public static final int SERVER_PORT = 5050;
	
	private ServerSocket serverSocket;
	
	/**
     * Make a SquareServer that listens for connections on port.
     * @param port port number, requires 0 <= port <= 65535.
     */
	public WhiteboardServer(int port) throws IOException{
		System.out.println("server started in port " + port);
		serverSocket = new ServerSocket(port);
	}
	
    /**
     * Run the server, listening for client connections and handling them.  
     * Never returns unless an exception is thrown.
     * @throws IOException if the main server socket is broken
     * (IOExceptions from individual clients do *not* terminate serve()).
     */
	public void serve() throws IOException{
		while(true){
			Socket socket = serverSocket.accept();
		
		try{
			handle(socket);
		}catch(IOException e){
			e.printStackTrace();
		}finally{
			socket.close();
		}
		}
	}
	
	//where will the server get it's input from??what will read to it?
	private void handle(Socket socket) throws IOException{
		System.err.println("client connected");
		
        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        PrintWriter out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));

        try {
            // each request from the client is whiteboardName
            for (String line = in.readLine(); line != null; line = in.readLine()) {
                System.err.println("request: " + line);
                try {
                	if(canvas.contains(o))
                    int x = Integer.valueOf(line);
                    
                    // compute answer and send back to client
                    int y = x*x;     
                    System.err.println("reply: " + y);
                    out.print(y + "\n");
                } catch (NumberFormatException e) {
                    // complain about ill-formatted request
                    System.err.println("reply: err");
                    out.println("err");
                }
                
                // VERY IMPORTANT! flush our buffer so the client gets the reply
                out.flush();
            }
        } finally {        
            out.close();
            in.close();
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
