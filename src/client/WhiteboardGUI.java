package client;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.IOException;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

/**
 * this class combines the canvas and tools panel in one 
 * JFrame to present a unified appearance for the GUI
 *
 *TODO: Testing Strategy: 
 *
 */
@SuppressWarnings("serial")
public class WhiteboardGUI extends JFrame implements WindowListener {
	/**
	 * Brush type to draw
	 */
	public static final int DRAW = 0;

	/**
	 * Brush type to erase
	 */
	public static final int ERASE = 1;

	/**
	 * Brush size to small
	 */
	public static final int SMALL = 0;

	/**
	 * Brush size to medium
	 */
	public static final int MEDIUM = 1;

	/**
	 * Brush size to large
	 */
	public static final int LARGE = 2;

	/**
	 * Brush color to black (#000000)
	 */
	public static final int BLACK = 0;

	/**
	 * Brush color to red (#FF0000)
	 */
	public static final int RED = 1;

	/**
	 * Brush color to orange (#FF7700)
	 */
	public static final int ORANGE = 2;

	/**
	 * Brush color to yellow (#FFFF00)
	 */
	public static final int YELLOW = 3;

	/**
	 * Brush color to green (#00FF00)
	 */
	public static final int GREEN = 4;

	/**
	 * Brush color to blue (#0000FF)
	 */
	public static final int BLUE = 5;

	/**
	 * Brush color to white
	 */
	public static final int WHITE = 6;

	// INSTANCE VARIABLES
	/**
	 * Current brush setting (see brush constants)
	 */
	private int currentBrush;

	/**
	 * Current brush size (see size constants)
	 */
	private int currentSize;

	/**
	 * Current brush color (see color constants)
	 */
	private int currentColor;

	final UsernamePanel usernamePanel;
	private final ToolsPanel toolsPanel;
	final Canvas canvas;
	private final WhiteboardClient client;

	/**
	  * constructs a whiteboard gui under the given name, with the given canvas, 
	  * for the given client
	 * @param name    name of the whiteboard
	 * 			name!= null, name cannot be empty string
	 * 			name can only contain the characters [a-z],[A-Z], or [0-9]		
	 * @param canvas	canvas for the whiteboard
	 * 			canvas != null
	 * @param client  client for which the whiteboard gui is created
	 * 			client != null
	 */
	public WhiteboardGUI(String name, Canvas canvas, WhiteboardClient client){
		this.client = client;
		
		//default whiteboard settings
		currentBrush = DRAW;
		currentSize = SMALL;
		currentColor = BLACK;
		
		setLayout(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.fill = GridBagConstraints.BOTH;
		gbc.insets = new Insets(3, 3, 3, 3);

		usernamePanel = new UsernamePanel(this);
		gbc.gridx = 0;
		gbc.gridy = 0;
		add(usernamePanel, gbc);

		this.canvas = canvas;
		addDrawingController(canvas);
		gbc.gridx = 1;
		gbc.weightx = 1;
		gbc.weighty = 1;
		add(canvas, gbc);

		toolsPanel = new ToolsPanel(this);
		gbc.weightx = 0;
		gbc.weighty = 0;
		gbc.gridx = 2;
		add(toolsPanel, gbc);

		//assign the title of the WhiteboardGUI to be the name of the canvas being accessed
		setTitle(name);
		setResizable(false);
		pack();
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		addWindowListener(this);
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				setVisible(true);
			}
		});
	}

	/**
	 * sets the current Brush type (either draw or erase)
	 * @param newBrush   brush type
	 * 			newBrush != null, can only be Whiteboard.DRAW or 
	 * 			Whiteboard.ERASE
	 */
	public void setCurrentBrush(int newBrush) {
		currentBrush = newBrush;
	}
	
	/**
	 * sets the current Brush size (either smalle, medium or large)
	 * @param newSize  brush size
	 * 			newSize != null, can only be Whiteboard.SMALL, Whiteboard.MEDIUM, or 
	 * 			Whiteboard.LARGE
	 */
	public void setCurrentSize(int newSize) {
		currentSize = newSize;
	}
	
	/**
	 * sets the current Brush color
	 * @param newColor   brush color
	 * 			newColor != null, can only be Whiteboard.RED, Whiteboard.ORANGE, 
	 * 			Whiteboard.YELLOW, Whiteboard.GREEN, Whiteboard.BLUE, Whiteboard.BLACK, 
	 * 			or Whiteboard.WHITE
	 */
	public void setCurrentColor(int newColor) {
		currentColor = newColor;
	}
	
	/**
	 * gets client brush type as an int 0 or 1
	 * @return client brush type
	 */
	public int getCurrentBrush() {
		return currentBrush;
	}
	
	/**
	 * gets client brush size as an int 0, 1, or 2
	 * @return client brush size
	 */
	public int getCurrentSize() {
		return currentSize;
	}
	
	/**
	 * gets the client's brush color as an int 0 through 6
	 * @return client brush color
	 */
	public int getCurrentColor() {
		return currentColor;
	}
	
	/**
	 * adds drawing controller to the gui
	 * @param canvas   canvas that will have the drawing controller
	 * 			canvas!=null
	 */
	private void addDrawingController(Canvas canvas) {
		DrawingController controller = new DrawingController();
		canvas.addMouseListener(controller);
		canvas.addMouseMotionListener(controller);
	}

	/*
	 * DrawingController handles the user's freehand drawing.
	 */
	public class DrawingController implements MouseListener, MouseMotionListener {
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
			int color = currentColor;
			if (currentBrush == ERASE) {
				color = WHITE;
			}
			//canvas.drawLineSegment(color, currentSize, lastX, lastY, x, y);
			try {
				client.drawRequest(lastX, lastY, x, y, currentSize, color);
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			lastX = x;
			lastY = y;
		}

		public void mouseMoved(MouseEvent e) { }
		public void mouseClicked(MouseEvent e) { }
		public void mouseReleased(MouseEvent e) { }
		public void mouseEntered(MouseEvent e) { }
		public void mouseExited(MouseEvent e) { }
	}

	@Override
	public void windowActivated(WindowEvent we) {}

	/**
	 * stops the client if the window is closed
	 */
	@Override
	public void windowClosed(WindowEvent we) {
		System.out.println("closed");
		try {
			client.stop();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void windowClosing(WindowEvent we) {
		System.out.println("closed");
		try {
			client.stop();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void windowDeactivated(WindowEvent we) {}

	@Override
	public void windowDeiconified(WindowEvent we) {}

	@Override
	public void windowIconified(WindowEvent we) {}

	@Override
	public void windowOpened(WindowEvent we) {}
}