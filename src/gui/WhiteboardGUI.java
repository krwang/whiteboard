package gui;

import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import canvas.Canvas;
import drawing.ToolsPanel;

/**
 * this class combines the canvas and tools panel in one 
 * JFrame to present a unfied appearance for the GUI
 *
 */
@SuppressWarnings("serial")
public class WhiteboardGUI extends JFrame{
	private final ToolsPanel toolsPanel;
	private final Canvas canvas;
	private final JButton switchButton;
	
	public WhiteboardGUI(String name){
		toolsPanel = new ToolsPanel();
		canvas = new Canvas(800,800);
		
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
