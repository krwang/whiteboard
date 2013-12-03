package gui;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

//TODO: add swing workers

/**
 * This class contains code for a EntryGUI. The EntryGUI allows the user to 
 * either create a new Whiteboard and input the name of the Whiteboard
 * or load a previously created Whiteboard by name. There will be a table at the bottom
 * that contains all previously created Whiteboards. 
 * 
 * TODO: thread safety argument
 * 
 * TODO: testing strategy
 * 
 * @author krwang
 *
 */
@SuppressWarnings("serial")
public class EntryGUI extends JFrame {

	private final JPanel panel;
	private final JLabel loadButton;
	
	//Button for user to click on to create a new Whiteboard
	private final JButton newButton;
	
	//Text field for user to input name of Whiteboard to load
	private final JTextField loadName;
	
	//Table containing all Whiteboards available to load
	private final JTable availableCanvases;
	
	public EntryGUI() {
		super("Whiteboard");
		
		//JFrame setup
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setTitle("Welcome to Whiteboard");
		
		//JFrame component initialization
		loadButton = new JLabel("Load Whiteboard:");
		loadButton.setName("loadButton");
		loadButton.setText("Load Whiteboard:");
		
		newButton = new JButton();
		newButton.setName("newButton");
		newButton.setText("Create New");
		
		loadName = new JTextField();
		loadName.setName("loadName");
		loadName.setMinimumSize(new Dimension(200, 25));
		loadName.setMaximumSize(new Dimension(1000, 25));
		
		addListeners();
		
		DefaultTableModel model = new DefaultTableModel();
		availableCanvases = new JTable() {
			
			@Override
			//makes the cells of availableCanvases uneditable
			public boolean isCellEditable(int row, int column) {
				return false;
			}
			
		};
		availableCanvases.setRowSelectionAllowed(false);
		availableCanvases.setName("availableCanvases");
		model.addColumn("Canvases");
		availableCanvases.setModel(model);
		availableCanvases.removeEditor();
		
		//fill availableCanvases with canvases saved on the server
		fillTable();
		
		//organizing layout of EntryGUI
		panel = new JPanel();
		panel.setBorder(new EmptyBorder(5, 5, 5, 5));
		this.setContentPane(panel);
		GroupLayout layout = new GroupLayout(panel);
		panel.setLayout(layout);
		layout.setAutoCreateGaps(true);
		layout.setAutoCreateContainerGaps(true);
		layout.setHorizontalGroup(layout.createParallelGroup()
				.addGroup(layout.createSequentialGroup()
						.addComponent(loadButton)
						.addComponent(loadName)
						.addComponent(newButton))
				.addComponent(availableCanvases));
		layout.setVerticalGroup(layout.createParallelGroup()
				.addGroup(layout.createSequentialGroup()
						.addGroup(layout.createParallelGroup()
								.addComponent(loadButton)
								.addComponent(loadName)
								.addComponent(newButton))
				.addComponent(availableCanvases)));
		
		//frame the JottoGUI around the default components
		this.pack();
	}
	
	/**
	 * add and bind listeners to the EntryGUI components
	 * components must already be initialized
	 */
	private void addListeners() {
		
		//add listener for enter key stroke while cursor is in loadName text field
		loadName.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				//TODO: load WhiteboardGUI
			}
		});
		
		//add listener for mouse clicks on newButton
		newButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				SwingUtilities.invokeLater(new Runnable() {
					public void run() {
						NameGUI name = new NameGUI();
						name.setVisible(true);
					}
				});
			}
			
		});
	}
	
	private synchronized void fillTable() {
		//TODO: fill table with saved Canvases
	}
	
	/**
	 * This class contains code for a NameGUI, which is 
	 * created when the newButton of EntryGUI is clicked on.
	 * The NameGUI is used for inputting the name of the 
	 * Whiteboard about to be created
	 * 
	 * TODO: thread safety argument
	 * 
	 * TODO: testing strategy
	 * 
	 * @author krwang
	 *
	 */
	private class NameGUI extends JFrame{
		
		private final JPanel panel;
		
		//Button for user to click on to create a new Whiteboard
		private final JButton create;
		
		//Text field for user to enter the name of the new Whiteboard
		private final JTextField name;
		
		private NameGUI() {
			super("New Whiteboard");
			
			//JFrame setup
			this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			this.setTitle("New Whiteboard");
			
			create = new JButton();
			create.setName("create");
			create.setText("Create Under Name:");
			
			name = new JTextField();
			name.setName("name");
			name.setMinimumSize(new Dimension(200, 25));
			name.setMaximumSize(new Dimension(1000, 25));
			
			addListeners();
			
			panel = new JPanel();
			panel.setBorder(new EmptyBorder(5, 5, 5, 5));
			this.setContentPane(panel);
			GroupLayout layout = new GroupLayout(panel);
			panel.setLayout(layout);
			layout.setAutoCreateGaps(true);
			layout.setAutoCreateContainerGaps(true);
			layout.setHorizontalGroup(layout.createParallelGroup()
					.addGroup(layout.createSequentialGroup()
							.addComponent(create)
							.addComponent(name)));
			layout.setVerticalGroup(layout.createParallelGroup()
					.addGroup(layout.createSequentialGroup()
							.addGroup(layout.createParallelGroup()
									.addComponent(create)
									.addComponent(name))));
			
			//frame the NameGUI around the default components
			this.pack();
		}
		
		/**
		 * add and bind listeners to the NameGUI components
		 * components must already be initialized
		 */
		private void addListeners() {
			
			//add listener for mouse clicks on create button
			create.addActionListener(new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent e) {
					//TODO: Save name
					SwingUtilities.invokeLater(new Runnable() {
						public void run() {
							WhiteboardGUI main = new WhiteboardGUI();
							main.setVisible(true);
						}
					});
				}
			});
			
			//add listener for enter key stroke while cursor is in the name text field
			name.addActionListener(new ActionListener() {
			
				@Override
				public void actionPerformed(ActionEvent e) {
					//TODO: Save name
					SwingUtilities.invokeLater(new Runnable() {
						public void run() {
							WhiteboardGUI main = new WhiteboardGUI();
							main.setVisible(true);
						}
					});
				}
			});
			
		}
	}
	
	//DELETE THIS AT SOME POINT
	public static void main(final String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				EntryGUI main = new EntryGUI();
				main.setVisible(true);
			}
		});
	}
	
}
