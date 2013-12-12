package testing;

/**
 * To test the client, we will the passing and receiving of messages:
 * 1) Passing an add request to the server:
 * 		a) the request should be formatted in a way the server action grammar can understand
 * 		b) we should be able to receive the response from the server, open a WhiteboardGUI,
 * 		   perform all the draw actions sent from the server, and update the UsernamePanel according
 * 		   to the usernames sent from the server.
 * 2) Passing a draw request to the server:
 * 		a) the request should be formatted in a way the server action grammar can understand
 * 		b) we should be able to receive the response from the server and update the canvas accordingly
 * 3) Passing a bye request to the server:
 * 		a) the request should be formatted in a way the server action grammar can understand
 * 		b) we should be able to receive the server response, close the canvas, and all other clients
 * 		   should see that the client disconnected by the UsernamePanel updating
 * 
 * @category no_didit
 */
public class WhiteboardClientTest {

}
