package client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import javax.swing.DefaultListModel;

import server.WhiteboardServer;

/**
 * TODO: class declaration
 * @author krwang
 *
 */

public class WhiteboardClient {
    private final WhiteboardGUI gui;
    private final String canvasName;
    private final Socket socket;
    private final String username;
    private WhiteboardClientThread thread;
    private BufferedReader dataIn;
    private PrintWriter dataOut;
    //private DataInputStream dataIn;
    //private DataOutputStream dataOut;

    /**
     * creates a local Whiteboard Client. Connects to the localhost server through a socket
     * and sends a request to connect to board
     * @param user is the username inputted into the EntryGUI, used to identify the user on the server
     * @param canvas is the canvas to be connected to. Either the server will create it or the client will load it
     * @throws IOException
     */
    public WhiteboardClient(String user, String canvas, Socket s) throws IOException {
        //TODO: need to figure out how to get the WhiteboardGUI in here...
        //maybe make the entry gui return the created or loaded WhiteboardGUI??
        username = user;
        canvasName = canvas;

        //creating a new socket and connecting it to the server
        socket = s;

        // start();

        dataIn = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        dataOut = new PrintWriter(socket.getOutputStream(), true);

        //create the WhiteboardGUI with the specified canvas
        addRequest();

        Canvas c = new Canvas();
        try {
            String line;
            while (!(line = dataIn.readLine()).equals("endinit")) {
                String[] args = line.split(" ");
                int color = Integer.parseInt(args[1]);//<--will be the color represented as an int
                int size = Integer.parseInt(args[2]);//size represented as an int
                int x1 = Integer.parseInt(args[3]);
                int y1 = Integer.parseInt(args[4]);
                int x2 = Integer.parseInt(args[5]);
                int y2 = Integer.parseInt(args[6]);
                c.drawLineSegment(color, size, x1, y1, x2, y2);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        thread = new WhiteboardClientThread(socket, this);
        this.gui = new WhiteboardGUI(canvas, c, this);
    }

    /**
     * opens all data streams and opens a client thread
     * @throws IOException
     */
    public void start() throws IOException {
        // change input stream to Server output stream?
        dataIn = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        dataOut = new PrintWriter(socket.getOutputStream(), true);
        if (thread == null) {
            thread = new WhiteboardClientThread(socket, this);
        }
    }

    /**
     * sends notice to the server about disconnecting and closes
     * the data streams, the socket, and the thread	
     * @throws IOException
     */
    public void stop() throws IOException {
        System.out.println("stopped");
        byeRequest();
        if (dataIn != null) dataIn.close();
        if (dataOut != null) dataOut.close();
        if (socket != null) socket.close();
        thread.close();
    }

    /**
     * handles requests from the server and updates the canvas accordingly
     * @param message
     */
    public void handle(String message) {
        // NOTE: i don't think this regex is right..
        String regex = "(add \\w+)|(draw \\w+ \\w+ \\d+ \\d+ \\d+ \\d+)|(bye \\w+)";
        if ( ! message.matches(regex)) {
            // invalid input
            return;
        }

        String[] tokens = message.split(" ");

        /**
         * if someone joins the canvas
         */
        if(tokens[0].equals("add")){
            //String boardName = tokens[1];
            String userName = tokens[2];

            //add username to username panel
            DefaultListModel<String> model = (DefaultListModel<String>) gui.usernamePanel.usernameList.getModel();
            model.addElement(userName);
            gui.usernamePanel.usernameList.setModel(model);
        }

        /**
         * draws the draw input on the client canvas
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
            //String boardName = tokens[7];
            gui.canvas.drawLineSegment(color,size, x1,y1,x2,y2);
        }

        /**
         * if someone disconnects from the canvas
         */
        else if(tokens[0].equals("bye")){
            String userName = tokens[1];

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
        //dataOut.flush();
        System.out.println(request);
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
        String request = String.format("draw %s %s %d %d %d %d %s", color, size, x1, y1, x2, y2, gui.getTitle());
        dataOut.println(request);
        //dataOut.flush();
        System.out.println(request);
    }

    /**
     * sends a bye request to the server, so the server can remove the user from
     * the list of users accessing the canvas currently
     * @throws IOException
     */
    public void byeRequest() throws IOException {
        String request = "bye " + canvasName + " " + username;
        dataOut.println(request);
        //dataOut.flush();
        System.out.println(request);
    }

    //	/*
    //     * Add the mouse listener that supports the user's freehand drawing.
    //     */
    //    private void addDrawingController() {
    //        DrawingController controller = new DrawingController();
    //        gui.canvas.addMouseListener(controller);
    //        gui.canvas.addMouseMotionListener(controller);
    //    }
    //    
    //	/*
    //     * DrawingController handles the user's freehand drawing.
    //     */
    //    final class DrawingController implements MouseListener, MouseMotionListener {
    //        // store the coordinates of the last mouse event, so we can
    //        // draw a line segment from that last point to the point of the next mouse event.
    //        private int lastX, lastY; 
    //
    //        /*
    //         * When mouse button is pressed down, start drawing.
    //         */
    //        public void mousePressed(MouseEvent e) {
    //            lastX = e.getX();
    //            lastY = e.getY();
    //        }
    //
    //        /*
    //         * When mouse moves while a button is pressed down,
    //         * draw a line segment.
    //         */
    //        public void mouseDragged(MouseEvent e) {
    //            int x = e.getX();
    //            int y = e.getY();
    //            try {
    //				drawRequest(lastX, lastY, x, y, 1, 0);
    //			} catch (IOException e1) {
    //				e1.printStackTrace();
    //			} //TODO: figure out how to send brush color/size, can already send line segment
    //            lastX = x;
    //            lastY = y;
    //        }
    //
    //        //ignore these events
    //		public void mouseMoved(MouseEvent arg0) {}
    //		public void mouseClicked(MouseEvent e) {}
    //		public void mouseEntered(MouseEvent e) {}
    //		public void mouseExited(MouseEvent e) {}
    //		public void mouseReleased(MouseEvent e) {}
    //    }
}