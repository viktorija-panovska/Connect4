# Software structure
The program utilizes a basic Model-View-Controller architecture, with the following public classes in the Connect4 package:

- **Main**: plays the role of the controller. It prompts and receives textual input from the user, and calls methods from the View, Model and ComputerPlayer classes.

- **Model**: represents the board and contains methods responsible for retrieving information about the current state of the board and altering the state of the board (dropping or removing a chip in a specified column). 

- **View**: contains methods responsible for displaying the game mode selection menu and the current state of the game board to the command line. Gets the state of the board from the Model class.

- **ComputerPlayer**: contains the implementation of the computer opponent. The algorithm used is the minimax algorithm with a heuristic. Gets the state of the board from the Model class.


The program contains one other class:

- **WinChecker**: inherits from RecursiveTask. Used in the multithreaded implementation of a test for a win condition of the board. Gets the state of the board from the Model class.

This project has no extrenal dependencies.