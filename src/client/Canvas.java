package client;

import java.awt.*;
import javax.swing.JPanel;

/**
 * Canvas represents a drawing surface that allows the user to draw
 * on it freehand, with the mouse.
 * 
 * @param drawingBuffer: stores the instance of the drawn image on the canvas
 */
@SuppressWarnings("serial")
public class Canvas extends JPanel {
	// image where the user's drawing is stored
	private Image drawingBuffer;
	private final int DEFAULT_WIDTH = 800;
	private final int DEFAULT_HEIGHT = 600;

	public Canvas(){
		this.setPreferredSize(new Dimension(DEFAULT_WIDTH, DEFAULT_HEIGHT));
	}

	/**
	 * Make a canvas.
	 * @param width width in pixels
	 * @param height height in pixels
	 */
	public Canvas(int width, int height) {
		this.setPreferredSize(new Dimension(width, height));
		// note: we can't call makeDrawingBuffer here, because it only
		// works *after* this canvas has been added to a window.  Have to
		// wait until paintComponent() is first called.
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

	/*
	 * Make the drawing buffer and draw some starting content for it.
	 */
	private void makeDrawingBuffer() {
		drawingBuffer = createImage(getWidth(), getHeight());
		fillWithWhite();
	}

	/*
	 * Make the drawing buffer entirely white.
	 */
	private void fillWithWhite() {
		final Graphics2D g = (Graphics2D) drawingBuffer.getGraphics();

		g.setColor(Color.WHITE);
		g.fillRect(0,  0,  getWidth(), getHeight());

		// IMPORTANT!  every time we draw on the internal drawing buffer, we
		// have to notify Swing to repaint this component on the screen.
		this.repaint();
	}

	/*
	 * Draw a line between two points (x1, y1) and (x2, y2), specified in
	 * pixels relative to the upper-left corner of the drawing buffer.
	 * this method is PUBLIC because it will only be accessed by unique references
	 */
	public void drawLineSegment(int currentColor, int currentSize, int x1, int y1, int x2, int y2) {
		Graphics2D g = (Graphics2D) drawingBuffer.getGraphics();

		g.setColor(getColor(currentColor));
		g.setStroke(new BasicStroke(getStroke(currentSize)));
		g.drawLine(x1, y1, x2, y2);

		// IMPORTANT!  every time we draw on the internal drawing buffer, we
		// have to notify Swing to repaint this component on the screen.
		this.repaint();
	}

	private Color getColor(int userCurrentColor){
		Color color;
		switch(userCurrentColor){
		case 0: color = Color.black;
		break;
		case 1: color = Color.red;
		break;
		case 2: color = Color.orange;
		break;
		case 3: color = Color.yellow;
		break;
		case 4: color = Color.green;
		break;
		case 5: color = Color.blue;
		break;
		case 6: color = Color.white;
		break;
		default: color = Color.black;
		break;
		}
		return color;
	}

	private int getStroke(int userCurrentSize){
		int size;
		switch(userCurrentSize){
		case 0: size = 5;
		break;
		case 1: size = 10;
		break;
		case 2: size = 20;
		break;
		default:size = 10;
		break;
		}
		return size;
	}
}
