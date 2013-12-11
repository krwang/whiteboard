package client;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.DefaultListModel;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;

/**
 * This class contains code for the username panel that displays
 * the usernames of all of the clients currently accessing the 
 * same whiteboard. The panel will be contained in the WhiteboardGUI
 */
@SuppressWarnings("serial")
public class UsernamePanel extends JPanel{
	private final JLabel title;
	final JList<String> usernameList;

	/**
	 * constructs a username panel
	 * @param parent   whiteboard gui that the username panel will be added to
	 */
	public UsernamePanel(WhiteboardGUI parent) {
		super();
				
		setLayout(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.fill = GridBagConstraints.BOTH;
		gbc.insets = new Insets(3, 3, 3, 3);
		
		title = new JLabel("Users currently editing:");
		title.setText("Users currently editing:");
		gbc.gridx = 0;
		gbc.gridy = 0;
		add(title, gbc);
		
		usernameList = new JList<String>();
		usernameList.setModel(new DefaultListModel<String>());
		usernameList.setName("usernameList");		
		gbc.gridy = 1;
		add(usernameList, gbc);
	}
}
