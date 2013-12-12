package client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import javax.swing.DefaultListModel;

/**
 * This class contains code for the WhiteboardClient, which
 * will act as the controller in the MVC design on the client
 * side. The WhiteboardClient can accept messages from the server
 * and update the client canvas as well as send action messages
 * to the server.
 * 
 * This class is threadsafe because each client is separate and each 
 * client only contains one thread that is not accessed by anything else.
 * Also, each request from the data that is read is is handled one line at 
 * a time, so there are no race conditions. Also, the input is read through 
 * a buffer reader. 
 */
public class WhiteboardClient {
    private final WhiteboardGUI gui;
    private final String canvasName;
    private final Socket socket;
    private final String username;
    private final String ipaddress;
    private BufferedReader dataIn;
    private PrintWriter dataOut;

    /**
     * creates a local Whiteboard Client. Connects to the localhost server through a socket
     * and sends a request to connect to board
     * @param user is the username inputted into the EntryGUI, used to identify the user on the server
     * @param canvas is the canvas to be connected to. Either the server will create it or the client will load it
     * @param ip The IP address to open the socket on, should correspond to the IP of the server host
     * @throws IOException
     */
    public WhiteboardClient(String user, String canvas, String ip) throws IOException {
        username = user;
        canvasName = canvas;
        ipaddress = ip;
        
        //creating a new socket and connecting it to the server
        socket = new Socket(ipaddress, 5050);
        
        dataIn = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        dataOut = new PrintWriter(socket.getOutputStream(), true);
        
        Canvas c = new Canvas();
        this.gui = new WhiteboardGUI(canvas, c, this);
        
        dataOut.println("get " + canvas);
        try {
            String line;
            while (!(line = dataIn.readLine()).equals("endinit")){
            	if (!line.equals("")) {
	                String[] args = line.split(" ");
	                int color = Integer.parseInt(args[1]);
	                int size = Integer.parseInt(args[2]);
	                int x1 = Integer.parseInt(args[3]);
	                int y1 = Integer.parseInt(args[4]);
	                int x2 = Integer.parseInt(args[5]);
	                int y2 = Integer.parseInt(args[6]);
	                c.drawLineSegment(color, size, x1, y1, x2, y2);
            	}
            	else {
            		break;
            	}
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        addRequest();
        
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    String line;
                    while (!((line = dataIn.readLine()).equals("end"))) {
                        handle(line);
                    }
                    
                    // close connections
                    dataIn.close();
                    dataOut.close();
                    socket.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
    
    /**
     * Accessor to the IP address field
     * @return the ip address
     */
    public String getIP() {
        return ipaddress;
    }
    
    /**
     * handles requests from the server and updates the canvas accordingly
     * @param message
     */
    public void handle(String message) {
        String regex = "(add \\w+ \\w+)|(draw \\w+ \\w+ \\d+ \\d+ \\d+ \\d+ \\w+)|(bye \\w+ \\w+)";
        if (!message.matches(regex)) {
            // invalid input
            return;
        }

        String[] tokens = message.split(" ");

        /**
         * if someone joins the canvas
         */
        if(tokens[0].equals("add")){
            String userName = tokens[2];

            //add username to username panel
            DefaultListModel<String> model = (DefaultListModel<String>) gui.usernamePanel.usernameList.getModel();
            if (!model.contains(userName)) {
            	model.addElement(userName);
            }
            gui.usernamePanel.usernameList.setModel(model);
            gui.repaint();
        }

        /**
         * draws the draw input on the client canvas
         */
        else if(tokens[0].equals("draw")) {
            int color = Integer.parseInt(tokens[1]);
            int size = Integer.parseInt(tokens[2]);
            int x1 = Integer.parseInt(tokens[3]);
            int y1 = Integer.parseInt(tokens[4]);
            int x2 = Integer.parseInt(tokens[5]);
            int y2 = Integer.parseInt(tokens[6]);
            gui.canvas.drawLineSegment(color,size, x1,y1,x2,y2);
        }

        /**
         * if someone disconnects from the canvas
         */
        else if(tokens[0].equals("bye")){
            String userName = tokens[2];

            //remove username from the username panel
            DefaultListModel<String> model = (DefaultListModel<String>) gui.usernamePanel.usernameList.getModel();
            model.removeElement(userName);
            gui.usernamePanel.usernameList.setModel(model);
        } else {
            throw new UnsupportedOperationException();
        }
    }

    /**
     * sends an add request to the server, so that the server adds the user
     * to the list of users accessing the specified canvas and sends the canvas to the client
     * @throws IOException
     */
    public void addRequest() throws IOException {
        String request = "add " + canvasName + " " + username;
        dataOut.println(request);
        dataOut.flush();
    }

    /**
     * sends a draw request to the server, so the server will update the central canvas version
     * @param x1 is the starting x coordinate of the drawing on the canvas
     * @param y1 is the starting y coordinate of the drawing on the canvas
     * @param x2 is the ending x coordinate of the drawing on the canvas
     * @param y2 is the ending y coordinate of the drawing on the canvas
     * @param size is the current size of the brush being used
     * @param color is the current color of the brush being used
     * @throws IOException
     */
    public void drawRequest(int x1, int y1, int x2, int y2, int size, int color) throws IOException {
        String request = String.format("draw %d %d %d %d %d %d %s", color, size, x1, y1, x2, y2, gui.getTitle());
        dataOut.println(request);
    }

    /**
     * sends a bye request to the server, so the server can remove the user from
     * the list of users accessing the canvas currently
     * @throws IOException
     */
    public void byeRequest() throws IOException {
        String request = "bye " + canvasName + " " + username;
        dataOut.println(request);
    }
}
