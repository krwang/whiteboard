package client;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.*;

/**
 * Interface for user-customizable settings for drawing on the canvas. Contains
 * options for brush types, brush sizes, and colors.
 * 
 * Each toolsPanel is linked with a User that keeps track ot the currentBrush and size and 
 * color selected....is this even necessary?????????
 * TODO:
 *      Change size buttons to JSlider
 *      Change color buttons to JColorChooser
 */
@SuppressWarnings("serial")
public class ToolsPanel extends JPanel implements ActionListener {
//    // CLASS CONSTANTS
//    // all values refer to respective index in relevant list
//    /**
//     * Brush setting to draw
//     */
//    public static final int DRAW = 0;
//    
//    /**
//     * Brush setting to erase
//     */
//    public static final int ERASE = 1;
//    
//    /**
//     * Brush size to small
//     */
//    public static final int SMALL = 0;
//    
//    /**
//     * Brush size to medium
//     */
//    public static final int MEDIUM = 1;
//    
//    /**
//     * Brush size to large
//     */
//    public static final int LARGE = 2;
//    
//    /**
//     * Brush color to black (#000000)
//     */
//    public static final int BLACK = 0;
//    
//    /**
//     * Brush color to red (#FF0000)
//     */
//    public static final int RED = 1;
//    
//    /**
//     * Brush color to orange (#FF7700)
//     */
//    public static final int ORANGE = 2;
//    
//    /**
//     * Brush color to yellow (#FFFF00)
//     */
//    public static final int YELLOW = 3;
//    
//    /**
//     * Brush color to green (#00FF00)
//     */
//    public static final int GREEN = 4;
//    
//    /**
//     * Brush color to blue (#0000FF)
//     */
//    public static final int BLUE = 5;
    
    /**
     * Maximum number of buttons allowed in one row
     */
    private static final int MAX_ROW_BUTTONS = 3;
    
//    // INSTANCE VARIABLES
//    /**
//     * Current brush setting (see brush constants)
//     */
//    private int currentBrush;
//    
//    /**
//     * Current brush size (see size constants)
//     */
//    private int currentSize;
//    
//    /**
//     * Current brush color (see color constants)
//     */
//    private int currentColor;
    
    
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
//        currentBrush = DRAW;<-done in the user class
        
        smallButton.setSelected(true);
//        currentSize = SMALL;
        
        blackButton.setSelected(true);
//        currentColor = BLACK;
    }
    
    /**
     * Adds buttons to the ToolsPanel in sequential order of the list. Creates
     * a new row when the maximum number of buttons are added to a row. Uses
     * GridBagLayout to add the buttons.
     * 
     * @param buttons The list of buttons to be added
     * @param gbc The GridBagConstraints currently in use
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
     * @param ae The ActionEvent that occurred
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
//            currentSize = SMALL;
            gui.setCurrentSize(WhiteboardGUI.SMALL);
        } else if (command.equals("medium")) {
            sizeButtons.get(gui.getCurrentSize()).setSelected(false);
//            currentSize = MEDIUM;
            gui.setCurrentSize(WhiteboardGUI.MEDIUM);
        } else if (command.equals("large")) {
            sizeButtons.get(gui.getCurrentSize()).setSelected(false);
//            currentSize = LARGE;
            gui.setCurrentSize(WhiteboardGUI.LARGE);
        } else if (command.equals("black")) {
            colorButtons.get(gui.getCurrentColor()).setSelected(false);
//            currentColor = BLACK;
            gui.setCurrentColor(WhiteboardGUI.BLACK);
        } else if (command.equals("red")) {
            colorButtons.get(gui.getCurrentColor()).setSelected(false);
//            currentColor = RED;
            gui.setCurrentColor(WhiteboardGUI.RED);
        } else if (command.equals("orange")) {
            colorButtons.get(gui.getCurrentColor()).setSelected(false);
//            currentColor = ORANGE;
            gui.setCurrentColor(WhiteboardGUI.ORANGE);
        } else if (command.equals("yellow")) {
            colorButtons.get(gui.getCurrentColor()).setSelected(false);
//            currentColor = YELLOW;
            gui.setCurrentColor(WhiteboardGUI.YELLOW);
        } else if (command.equals("green")) {
            colorButtons.get(gui.getCurrentColor()).setSelected(false);
//            currentColor = GREEN;
            gui.setCurrentColor(WhiteboardGUI.GREEN);
        } else if (command.equals("blue")) {
            colorButtons.get(gui.getCurrentColor()).setSelected(false);
//            currentColor = BLUE;
            gui.setCurrentColor(WhiteboardGUI.BLUE);
        } else if (command.equals("switch")) {
        	new EntryGUI().setVisible(true);
			gui.dispose();
        } else {
            System.out.println("Action not recognized");
        }
    }
    
//    /**
//     * Gets the current size of the brush selected
//     * 
//     * @return current size of brush
//     */
//    private int getCurrentSize(){
//    	return currentSize;
//    }
//    
//    /**
//     * Gets the current brush type selected
//     * 
//     * @return current brush type
//     */
//    private int getCurrentBrush(){
//    	return currentBrush;
//    }
//   
//    /**
//     * Gets the current color of the brush selected
//     * 
//     * @return current brush color
//     */
//    private int getCurrentColor(){
//    	return currentColor;
//    }
    
//    /**
//     * For testing purposes
//     * 
//     * @param args Standard command line input
//     */
//    public static void main(String[] args) {
//        final JFrame frame = new JFrame();
//        frame.add(new ToolsPanel());
//        frame.pack();
//        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//        frame.setLocationRelativeTo(null);
//        SwingUtilities.invokeLater(new Runnable() {
//            @Override
//            public void run() {
//                frame.setVisible(true);
//            }
//        });
//    }
}