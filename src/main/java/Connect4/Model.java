package Connect4;

import java.util.concurrent.RecursiveTask;

/**
 * The <code>Direction</code> record represents a vector on a 2D grid by its horizontal and vertical component
 */
record Direction(int horizontal, int vertical) {
}


/**
 * The <code>Model</code> class represents the game board.
 * It maintains the current state of the board and performs changes to the state such as dropping a chip or removing a
 * chip from a given column
 */
public class Model {

    // Fields

    // All the directions in which a possible winning condition can lie.
    final Direction[] directions = { new Direction(1, 0),
                                     new Direction(0, 1),
                                     new Direction(1, 1),
                                     new Direction(1, -1) };

    /* Each slot of the array can have one of 3 states:
     *      - 0 -> the slot is unoccupied
     *      - 1 -> the slot is occupied by a token from Player 1
     *      - 2 -> the slot is occupied by a token from Player 2
    */
    private final int[][] board;

    // True if Player vs Computer, false if Player vs Player
    private final boolean computerPlayer;

    // 1 if it is Player 1's turn, 2 if it is player 2's turn
    private int playerTurn;

    // The number of unoccupied spaces on the board
    private int freeSpaces;

    // 1 if Player 1 has won, 2 if Player 2 has won, 0 if there is no winner (either the game is still in progress
    // or we have a draw)
    private int winner;


    // Constructors

    /**
     * Public constructor.
     * Creates the empty 2D array with dimensions <code>width</code> x <code>height</code>, sets the mode
     * (Player vs Player or Player vs Computer) of the game and the starting player
     * @param columns number of columns in the board
     * @param rows number of rows in the board
     * @param computerPlayer true if the player has chosen to play against the computer, false if the player has chosen
     *                       to play against another player
     * @param playerTurn the player who has the first move
     */
    public Model(int columns, int rows, boolean computerPlayer, int playerTurn) {
        this.board = new int[rows][columns];
        this.freeSpaces = rows * columns;
        this.computerPlayer = computerPlayer;
        this.playerTurn = playerTurn;
    }


    // Getters

    /**
     * @param row index of a row in the board
     * @param column index of a column in the board
     * @return value at position (<code>row</code>, <code>column</code>) in the board
     */
    public int get(int row, int column) { return board[row][column]; }

    /**
     * @return number of columns in the board
     */
    public int getWidth() { return board[0].length; }

    /**
     * @return number of rows in the board
     */
    public int getHeight() { return board.length; }

    /**
     * @return true if the player has selected to play against the computer player, false otherwise
     */
    public boolean isComputerPlayer() { return computerPlayer; }

    /**
     * @return the player who has the current turn
     */
    public int getPlayer() { return playerTurn; }

    /**
     * @return the player who will have the next turn
     */
    public int getOtherPlayer() { return 3 - playerTurn; }

    /**
     * @return the number of free spaces remaining in the board
     */
    public int getFreeSpaces() { return freeSpaces; }

    /**
     * @return the winning player
     */
    public int getWinner() { return winner; }


    // Modifying the board state

    /**
     * @param winner the player that has won the game
     */
    public void setWinner(int winner) { this.winner = winner; }

    /**
     * Switches the current player to the next player
     */
    public void swapPlayer() { playerTurn = getOtherPlayer(); }


    /**
     * Sets the free cell in the selected column with the highest row index to be occupied by the current player.
     * If the selected column is already full, the returns false
     * @param column index of the column in which the chip is dropped
     * @return true if the move is valid and has been executed, false if the chosen column is full and the move fails
     */
    public boolean move(int column) {
        // if the column is full, we can't drop a new chip
        if (board[0][column] > 0)
            return false;

        // find the lowest free row
        int row = getHeight() - 1;
        while (row >= 0 && board[row][column] > 0)
            row--;

        // drop a new chip in the board
        board[row][column] = getPlayer();
        freeSpaces--;
        return true;
    }


    /**
     * Sets the occupied cell in the selected column with the lowest row index to be free. Does nothing if the column
     * is empty.
     * @param column index of the column from which we are removing the chip
     */
    public void unmove(int column) {
        // if the column is empty, do nothing
        if (board[getHeight() - 1][column] == 0)
            return;

        // find the highest occupied row
        int row = getHeight() - 1;
        while (row >= 0 && board[row][column] > 0)
            row--;

        // remove the chip from the board
        board[row + 1][column] = 0;
        freeSpaces++;
    }
}


/**
 * The <code>WinChecker</code> class extends <code>RecursiveTask</code> and implements a Divide and Conquer approach
 * to check if the win condition of the game has been reached (if the previous player has 4-in-a-row anywhere on the board)
 */
class WinChecker extends RecursiveTask<Boolean> {
    private final Model board;
    private final int startColumn;
    private final int endColumn;


    /**
     * Public constructor.
     * @param board instance of <code>Model</code>
     * @param startColumn the column index at the start of the window of the board available in this recursive call
     * @param endColumn the column index at the end of the window of the board available in this recursive call
     */
    public WinChecker(Model board, int startColumn, int endColumn) {
        this.board = board;
        this.startColumn = startColumn;
        this.endColumn = endColumn;
    }


    /**
     * Checks if the 4 cells starting from the cell (<code>row</code>, <code>column</code>) and moving in direction
     * <code>d</code> are occupied by the previous player.
     * @param row index of the starting row
     * @param column index of the starting column
     * @param d direction in which to iterate
     * @return true if the 4 cells are occupied by the last player, false otherwise
     */
    private boolean hasWon(int row, int column, Direction d) {
        for (int i = 0; i < 4; i++) {
            if (row < 0 || row >= board.getHeight() ||
                    column < 0 || column >= board.getWidth() ||
                    board.get(row, column) == 0 || board.get(row, column) == board.getPlayer())
                return false;

            row += d.vertical();
            column += d.horizontal();
        }
        return true;
    }


    /**
     * Checks if the previous player has 4-in-a-row starting from each row in the chosen column and going in every
     * possible direction.
     * @param column index of the starting column
     * @return true if the previous player has 4-in-a-row from any starting point or if we have a draw, false otherwise
     */
    public boolean isFinished(int column) {
        if (board.getFreeSpaces() == 0)
            return true;

        for (int row = 0; row < board.getHeight(); row++)
            for (Direction d : board.directions)
                if (hasWon(row, column, d)) {
                    board.setWinner(board.getOtherPlayer());
                    return true;
                }

        return false;
    }


    /**
     * Overrides the <code>compute</code> function from <code>RecursiveTask</code> to implement a Divide and Conquer
     * approach to checking if the previous player has achieved 4-in-a-row on the board
     * @return true if the previous player has 4-in-a-row, false otherwise
     */
    @Override
    protected Boolean compute() {
        if (startColumn == endColumn)
            return isFinished(startColumn);

        int middle = (startColumn + endColumn) / 2;
        WinChecker left = new WinChecker(board, startColumn, middle);
        WinChecker right = new WinChecker(board, middle + 1, endColumn);

        left.fork();
        right.fork();

        return left.join() || right.join();
    }
}