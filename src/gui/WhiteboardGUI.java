package gui;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.FlowLayout;

import javax.swing.GroupLayout;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import canvas.Canvas;
import drawing.ToolsPanel;

/**
 * this class combines the canvas and tools panel in one 
 * JFrame to present a unfied appearance for the GUI
 *
 */
public class WhiteboardGUI extends JFrame{
	private final ToolsPanel toolsPanel;
	private final Canvas canvas;
	
	public WhiteboardGUI(){
		toolsPanel = new ToolsPanel();
		canvas = new Canvas(800,800);
		
		Container cp = this.getContentPane();
		GroupLayout layout = new GroupLayout(cp);
		cp.setLayout(layout);
		layout.setAutoCreateGaps(true);
		layout.setAutoCreateContainerGaps(true);
	
		layout.setHorizontalGroup(
				layout.createParallelGroup(GroupLayout.Alignment.LEADING)
				.addGroup(layout.createSequentialGroup()
						.addComponent(canvas)
						.addComponent(toolsPanel))
				);
		
		layout.setVerticalGroup(
				layout.createSequentialGroup()
				.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
						.addComponent(canvas)
						.addComponent(toolsPanel))
				);  
		
		setTitle("Whiteboard");//should set title to soemthing sepcific for diff whiteboard
		pack();
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

	}
	
	//DELETE THIS AT SOME POINT
//	public static void main(final String[] args) {
//		SwingUtilities.invokeLater(new Runnable() {
//			public void run() {
//				WhiteboardGUI main = new WhiteboardGUI();
//
//				main.setVisible(true);
//			}
//		});
//	}
}
