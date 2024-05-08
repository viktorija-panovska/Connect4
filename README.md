# Running the project
The project is built using Maven.

To launch the project, open the command line and navigate to the project directory (the folder containing the pom.xml file). 

First, the project must be compiled. Execute the command:

    >mvn compile

Then, to launch the game, execute the command:
    
    >mvn exec:java



<br></br>

# Playing the game
The game runs on the command line and accepts only textual input.

The user will first be prompted to select a mode of play by typing in the number of the mode of their choosing. The available modes are:

- **player vs player**: the players take turns entering their moves, as prompted. After a move is made, the input swaps to the other player.

- **player vs computer**:  the player enters their moves, as prompted. The computer player responds to each of the player's moves.

Once the game mode is selected, the user is presented by an ASCII representation of the game board, a seven-column six-row grid in which each cell contains one of the following symbols:

- **.**  -  the cell is empty

- **X**  -  the cell is occupied by a chip owned by Player 1

- **O**  -  the cell is occupied by a chip owned by Player 2

The goal of the game is for a player to be the first to get 4-in-a-row, either horizontally, vertically, or diagonally, by dropping one of their chips in a non-full column each turn.

In order to make a move, the user must enter the number of the column in which they want to drop their chip. If this produces 4-in-a-row for the current player, the game ends in their victory. Otherwise, it is the other player's turn.

The game proceeds until one of the players wins, or all the cells in the board are filled, resulting in a draw.

If a player enters incorrect input, they will be prompted to try again.