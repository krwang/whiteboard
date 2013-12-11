package client;

/**
 * To test the entryGUI, we make sure that every input the user enters produces the 
 * correct output, whether it is a new canvas, an existing canvas, or no canvas at all
 * 
 * Tests:
 * a)opening just one canvas (running entryGUI once):
 * 
 * 1)user enters name of new canvas and a username into entryGUI window
 * 		expect creation of new whiteboard GUI with a blank canvas, 
 * 		the input canvas name as title and user's name in username panel 
 * 
 * 2)user enters name of existing canvas and username
 * 		expect creation of existing whiteboardGUI with correct name and user's name in 
 * 		username panel. whiteboard GUI should display any art history that was in it from 
 * 		the previous user 
 * 
 * 3)user enters name of canvas and no username
 * 		expect a popup that says "username field cannot be empty", and then clears the entrygui
 * 		window so that the user can try again
 * 
 * 4)user enters no name for canvas and no name for username
 * 		expect a popup saying "Boardname field cannot be empty" and "Username field cannot be 
 * 		empty", and then another cleared entry gui window in which the user can try again
 *
 * 5)user enters username and no name for canvas
 * 		expect a popup saying "Boardname field cannot be empty" and then another cleared entry
 * 		 gui window in which the user can try again
 * 
 * 
 * b)opening multiple canvases (running entryGUI twice)
 * 
 * 1)user keeps one canvas with a username open and tries open a new canvas with same username
 * 		expect two whiteboards to appear on the screen, one titled with the first canvas name 
 * 		and the right username in the username panel, and the other with the second canvas name
 * 		and that same username in its username panel
 * 
 * 2)user keeps one canvas open and tries to open new canvas with different username
 * 		expect two whiteboards total on the screen. The first whiteboard will contain the first
 * 		canvas and username, the second whitboard will contain the new canvas and differen username
 *
 * 3)user keeps one canvas with username open and tries to open same canvas with different username
 * 		expect two whiteboards to appear on the screen. The first whiteboard contains the first canvas
 * 		and the first username. The second whiteboard contains the same canvas (with the same history)
 * 		and the second username.
 * 
 * 4)user keeps one canvas with username open and tries to open same canvas with same username
 * 		expect the first whiteboard to still remain on the screen, but a Jdialog will pop up telling the 
 * 		user that their input is invalid. The user will then have to enter new data into the enterGUI window 
 * 
 * To test that the GUI buttons are working:
 * 1)test that pressing enter or the load button inputs data and outputs correct whiteboard with
 * correct username
 */
public class EntryGUITest {

}
