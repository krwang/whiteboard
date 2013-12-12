package testing;

/**
 * To test the ToolsPanel class, we will be testing that all of the corresponding
 * colors, sizes, and types of brushes are chosen when the user clicks on a 
 * certain buttons in the tools panel.
 * 
 * Tests:
 * 1)To test the various color swatch buttons, we clicked on the buttons and 
 * reviewed the print statements that we wrote in the actionPerformed method
 * to see that the appropriate color name was printed out. 
 * 		-We will click on each of the buttons (red, orange, yellow, green, blue, black)
 *		and make sure that the corresponding color name is printed out in the console
 * 		-We can also double check by making sure that the color drawn on the whiteboard
 * 		GUI's Canvas corresponds to the one clicked on the toolspanel because the canvas
 * 		directly uses the colors returned by the tools panel
 * 
 * 2) To test the various brush size buttons, we clicked on the buttons and 
 * reviewed the print statements that we wrote in the actionPerformed method
 * to see that the appropriate brush size name was printed out. 
 * 		-We will click on each of the buttons (small, medium, and large brush sizes)
 *		and make sure that the corresponding brush size name(small, medium large) is printed
 *		out in the console
 * 		-We can also double check by making sure that the brush size used in the whiteboard
 * 		GUI's Canvas corresponds to the one clicked on the toolspanel because the canvas
 * 		directly uses the brush size returned by the tools panel
 * 
 * 3) To test the various brush type buttons, we clicked on the buttons and 
 * reviewed the print statements that we wrote in the actionPerformed method
 * to see that the appropriate brush type name was printed out. 
 * 		-We will click on each of the buttons (draw, erase) and make sure that the corresponding
 * 		 brush size name(draw, erase) is printed out in the console
 * 		-We can also double check by making sure that the brush type used in the whiteboard
 * 		GUI's Canvas corresponds to the one clicked on the toolspanel because the canvas
 * 		directly uses the brush size returned by the tools panel 
 * 			so the user will be drawing in color if they click the draw button and in white if 
 * 			they click the erase button
 * 
 * @category no_didit
 */
public class ToolsPanelTest {

}
