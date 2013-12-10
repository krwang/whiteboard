package client;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import javax.swing.DefaultListModel;
import server.WhiteboardServer;

public class WhiteboardClient {
	private final WhiteboardGUI gui;
	private final Socket socket;
	private final String username;
	private WhiteboardClientThread thread;
	private DataInputStream dataIn;
	private DataOutputStream dataOut;
	
	public WhiteboardClient(String user, String board) throws IOException {
		//TODO: need to figure out how to get the WhiteboardGUI in here...
		//maybe make the entry gui return the created or loaded WhiteboardGUI??
		username = user;
		//addDrawingController();
		socket = new Socket("localhost", 5050);
		start();
		Canvas canvas = WhiteboardServer.getBoard(board, user);
		addRequest(board);
		this.gui = new WhiteboardGUI(board, canvas, this);
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
		}
		else{
			throw new UnsupportedOperationException();
		}
	}
	
	public void addRequest(String boardName) throws IOException {
		String request = "add " + boardName;
		dataOut.writeUTF(request);
	}
	
	public void drawRequest(int x1, int y1, int x2, int y2, int size, int color) throws IOException {
		String request = String.format("draw %s %s %n %n %n %n %s", color, size, x1, y1, x2, y2, gui.getTitle());
		dataOut.writeUTF(request);
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
