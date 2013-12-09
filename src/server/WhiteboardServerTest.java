package server;

public class WhiteboardServerTest {
	/**
	 * To test the server, we want to make sure that the correct output string 
	 * is produced when the client's input string is entered. We will test this by
	 * inserting print statements in our code and making sure that the printed
	 * lines correctly correspond with the input
	 * 
	 * To test the handleRequest method with one client: 
	 * 1) To test "add" token:
	 * 	  a)input = "add boardname" when boardname already exists in canvasMap
	 * 	  check that output = {{"add", boardname}, boardname}
	 * 	  check that list of sockets under that boardname in the sockets hashmap
	 *    contains the new socket
	 * 
	 * 2) To test "draw" token:
	 *    a)input = "draw 2 0 1 1 18 20 boardname" (draws a red small line from (1,1) to (18,20)
	 *    check that output = {{draw 2 0 1 1 18 20}, {boardname}}
	 *  
	 *  do i need to check that the master copy has it drawn on it?
	 * 
	 * 3) To test "erase" token:
	 * 	  a)input = "erase  0 6 2 18 20 boardname" (erases a medium line from (1,1) to (18,20)
	 *    check that output = {{draw 2 0 1 1 18 20}, {boardname}}
	 *    
	 * 4) To test "bye" token: 
	 * 		a)input = "bye"
	 * 		check that output = {bye}
	 * 
	 * To test containsUsername method:
	 * 1) test when username is in the arraylist--should return true
	 * 2) test when username is not in arraylist--should return false
	 * 
	 * To test server in general:
	 * 1) adding a new whiteboard and updating canvasMap accordingly
	 * 2) loading an existing whiteboard and updating the canvasMap accordingly
	 * 3) draw on a whiteboard and update canvas accordingly
	 * 4) erase on whiteboard and update canvas accordingly
	 * 5) close whiteboard
	 * 
	 * To test server for concurrency, we'll open the same whiteboard through 2 different 
	 * clients, client 1, and client 2: 
	 * 1) 1 draws, and 2 draws in same location at same time
	 * 2) 1 draws and 2 erases in same location at same time
	 * 3) 1 draws and 2 draws in same location
	 * 4) 1 draws and 2 erases in same location
	 * 5) 1 closes, see if 2's username list updates
	 * 6) 1 is open, then open 2 and see if 1's list updates
	 * 
	 * 
	 * 	 * open a new board--make sure that the canvas is added to the canvasMap under
	 */
}
