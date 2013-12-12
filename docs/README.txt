Place your team contract and design documents in this directory.

The files in this directory should be:
- team-contract.pdf
- design-milestone.pdf
- design-revised.pdf

If you add any other documents to this directory, please add the filenames to
the above list.

HOW TO RUN THE SERVER:
	All fields are already set in the server as class constants, so all that is
required is to simply run the program and it will open up a socket on port 5050.

HOW TO RUN THE CLIENT:
	The client should be run using the EntryGUI. This will start a GUI from
which the user can join or create a whiteboard. This EntryGUI requires no
parameters to start, so it simply needs to be run to start the client. The
socket is opened on localhost (127.0.0.1) by default, but a different IP
address can be set by passing a single argument into the EntryGUI: the argument
must be a valid IP address of the form ###.###.###.###, where each of the four
fields must be between 0-255.