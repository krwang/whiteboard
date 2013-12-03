package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class WhiteboardServer {
	
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
		}
		try{
			handle(socket);
		}catch(IOException e){
			e.printStackTrace();
		}finally{
			socket.close();
		}
	}
	
}
