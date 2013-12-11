package testing;

/**
 * To test the UsernamePanel class, we will be testing that the username panel outputs the
 * correct usernames for different cases where different whiteboards with users are closed 
 * or opened
 * 
 * Tests:
 * 1)Different Whiteboards same Username
 * 	open Whiteboard 1 with user A. open Whiteboard 2 with user A. 
 * 		expect userpanel for whiteboard 1 and userpanel for whiteboard 2 to
 * 		 display user A
 * 
 * 2)Different Whiteboards different Usernames
 * 	open Whiteboard 1 with user A. open Whiteboard 2 with user B. 
 * 		expect userpanel 1 to display user A, userpanel 2 to display user B
 *
 * 3)Same Whiteboard different Usernames
 * 	open Whiteboard 1 with user A. open Whiteboard 1 with user B
 * 		expect userpanel 1 to display user A and user B
 * 		expect userpanel 2 to display user A and user B
 * 
 * 4)Whiteboard share 3 users, close one of them
 * 	open Whiteboard 1 with user A, open Whiteboard 1 with user B, open Whiteboard 1
 * 	with user C, then close Whiteboard 1 with user B
 * 		expect userpanel 1 to display user A and user C
 * 		expect userpanel 3 to display user A and user C
 * 
 * 5)Whiteboard shares 3 users, then switch one of them to a different whiteboard
 * 	open Whiteboard 1 with user A, open Whiteboard 1 with user B, open Whiteboard 1
 * 	with user C, then switch Whiteboard 1 with user B to Whiteboard2
 * 		expect userpanel 1 to display user A and user C
 * 		expect userpanel from Whiteboard 2 to display user B
 * 		expect userpanel 3 to display user A and user C
 * 
 */
public class UsernamePanelTest {

}
