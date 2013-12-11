package testing;
/**
 * To test the GUI, we will test several things: 
 * 1) that the GUI setup from the EntryGUI to the WhiteboardGUI are all correct
 * 2) that the whiteboardGUI updates correctly in the presence of threading and multithreading
 * 
 * To test the GUI setup:
 * 1) we first tested the EntryGUI(tests can be found in the EntryGUITest class)
 * 2) we tested that the toolsPanel performed the appropriate commands of color and size and 
 *    type of brush (tests found in ToolsPanelTest class)
 * 3) we tested that the userNamePanel always contained the correct number and names of the users
 *    currently accessing the same whiteboard (tests found in UsernamePanel class)
 *
 * Testing multiple users accessing the same canvas:
 * 1) We tested that, when two or more users are connected to the same canvas, these things happened appropriately:
 * 		a) The Jlist of usernames on the usernamePanel displayed all the users currently connected and 
 * 		   updated when a user connected or disconnected
 * 		b) When one user draws on their canvas, the canvases and GUIs for all the other users display that draw
 * 		c) When opening a canvas that had already been modified on the server, the canvas and GUI display the
 * 		   drawings the server stored
 * 		d) The switch button closes the WhiteboardGUI and opens a new EntryGUI for the user to connect to another
 * 		   canvas
 * 		e) When toggling buttons on the ToolsPanel, the canvas should reflect those choices when drawing
 */
public class WhiteboardGUITest {

}
