package client;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class WhiteboardClient {
	private final WhiteboardGUI gui;
	private final Socket socket;
	private final String username;
	private WhiteboardClientThread thread;
	private DataInputStream dataIn;
	private DataOutputStream dataOut;
	
	public WhiteboardClient(String user, String board) {
		//TODO: need to figure out how to get the WhiteboardGUI in here...
		//maybe make the entry gui return the created or loaded WhiteboardGUI??
		username = user;
		this.gui = new WhiteboardGUI(board, this);
		addDrawingController();
		socket = new Socket();
		//this.socket = socket;
	}
	
	public void start() throws IOException {
		// change input stream to Server output stream?
		dataIn = new DataInputStream(System.in);
		dataOut = new DataOutputStream(socket.getOutputStream());
		if (thread == null) {
			thread = new WhiteboardClientThread(socket, this);
		}
	}
	
	public void stop() throws IOException {
		if (dataIn != null) dataIn.close();
		if (dataOut != null) dataOut.close();
		if (socket != null) socket.close();
		thread.close();
	}
	
	public void handle(String message) {
		//TODO: write regex and stuff
	}
	
	public void sendMessage(int x1, int y1, int x2, int y2, int size, int color) {
		//TODO: send message with the socketID??
	}
	
	public void receiveMessage(int x1, int y1, int x2, int y2, int size, int color) { // are size and color ints?? (from the user class)
		//TODO: draw on canvas??
	}
	
	/*
     * Add the mouse listener that supports the user's freehand drawing.
     */
    private void addDrawingController() {
        DrawingController controller = new DrawingController();
        gui.canvas.addMouseListener(controller);
        gui.canvas.addMouseMotionListener(controller);
    }
    
	/*
     * DrawingController handles the user's freehand drawing.
     */
    final class DrawingController implements MouseListener, MouseMotionListener {
        // store the coordinates of the last mouse event, so we can
        // draw a line segment from that last point to the point of the next mouse event.
        private int lastX, lastY; 

        /*
         * When mouse button is pressed down, start drawing.
         */
        public void mousePressed(MouseEvent e) {
            lastX = e.getX();
            lastY = e.getY();
        }

        /*
         * When mouse moves while a button is pressed down,
         * draw a line segment.
         */
        public void mouseDragged(MouseEvent e) {
            int x = e.getX();
            int y = e.getY();
            sendMessage(lastX, lastY, x, y, 1, 0); //TODO: figure out how to send brush color/size, can already send line segment
            lastX = x;
            lastY = y;
        }

        //ignore these events
		public void mouseMoved(MouseEvent arg0) {}
		public void mouseClicked(MouseEvent e) {}
		public void mouseEntered(MouseEvent e) {}
		public void mouseExited(MouseEvent e) {}
		public void mouseReleased(MouseEvent e) {}
    }
}
