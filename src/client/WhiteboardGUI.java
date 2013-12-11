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

	public WhiteboardGUI(String name, WhiteboardClient client){
		this.client = client;
		
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

		canvas = new Canvas();
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

	public WhiteboardGUI(String name, Canvas canvas, WhiteboardClient client){
		this.client = client;
		
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

	public void setCurrentBrush(int newBrush) {
		currentBrush = newBrush;
	}
	
	public void setCurrentSize(int newSize) {
		currentSize = newSize;
	}
	
	public void setCurrentColor(int newColor) {
		currentColor = newColor;
	}
	
	public int getCurrentBrush() {
		return currentBrush;
	}
	
	public int getCurrentSize() {
		return currentSize;
	}
	
	public int getCurrentColor() {
		return currentColor;
	}
	
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

		// Ignore all these other mouse events.
		public void mouseMoved(MouseEvent e) { }
		public void mouseClicked(MouseEvent e) { }
		public void mouseReleased(MouseEvent e) { }
		public void mouseEntered(MouseEvent e) { }
		public void mouseExited(MouseEvent e) { }
	}

	@Override
	public void windowActivated(WindowEvent we) {}

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