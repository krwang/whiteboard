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

/**
 * This class contains code for a EntryGUI. The EntryGUI allows the user to 
 * enter a name of a canvas and an username. If the canvas exists on the server,
 * the canvas will be loaded from the server. Otherwise, a new canvas is created.
 * 
 * The username is used by the server to identify a user and to indicate to other users
 * connected to the canvas that a new user has connected
 * 
 * Clients can open two different canvases using the same username. 
 * The client can only also open multiple copies of the same canvas under the same 
 * username, though the user is still the same in both
 * 
 * For testing strategy, please see the EntryGUITest.java class
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
     * Initializes and organizes all elements of the EntryGUI,
     * using a GridBag layout
     *  
     * @throws IOException
     */
	public EntryGUI() throws IOException {
		super();
		
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
		
		//frame the JottoGUI around the default components
		setContentPane(panel);
		setResizable(false);
	    setTitle("Welcome to Whiteboard");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLocationRelativeTo(null);
		pack();
	}
	
	/**
	 * This method is called any time the user hits the enter key or 
	 * mouse clicks on the load button. It parses the inputs into the username 
	 * and boardname text fields and does one of two things with the inputs.
	 * If one of the inputs is empty (ie. is the empty string ""), the GUI rejects that
	 * submission, popping up a JDialog that indicates the user error. If neither input
	 * is empty, it sends the input to the server, which then checks that nobody
	 * under the same username is currently connected to the canvas under the boardname.
	 * If so, the server rejects the username and a JDialog box pops up indicating the 
	 * error. Otherwise, a new WhiteboardGUI appears.
	 * 
	 * Precondition: Boardname and username must contain only alphanumerics (no symbols). 
	 * 				 The server will hit an exception if either contains a non-alphanumeric. 
	 */
	public void actionPerformed(ActionEvent ae) {
		
		//indicator variable for whether the user inputs are valid
		boolean valid = true;
		
	    String username = userField.getText();
	    String boardname = boardField.getText();
	    
	    StringBuilder errorText = new StringBuilder("<html>");
	    
	    try {
	    	//create a socket for communication with the server
	        Socket socket = new Socket("localhost", 5050);
	        
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter out = new PrintWriter(socket.getOutputStream(),true);
            
            try {
            	
            	//if neither input is empty
                if(!boardname.equals("") && !(username.equals(""))) {
                	
                	//send a request to the server through the socket output stream
	                out.println("username " + boardname  + " " + username);
	
	                //read the response from the server
	                String line = in.readLine();
	                if (line.equals("contains")) {
	                    errorText.append("This username is not available.");
	                    valid = false;
	                }
                }
                
                //if either input is empty
                else {
                	if (boardname.equals("")) {
                		errorText.append("Boardname field cannot be empty <br>");
                	}
                	if (username.equals("")) {
                		errorText.append("Username field cannot be empty.");
                	}
                    valid = false;
                }
                
            } 
            finally {
                out.close();
                in.close();
                socket.close();
            }
        } 
	    catch(IOException e) {
            e.printStackTrace();
        }

	    errorText.append("</html>");
	    
	    //create a WhiteboardClient, which starts a WhiteboardGUI
	    if (valid) {
	    	try {
				new WhiteboardClient(username, boardname);
			} catch (IOException e) {
				e.printStackTrace();
			}
	    	dispose();
	    } 
	    
	    //if the server says the username is in use or there is an empty input
	    else {
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
	
	/**
	 * To start a new EntryGUI, which allows the user to 
	 * connect to the server and open a WhiteboardGUI with
	 * a canvas
	 */
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