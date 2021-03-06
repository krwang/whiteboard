package client;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.*;

/**
 * Interface for user-customizable settings for drawing on the canvas. Contains
 * options for brush types, brush sizes, and colors.
 */
@SuppressWarnings("serial")
public class ToolsPanel extends JPanel implements ActionListener {
    
    /**
     * Maximum number of buttons allowed in one row
     */
    private static final int MAX_ROW_BUTTONS = 3;
        
    // GUI COMPONENTS
    /**
     * Toggle button for selecting the draw function
     */
    private final JToggleButton drawButton;
    
    /**
     * Toggle button for selecting the erase function
     */
    private final JToggleButton eraseButton;
    
    /**
     * List containing all of the brush types
     */
    private final ArrayList<JToggleButton> brushButtons;
    
    /**
     * Toggle button for selecting a small brush size
     */
    private final JToggleButton smallButton;
    
    /**
     * Toggle button for selecting a medium brush size
     */
    private final JToggleButton mediumButton;
    
    /**
     * Toggle button for selecting a large brush size
     */
    private final JToggleButton largeButton;
    
    /**
     * List containing all of the brush sizes
     */
    private final ArrayList<JToggleButton> sizeButtons;
    
    /**
     * Toggle button for selecting a black brush color
     */
    private final JToggleButton blackButton;
    
    /**
     * Toggle button for selecting a red brush color
     */
    private final JToggleButton redButton;
    
    /**
     * Toggle button for selecting an orange brush color
     */
    private final JToggleButton orangeButton;
    
    /**
     * Toggle button for selecting a yellow brush color
     */
    private final JToggleButton yellowButton;
    
    /**
     * Toggle button for selecting a green brush color
     */
    private final JToggleButton greenButton;
    
    /**
     * Toggle button for selecting a blue brush color
     */
    private final JToggleButton blueButton;
    
    /**
     * List containing all of the brush colors
     */
    private final ArrayList<JToggleButton> colorButtons;
    
    /**
     * Button for switching Canvases
     */
    private final JButton switchButton;
    
    /**
     * Reference to GUI container
     */
    private final WhiteboardGUI gui;
    
    // METHODS
    /**
     * Default constructor for creating a ToolsPanel
     * Initializes all elements of a ToolsPanel and organizes them
     */
    public ToolsPanel(WhiteboardGUI parent) {
        super();
        
        gui = parent;
        
        Insets standard = new Insets(15, 5, 5, 5);
        
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        
        JLabel brushLabel = new JLabel("Brush");
        gbc.insets = standard;
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 3;
        gbc.gridheight = 1;
        add(brushLabel, gbc);
        
        brushButtons = new ArrayList<JToggleButton>();
        
        drawButton = new JToggleButton();
        drawButton.setIcon(new ImageIcon("resources/draw.png"));
        drawButton.addActionListener(this);
        drawButton.setActionCommand("draw");
        brushButtons.add(drawButton);
        
        eraseButton = new JToggleButton();
        eraseButton.setIcon(new ImageIcon("resources/erase.png"));
        eraseButton.addActionListener(this);
        eraseButton.setActionCommand("erase");
        brushButtons.add(eraseButton);
        
        gbc.gridy = 1;
        addButtons(brushButtons, gbc);
        
        JLabel sizeLabel = new JLabel("Size");
        gbc.insets = standard;
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 3;
        gbc.gridheight = 1;
        add(sizeLabel, gbc);
        
        sizeButtons = new ArrayList<JToggleButton>();
        
        smallButton = new JToggleButton();
        smallButton.setIcon(new ImageIcon("resources/small.png"));
        smallButton.addActionListener(this);
        smallButton.setActionCommand("small");
        sizeButtons.add(smallButton);
        
        mediumButton = new JToggleButton();
        mediumButton.setIcon(new ImageIcon("resources/medium.png"));
        mediumButton.addActionListener(this);
        mediumButton.setActionCommand("medium");
        sizeButtons.add(mediumButton);
        
        largeButton = new JToggleButton();
        largeButton.setIcon(new ImageIcon("resources/large.png"));
        largeButton.addActionListener(this);
        largeButton.setActionCommand("large");
        sizeButtons.add(largeButton);
        
        gbc.gridy = 3;
        addButtons(sizeButtons, gbc);
        
        JLabel colorLabel = new JLabel("Color");
        gbc.insets = standard;
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 3;
        gbc.gridheight = 1;
        add(colorLabel, gbc);
        
        colorButtons = new ArrayList<JToggleButton>();
        
        blackButton = new JToggleButton();
        blackButton.setIcon(new ImageIcon("resources/black.png"));
        blackButton.addActionListener(this);
        blackButton.setActionCommand("black");
        colorButtons.add(blackButton);
        
        redButton = new JToggleButton();
        redButton.setIcon(new ImageIcon("resources/red.png"));
        redButton.addActionListener(this);
        redButton.setActionCommand("red");
        colorButtons.add(redButton);
        
        orangeButton = new JToggleButton();
        orangeButton.setIcon(new ImageIcon("resources/orange.png"));
        orangeButton.addActionListener(this);
        orangeButton.setActionCommand("orange");
        colorButtons.add(orangeButton);
        
        yellowButton = new JToggleButton();
        yellowButton.setIcon(new ImageIcon("resources/yellow.png"));
        yellowButton.addActionListener(this);
        yellowButton.setActionCommand("yellow");
        colorButtons.add(yellowButton);
        
        greenButton = new JToggleButton();
        greenButton.setIcon(new ImageIcon("resources/green.png"));
        greenButton.addActionListener(this);
        greenButton.setActionCommand("green");
        colorButtons.add(greenButton);
        
        blueButton = new JToggleButton();
        blueButton.setIcon(new ImageIcon("resources/blue.png"));
        blueButton.addActionListener(this);
        blueButton.setActionCommand("blue");
        colorButtons.add(blueButton);
        
        gbc.gridy = 5;
        addButtons(colorButtons, gbc);
        
        switchButton = new JButton("Switch Canvas");
        switchButton.addActionListener(this);
		switchButton.setActionCommand("switch");
        gbc.gridx = 0;
        gbc.gridy = 7;
        gbc.gridwidth = 3;
        gbc.insets = new Insets(30, 0, 0, 0);
        add(switchButton, gbc);
        
        initialize();
    }
    
    /**
     * Initializes user settings to default:
     *      drawing brush, small size, black color
     */
    private void initialize() {
        drawButton.setSelected(true);      
        smallButton.setSelected(true);       
        blackButton.setSelected(true);
    }
    
    /**
     * Adds buttons to the ToolsPanel in sequential order of the list. Creates
     * a new row when the maximum number of buttons are added to a row. Uses
     * GridBagLayout to add the buttons.
     * 
     * @param buttons is the list of buttons to be added
     * @param gbc is the GridBagConstraints currently in use
     */
    private void addButtons(ArrayList<JToggleButton> buttons, 
                                                    GridBagConstraints gbc) {
        gbc.insets = new Insets(0, 0, 0, 0);
        gbc.gridwidth = 1;
        for (int i = 0; i < buttons.size(); i++) {
            if (i == 3) {
                gbc.gridy++;
            }
            gbc.gridx = i % MAX_ROW_BUTTONS;
            add(buttons.get(i), gbc);
        }
    }
    
    /**
     * Responds to user events on the ToolsPanel. Changes the respective
     * user setting to the user-selected button and deselects the previous
     * user selection.
     * 
     * @param ae is the ActionEvent that occurred
     */
    @Override
    public void actionPerformed(ActionEvent ae) {
        String command = ae.getActionCommand();
        System.out.println(command);
        if (command.equals("draw")) {
            brushButtons.get(gui.getCurrentBrush()).setSelected(false);
            gui.setCurrentBrush(WhiteboardGUI.DRAW);
        } else if (command.equals("erase")) {
            brushButtons.get(gui.getCurrentBrush()).setSelected(false);
            gui.setCurrentBrush(WhiteboardGUI.ERASE);
        } else if (command.equals("small")) {
            sizeButtons.get(gui.getCurrentSize()).setSelected(false);
            gui.setCurrentSize(WhiteboardGUI.SMALL);
        } else if (command.equals("medium")) {
            sizeButtons.get(gui.getCurrentSize()).setSelected(false);
            gui.setCurrentSize(WhiteboardGUI.MEDIUM);
        } else if (command.equals("large")) {
            sizeButtons.get(gui.getCurrentSize()).setSelected(false);
            gui.setCurrentSize(WhiteboardGUI.LARGE);
        } else if (command.equals("black")) {
            colorButtons.get(gui.getCurrentColor()).setSelected(false);
            gui.setCurrentColor(WhiteboardGUI.BLACK);
        } else if (command.equals("red")) {
            colorButtons.get(gui.getCurrentColor()).setSelected(false);
            gui.setCurrentColor(WhiteboardGUI.RED);
        } else if (command.equals("orange")) {
            colorButtons.get(gui.getCurrentColor()).setSelected(false);
            gui.setCurrentColor(WhiteboardGUI.ORANGE);
        } else if (command.equals("yellow")) {
            colorButtons.get(gui.getCurrentColor()).setSelected(false);
            gui.setCurrentColor(WhiteboardGUI.YELLOW);
        } else if (command.equals("green")) {
            colorButtons.get(gui.getCurrentColor()).setSelected(false);
            gui.setCurrentColor(WhiteboardGUI.GREEN);
        } else if (command.equals("blue")) {
            colorButtons.get(gui.getCurrentColor()).setSelected(false);
            gui.setCurrentColor(WhiteboardGUI.BLUE);
        } else if (command.equals("switch")) {
        	try {
                new EntryGUI(gui.getClientIP()).setVisible(true);
                gui.dispose();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("Action not recognized");
        }
    }
}