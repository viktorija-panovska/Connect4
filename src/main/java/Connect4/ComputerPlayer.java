package Connect4;


/**
 * The <code>ComputerPlayer</code> class represents the AI opponent.
 */
public class ComputerPlayer {
    private Model board;

    private int bestMove;


    /**
     * Public constructor.
     * @param board reference to the Model
     */
    public ComputerPlayer(Model board) {
        this.board = board;
        this.bestMove = 3;
    }


    /**
     * Counts the number of free spaces, spaces occupied by player 1 and spaces occupied by player 2 in a 4-cell
     * window starting from the cell (<code>row</code>, <code>column</code>) and going in the direction <code>d</code>
     * @param row index of the starting row
     * @param column index of the starting column
     * @param d direction in which to iterate
     * @return an integer array of length 3 where the element at index 0 represents the number of free spaces, the
     * element at index 1 represents the number of spaces occupied by player 1 and the element at index 2 represents
     * the number of spaces occupied by player 2, in the given window
     */
    private int[] count(int row, int column, Direction d) {
        int[] counts = new int[3];

        for (int i = 0; i < 4; i++) {
            counts[board.get(row, column)]++;
            row += d.vertical();
            column += d.horizontal();
        }

        return counts;
    }


    /**
     * Assigns a score depending on the number of free spaces, spaces occupied by player 1 and spaces occupied by player
     * 2 in a 4-cell window starting from cell (<code>row</code>, <code>column</code>) and going in the direction
     * <code>d</code>
     * @param row index of the starting row
     * @param column index of the starting column
     * @param d direction in which to iterate
     * @return the score of the given window
     */
    private int evaluate(int row, int column, Direction d) {
        int[] counts = count(row, column, d);

        if (counts[board.getPlayer()] == 4)
            return 100;
        else if (counts[board.getPlayer()] == 3 && counts[0] == 1)
            return 16;
        else if (counts[board.getPlayer()] == 2 && counts[0] == 2)
            return 8;

        if (counts[board.getOtherPlayer()] == 4)
            return -100;
        else if (counts[board.getOtherPlayer()] == 3 && counts[0] == 1)
            return -32;
        else if (counts[board.getOtherPlayer()] == 2 && counts[0] == 2)
            return -8;

        return 0;
    }


    /**
     * Calculates the total score of the current state of the board by summing the scores of all 4-cell windows,
     * starting from all cells in the board and going in all possible directions
     * @return the total score of the current board
     */
    private int getScore() {
        int score = 0;

        // Row score
        for (int row = 0; row < board.getHeight(); row++)
            for (int column = 0; column < board.getWidth() - 3; column++)
                score += evaluate(row, column, board.directions[0]);

        // Column score
        for (int row = 0; row < board.getHeight() - 3; row++)
            for (int column = 0; column < board.getWidth(); column++)
                score += evaluate(row, column, board.directions[1]);

        // Diagonal score
        for (int row = 0; row < board.getHeight() - 3; row++)
            for (int column = 0; column < board.getWidth() - 3; column++)
                score += evaluate(row, column, board.directions[2]);

        for (int row = board.getHeight() - 1; row >= 3; row--)
            for (int column = 0; column < board.getWidth() - 3; column++)
                score += evaluate(row, column, board.directions[3]);

        return score;
    }

    /**
     * Implements the minimax algorithm with fixed depth and alpha-beta pruning, getting the heuristic evaluation
     * from the method <code>getScore</code>.
     * @param depth how many more recursive steps we can perform before calculating the heuristic
     * @param alpha the best score on the current search path that the maximizing player can definitely achieve
     * @param beta the best score on the current search path that the minimizing player can definitely achieve
     * @return best score currently discovered
     */
    private int minimax(int depth, double alpha, double beta) {
        if (board.getWinner() == 2)
            return 1000;

        if (board.getWinner() == 1)
            return -1000;

        if (board.getFreeSpaces() == 0)
            return 0;

        if (depth == 0)
            return getScore();


        int bestValue = board.getPlayer() == 2 ? Integer.MIN_VALUE : Integer.MAX_VALUE;

        for (int col = 0; col < board.getWidth(); col++) {
            if (board.move(col)) {
                int value = minimax(depth - 1, alpha, beta);
                board.unmove(col);

                if ((board.getPlayer() == 2 && value > bestValue) || (board.getPlayer() == 1 && value < bestValue)) {
                    bestValue = value;
                    bestMove = col;

                    if (board.getPlayer() == 2) {
                        if (value >= beta)
                            return value;
                        alpha = Math.max(alpha, value);
                    } else {
                        if (value <= alpha)
                            return value;
                        beta = Math.min(beta, value);
                    }
                }
            }
        }
        return bestValue;
    }


    /**
     * Calls the minimax algorithm to compute the best possible move for the current board
     * @return index of the column of the best move
     */
    public int getMove() {
        minimax(1, -1000, 1000);
        return bestMove;
    }
}
