package client;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.*;

/**
 * this class contains code for the username panel
 * that will be contained in the WhiteboardGUI
 * @author bschang
 *
 */
@SuppressWarnings("serial")
public class UsernamePanel extends JPanel{
	private final JLabel title;
	final JList<String> usernameList;
	private final WhiteboardGUI gui;

	public UsernamePanel(WhiteboardGUI parent) {
		super();
		
		gui = parent;
		
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
