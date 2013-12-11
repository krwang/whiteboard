package client;

/**
 * To test the entryGUI, we make sure that every input the user enters produces the 
 * correct output, whether it is a new canvas, an existing canvas, or no canvas at all
 * 
 * Tests:
 * a)opening just one canvas (running entryGUI once):
 * 1)user enters name of new canvas and a username
 * 		expect creation of new whiteboard GUI with the input canvas name as title and 
 * 		user's name in username panel 
 * 2)user enters name of existing canvas and username
 * 		expect creation of existing whiteboardGUI with correct name and user's name in 
 * 		username panel. whiteboard GUI should display any art history that was in it from 
 * 		the previous user 
 * 3)user enters name of canvas and no username
 * 4)user enters no name for canvas and username
 * 5)user enters nothing
 * 
 * b)opening multiple canvases (running entryGUI twice)
 * 1)user keeps one canvas with a username open and tries open a new canvas with same username
 * 2)user keeps one canvas open and tries to open new canvas with different username
 * 3)user keeps one canvas with username open and tries to open same canvas with different username
 * 4)user keeps one canvas with username open and tries to open same canvas with same username
 * 
 * TO test that the GUI buttons are working:
 * 1)test that pressing enter or the load button inputs data and outputs correct whiteboard with
 * correct username
 *
 */
public class EntryGUITest {

}
