package client;

import java.awt.*;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;

/**
 * Canvas represents a drawing surface that allows the user to draw
 * on it freehand, with the mouse. It essentially listens for the actions on 
 * the screen and passes these actions to the client for processing
 * 
 * @param drawingBuffer: stores the instance of the drawn image on the canvas
 * This instance is saved for the duration that the server is open
 * so every time the user accesses a preexisting canvas object, the image will 
 * contain all of the history saved with it 
 */
@SuppressWarnings("serial")
public class Canvas extends JPanel {
    private static final int DEFAULT_WIDTH = 800;
    private static final int DEFAULT_HEIGHT = 800;
    private static final int SMALL_BRUSH_SIZE = 5;
    private static final int MEDIUM_BRUSH_SIZE = 10;
    private static final int LARGE_BRUSH_SIZE = 20;
    
	// image where the user's drawing is stored
	private BufferedImage drawingBuffer;
	
	/**
	 * default constructor of canvas. Creates a canvas of width 800 
	 * and height 800
	 */
	public Canvas(){
		this.setPreferredSize(new Dimension(DEFAULT_WIDTH, DEFAULT_HEIGHT));
	}

	/**
	 * @see javax.swing.JComponent#paintComponent(java.awt.Graphics)
	 */
	@Override
	public void paintComponent(Graphics g) {
		// If this is the first time paintComponent() is being called,
		// make our drawing buffer.
		if (drawingBuffer == null) {
			makeDrawingBuffer();
		}

		// Copy the drawing buffer to the screen.
		g.drawImage(drawingBuffer, 0, 0, null);
	}

	/**
	 * Make the drawing buffer and draw some starting content for it.
	 */
	private void makeDrawingBuffer() {
		drawingBuffer = (BufferedImage) createImage(getWidth(), getHeight());
		fillWithWhite();
	}

	/**
	 * Make the drawing buffer entirely white.
	 */
	private void fillWithWhite() {
		final Graphics2D g = (Graphics2D) drawingBuffer.getGraphics();

		g.setColor(Color.WHITE);
		g.fillRect(0,  0,  getWidth(), getHeight());
		this.repaint();
	}

	/**
	 * Draw a line between two points (x1, y1) and (x2, y2), specified in
	 * pixels relative to the upper-left corner of the drawing buffer.
	 * this method is PUBLIC because it will only be accessed by unique references
	 */
	public void drawLineSegment(int currentColor, int currentSize, int x1, int y1, int x2, int y2) {
		if (drawingBuffer == null) {
			makeDrawingBuffer();
		}
		Graphics2D g = (Graphics2D) drawingBuffer.getGraphics();
		g.setColor(getColor(currentColor));
		g.setStroke(new BasicStroke(getStroke(currentSize)));
		g.drawLine(x1, y1, x2, y2);
		this.repaint();
	}
	
	/**
	 * Returns the Color object corresponding to the passed in color code
	 * 
	 * @param userCurrentColor The user's current color code
	 * @return The corresponding Color object
	 */
	private Color getColor(int userCurrentColor){
		Color color;
		switch(userCurrentColor) {
		case WhiteboardGUI.BLACK:
		    color = Color.black;
		    break;
		case WhiteboardGUI.RED:
		    color = Color.red;
		    break;
		case WhiteboardGUI.ORANGE:
		    color = Color.orange;
		    break;
		case WhiteboardGUI.YELLOW:
		    color = Color.yellow;
		    break;
		case WhiteboardGUI.GREEN:
		    color = Color.green;
		    break;
		case WhiteboardGUI.BLUE:
		    color = Color.blue;
		    break;
		case WhiteboardGUI.WHITE:
		    color = Color.white;
		    break;
		default:
		    color = Color.black;
		    System.out.println("Color not recognized, set to black");
		    break;
		}
		return color;
	}

	/**
	 * Returns the brush size corresponding to passed in size code
	 * 
	 * @param userCurrentSize The user's current size code
	 * @return The integer value brush size
	 */
	private int getStroke(int userCurrentSize){
		int size;
		switch(userCurrentSize) {
		case WhiteboardGUI.SMALL:
		    size = SMALL_BRUSH_SIZE;
		    break;
		case WhiteboardGUI.MEDIUM:
		    size = MEDIUM_BRUSH_SIZE;
		    break;
		case WhiteboardGUI.LARGE:
		    size = LARGE_BRUSH_SIZE;
		    break;
		default:
		    size = SMALL_BRUSH_SIZE;
		    System.out.println("Size not recognized, set to medium");
		    break;
		}
		return size;
	}
}