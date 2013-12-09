package client;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

/**
 * this class combines the canvas and tools panel in one 
 * JFrame to present a unfied appearance for the GUI
 *
 */
@SuppressWarnings("serial")
public class WhiteboardGUI extends JFrame{
	private final UsernamePanel usernamePanel;
	private final ToolsPanel toolsPanel;
	final Canvas canvas;
	private final WhiteboardClient client;
	
	public WhiteboardGUI(String name, WhiteboardClient client){
		this.client = client;
		
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        gbc.insets = new Insets(3, 3, 3, 3);
        
        usernamePanel = new UsernamePanel(this);
        gbc.gridx = 0;
        gbc.gridy = 0;
        add(usernamePanel, gbc);
        
		canvas = new Canvas(800,800);
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
		
//		Container cp = this.getContentPane();
//		GroupLayout layout = new GroupLayout(cp);
//		cp.setLayout(layout);
//		layout.setAutoCreateGaps(true);
//		layout.setAutoCreateContainerGaps(true);
//	
//		layout.setHorizontalGroup(
//				layout.createParallelGroup(GroupLayout.Alignment.LEADING)
//				.addComponent(switchButton)
//				.addGroup(layout.createSequentialGroup()
//						.addComponent(toolsPanel)
//						.addComponent(canvas)
//						.addComponent(usernamePanel))
//				);
//		
//		layout.setVerticalGroup(
//				layout.createSequentialGroup()
//				.addComponent(switchButton)
//				.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
//						.addComponent(toolsPanel)
//						.addComponent(canvas)
//						.addComponent(usernamePanel))
//				);  
		
		//assign the title of the WhiteboardGUI to be the name of the canvas being accessed
		setTitle(name);
		setResizable(false);
		pack();
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				setVisible(true);
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
            canvas.drawLineSegment(User.getCurrentBrush(), User.getCurrentColor(), User.getCurrentSize(), lastX, lastY, x, y);
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