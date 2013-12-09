package client;

import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import client.Canvas;
import client.ToolsPanel;

/**
 * this class combines the canvas and tools panel in one 
 * JFrame to present a unfied appearance for the GUI
 *
 */
@SuppressWarnings("serial")
public class WhiteboardGUI extends JFrame{
	private final ToolsPanel toolsPanel;
	final Canvas canvas;
	private final JButton switchButton;
	private final WhiteboardClient client;
	
	public WhiteboardGUI(String name, WhiteboardClient client){
		this.client = client;
		toolsPanel = new ToolsPanel();
		canvas = new Canvas(800,800);
		addDrawingController(canvas);
		
		switchButton = new JButton();
		switchButton.setText("Switch Canvas");
        addListeners(this);
		
		Container cp = this.getContentPane();
		GroupLayout layout = new GroupLayout(cp);
		cp.setLayout(layout);
		layout.setAutoCreateGaps(true);
		layout.setAutoCreateContainerGaps(true);
	
		layout.setHorizontalGroup(
				layout.createParallelGroup(GroupLayout.Alignment.LEADING)
				.addComponent(switchButton)
				.addGroup(layout.createSequentialGroup()
						.addComponent(canvas)
						.addComponent(toolsPanel))
				);
		
		layout.setVerticalGroup(
				layout.createSequentialGroup()
				.addComponent(switchButton)
				.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
						.addComponent(canvas)
						.addComponent(toolsPanel))
				);  
		
		//assign the title of the WhiteboardGUI to be the name of the canvas being accessed
		setTitle(name);
		pack();
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				setVisible(true);
			}
		});
	}
	
	private void addListeners(final WhiteboardGUI whiteboard) {
        switchButton.addActionListener(new ActionListener() {
        	
        	@Override
        	public void actionPerformed(ActionEvent e) {
        		SwingUtilities.invokeLater(new Runnable() {
        			public void run() {
        				EntryGUI gui = new EntryGUI();
        				gui.setVisible(true);
        				whiteboard.dispose();
        			}
        		});
        	}
        });
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
            canvas.drawLineSegment(User.getCurrentColor(), User.getCurrentSize(), lastX, lastY, x, y);
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
}