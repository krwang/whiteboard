package server;

public class WhiteboardServerTest {
	/**
	 * To test the server, we want to make sure that the correct output string 
	 * is produced when the client's input string is entered. We will test this by
	 * inserting print statements in our code and making sure that the printed
	 * lines correctly correspond with the input
	 * 
	 * To test the handleRequest method: 
	 * 1) To test "add" token:
	 * 	  a)input = "add boardname username" when boardname does not exist in canvasMap
	 * 	  -check that output = {canvas moves, boardname, username}
	 * 	  -check that list of sockets under that boardname in the sockets hashmap
	 *    contains the new socket
	 *    -check that there exists a new blank arraylist of canvas moves mapped to the board name 
	 *    in the canvas moves map
	 *    -check the boardnames to users hashmap maps the input boardname to the input username
	 *    
	 * 	  b)input = "add boardname username" when boardname already exists in canvasMap
	 *    -check that output = {canvas moves, boardname, username}
	 * 	  -check that list of sockets under that boardname in the sockets hashmap
	 *    contains the new socket
	 *    -check that there exists an arraylist of canvas moves that corresponds to the canvas 
	 *    moves made by prior clients to that whiteboard, which ismapped to the board name 
	 *    in the canvas moves map
	 *    -check that the input boardname is mapped to an arraylist containing the input username in
	 *    the usersOnCanvas hashmap
	 * 
	 * 2) To test "draw" token:
	 *    a)input = "draw 2 0 1 1 18 20 boardname" (draws a red small line from (1,1) to (18,20)
	 *    -check that output = {draw 2 0 1 1 18 20, boardname}
	 *    -check that the input is added to the hashmap mapping boardnames to past canvas moves
	 *    
	 * 4) To test "bye" token: 
	 * 		a)input = "bye boardName userName"
	 * 		check that output = {bye boardName userName, boardName}
	 * 		-check that the username is removed from the usersOnCanvas hashmap
	 * 		-check that the socket is removed from the sockets hashmap
	 *	
	 * 5) To test "username" token:
	 * 		a)input = "username boardName userName" when userName is already contained in the 
	 * 		boardName to usernames hashmap
	 * 		- check that output = "contains"
	 * 
	 * 		b)input = "username boardName userName" when userName is not contained in the 
	 * 		usersOnCanvas hashmap
	 * 		- check that output = "username good"
	 * 
	 * 6) To test "get" token
	 * 		a)input = "get board boardname"
	 * 		- check that output is the arraylist of moves that are mapped to that boardname
	 * 
	 * 		b)input = "get thread boardname"
	 * 		- check that output is an integers that equals the threadcount
	 * 
	 * to test the sendOutput method, we will send in dummy inputs that would have been processed
	 * by handleRequest and then further processed in this method
	 * 1) to test input = {bye boardName userName, socket, printwriter}
	 * 		check that tokens[0] = "bye" with a print line
	 * 		check usersOnCanvas no longer contains the input unserName as mapped to boardName
	 * 		check that the arraylist of sockets for the boardname no longer contains input socket 
	 * 
	 * 2) test input = {add boardName userName, socket, printwriter} 
	 * 		check that tokens[0] = "add"
	 * 		check that line is being printed out for all sockets connected to same board
	 * 
	 * 3) test input = {draw color size x1 y1 x2 y2 boardName, socket, printwriter} 
	 * 		check that tokens[0] = "input"
	 * 		check that line is being printed out for all sockets connected to same board
	 * 
	 * 4) test input = {username boardName userName, socket, printwriter} 
	 * 		check that tokens[0] = username
	 * 		check that line is being printed out for all sockets connected to same board
	 * 
	 * 5) test input = {get board boardname, socket, printwriter} 
	 * 		check that tokens[0] = get, tokens[1] = board
	 * 		check that line is being printed out for all sockets connected to same board
	 * 
	 * 6)test input = {get thread boardname, socket, printwriter} 
	 * 		check that tokens[0] = get, tokens[1] = thread
	 * 		check that line is being printed out for all sockets connected to same board
	 * 
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
	 */
}
