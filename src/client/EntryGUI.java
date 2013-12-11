package client;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;

//TODO: add swing workers

/**
 * This class contains code for a EntryGUI. The EntryGUI allows the user to 
 * either create a new Whiteboard and input the name of the Whiteboard
 * or load a previously created Whiteboard by name. 
 * 
 * TODO: thread safety argument
 * 
 * TODO: testing strategy
 */
@SuppressWarnings("serial")
public class EntryGUI extends JFrame implements ActionListener {

	private final JPanel panel;
	
	/**
	 * Label for whiteboard field
	 */
	private final JLabel boardLabel;
	
	/**
	 * Text field for user to input name of Whiteboard to load
	 */
	private final JTextField boardField;
	
	/**
	 * Label for username field
	 */
    private final JLabel userLabel;
    
    /**
     * Text field for user to enter the name of the new Whiteboard
     */
    private final JTextField userField;
	
    /**
     * Button for user to click on to create a new Whiteboard
     */
    private final JButton newButton;
    
    /**
     * Socket used to communicate with the WhiteboardServer
     */
    private final Socket socket;
    
	//List containing all Whiteboards available to load
	//private final JList<String> availableCanvases;
		
	public EntryGUI() throws IOException {
		super();
		
        socket = new Socket("localhost", 5050);
		
		panel = new JPanel();
        panel.setBorder(new EmptyBorder(5, 5, 5, 5));
        
		panel.setLayout(new GridBagLayout());
		
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.insets = new Insets(3, 3, 3, 3);
		gbc.fill = GridBagConstraints.BOTH;
		
		//JFrame component initialization
		boardLabel = new JLabel("Whiteboard:");
		boardLabel.setName("boardLabel");
		gbc.gridx = 0;
		gbc.gridy = 0;
		panel.add(boardLabel, gbc);
		
		boardField = new JTextField();
		boardField.setName("loadName");
		boardField.setColumns(15);
		boardField.setMinimumSize(new Dimension(200, 25));
		boardField.setMaximumSize(new Dimension(1000, 25));
		boardField.addActionListener(this);
		gbc.gridx = 1;
		panel.add(boardField, gbc);
		
		userLabel = new JLabel("Username:");
        userLabel.setName("create");
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.VERTICAL;
        gbc.anchor = GridBagConstraints.EAST;
        panel.add(userLabel, gbc);
        
        userField = new JTextField();
        userField.setName("name");
        userField.setMinimumSize(new Dimension(200, 25));
        userField.setMaximumSize(new Dimension(1000, 25));
        userField.addActionListener(this);
        gbc.fill = GridBagConstraints.BOTH;
        gbc.gridx = 1;
        panel.add(userField, gbc);
		
        newButton = new JButton("Load");
        newButton.setName("newButton");
        newButton.addActionListener(this);
        gbc.gridy = 2;
        gbc.fill = GridBagConstraints.VERTICAL;
        gbc.anchor = GridBagConstraints.EAST;
        panel.add(newButton, gbc);
        
		//addListeners(this);
		
        //DefaultListModel<String> model = new DefaultListModel<String>();
		//availableCanvases = new JList<String>(model);
		
		//fill availableCanvases with canvases saved on the server
		//fillList();
		
		//frame the JottoGUI around the default components
		setContentPane(panel);
		setResizable(false);
	    setTitle("Welcome to Whiteboard");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLocationRelativeTo(null);
		pack();
	}
	
	public void actionPerformed(ActionEvent ae) {
	    boolean valid = true;
	    String username = userField.getText();
	    String boardname = boardField.getText();
	    
	    StringBuilder errorText = new StringBuilder("<html>");
	    
	    try {
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter out = new PrintWriter(socket.getOutputStream(),true);
            try {
                // check that username is available
                out.println("username " + username);
                
                String line = in.readLine();
                if (line.equals("unavailable")) {
                    errorText.append("Username field cannot be empty.");
                    valid = false;
                } else if (line.equals("contains")) {
                    errorText.append("This username is not available.");
                    valid = false;
                }
            } finally {
                out.close();
                in.close();
            }
        } catch(IOException e) {
            e.printStackTrace();
        }
	    
	    if (boardname.isEmpty()) {
	        if (errorText.length() > 5) {
	            errorText.append("<br>");
	        }
	        
	        errorText.append("Whiteboard field cannot be empty.");
	        valid = false;
	    }
	    
	    errorText.append("</html>");
	    
	    if (valid) {
	    	try {
				new WhiteboardClient(username, boardname, socket);
			} catch (IOException e) {
				e.printStackTrace();
			}
	    	dispose();
//	        final WhiteboardGUI whiteboard = new WhiteboardGUI(boardField.getText());
//	        SwingUtilities.invokeLater(new Runnable() {
//	            public void run() {
//	                whiteboard.setVisible(true);
//	                dispose();
//	            }
//	        });
	    } else {
	        final JDialog error = new JDialog(this, "Error", true);
	        error.setLayout(new GridBagLayout());
	        error.add(new JLabel(errorText.toString(), SwingConstants.CENTER));
	        error.pack();
	        error.setSize(new Dimension(250, 100));
	        error.setResizable(false);
	        error.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
	        error.setLocationRelativeTo(this);
	        SwingUtilities.invokeLater(new Runnable() {
	            public void run() {
	                error.setVisible(true);
	            }
	        });
	    }
	}
	
//	/**
//	 * add and bind listeners to the EntryGUI components
//	 * components must already be initialized
//	 */
//	private void addListeners(final EntryGUI gui) {
//		
//		//add listener for enter key stroke while cursor is in loadName text field
//		loadName.addActionListener(new ActionListener() {
//			
//			@Override
//			public void actionPerformed(ActionEvent e) {
//				//TODO: load WhiteboardGUI
//			}
//		});
//		
//		//add listener for mouse clicks on newButton
//		newButton.addActionListener(new ActionListener() {
//			
//			@Override
//			public void actionPerformed(ActionEvent e) {
//				SwingUtilities.invokeLater(new Runnable() {
//					public void run() {
//						NameGUI name = new NameGUI();
//						name.setVisible(true);
//						gui.dispose();
//					}
//				});
//			}
//		});
//	}
	
//	/**
//	 * This class contains code for a NameGUI, which is 
//	 * created when the newButton of EntryGUI is clicked on.
//	 * The NameGUI is used for inputting the name of the 
//	 * Whiteboard about to be created
//	 * 
//	 * thread safety argument
//	 * 
//	 * testing strategy
//	 * 
//	 * @author krwang
//	 *
//	 */
//	private class NameGUI extends JFrame{
//		
//		private final JPanel panel;
//		
//		//Button for user to click on to create a new Whiteboard
//		private final JLabel create;
//		
//		//Text field for user to enter the name of the new Whiteboard
//		private final JTextField name;
//		
//		private NameGUI() {
//			super("New Whiteboard");
//			
//			//JFrame setup
//			this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
//			this.setTitle("New Whiteboard");
//			
//			create = new JLabel();
//			create.setName("create");
//			create.setText("Create Under Name:");
//			
//			name = new JTextField();
//			name.setName("name");
//			name.setMinimumSize(new Dimension(200, 25));
//			name.setMaximumSize(new Dimension(1000, 25));
//			
//			addListeners(this);
//			
//			panel = new JPanel();
//			panel.setBorder(new EmptyBorder(5, 5, 5, 5));
//			this.setContentPane(panel);
//			GroupLayout layout = new GroupLayout(panel);
//			panel.setLayout(layout);
//			layout.setAutoCreateGaps(true);
//			layout.setAutoCreateContainerGaps(true);
//			layout.setHorizontalGroup(layout.createParallelGroup()
//					.addGroup(layout.createSequentialGroup()
//							.addComponent(create)
//							.addComponent(name)));
//			layout.setVerticalGroup(layout.createParallelGroup()
//					.addGroup(layout.createSequentialGroup()
//							.addGroup(layout.createParallelGroup()
//									.addComponent(create)
//									.addComponent(name))));
//			
//			//frame the NameGUI around the default components
//			this.pack();
//		}
//		
//		/**
//		 * add and bind listeners to the NameGUI components
//		 * components must already be initialized
//		 */
//		private void addListeners(final NameGUI gui) {
//			
//			//add listener for enter key stroke while cursor is in the name text field
//			name.addActionListener(new ActionListener() {
//			
//				@Override
//				public void actionPerformed(ActionEvent e) {
//					//TODO: Save name on server, ensure name is unique
//					SwingUtilities.invokeLater(new Runnable() {
//						public void run() {
//							UserGUI user = new UserGUI(name.getText());
//							user.setVisible(true);
//							gui.dispose();
//						}
//					});
//				}
//			});
//			
//		}
//	}
//	
//	/**
//	 * class javadoc
//	 * @author krwang
//	 */
//	
//	private class UserGUI extends JFrame{
//		
//		private final JPanel panel;
//		
//		//Text field for user to enter the name of the new Whiteboard
//		private final JTextField name;
//		
//		
//		private UserGUI(String whiteboardName) {
//			super("Enter a Username");
//					
//			//JFrame setup
//			this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
//			this.setTitle("Enter a Username");
//			
//			name = new JTextField();
//			name.setName("name");
//			name.setMinimumSize(new Dimension(300, 25));
//			name.setMaximumSize(new Dimension(1000, 25));
//			
//			addListeners(this, whiteboardName);
//			
//			panel = new JPanel();
//			panel.setBorder(new EmptyBorder(5, 5, 5, 5));
//			this.setContentPane(panel);
//			GroupLayout layout = new GroupLayout(panel);
//			panel.setLayout(layout);
//			layout.setAutoCreateGaps(true);
//			layout.setAutoCreateContainerGaps(true);
//			layout.setHorizontalGroup(layout.createParallelGroup()
//					.addComponent(name));
//			layout.setVerticalGroup(layout.createParallelGroup()
//					.addComponent(name));
//			
//			//frame the NameGUI around the default components
//			this.pack();
//		}
//		
//		/**
//		 * add and bind listeners to the NameGUI components
//		 * components must already be initialized
//		 */
//		private void addListeners(final UserGUI gui, final String whiteboardName) {
//			
//			//add listener for enter key stroke while cursor is in the name text field
//			name.addActionListener(new ActionListener() {
//			
//				@Override
//				public void actionPerformed(ActionEvent e) {
//					SwingUtilities.invokeLater(new Runnable() {
//						public void run() {
//							if (!name.getText().equals("") && !name.getText().equals("ENTER A USERNAME STUPID")) {
//								//TODO: save username to canvas
//								WhiteboardGUI whiteboard = new WhiteboardGUI(whiteboardName);
//								whiteboard.setVisible(true);
//								gui.dispose();
//							}
//							else {
//								name.setText("ENTER A USERNAME STUPID");
//								name.setSelectionStart(0);
//								name.setSelectionEnd(name.getText().length());
//							}
//						}
//					});
//				}
//			});
//			
//		}
//	}
	
	//DELETE THIS AT SOME POINT maybe...
	public static void main(final String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
                try {
                    new EntryGUI().setVisible(true);
                } catch (IOException e) {
                    e.printStackTrace();
                }
			}
		});
	}	
}