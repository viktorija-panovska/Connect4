package Connect4;

/**
 * The <code>View</code> class handles the display of the menu and the board on the screen.
 */
public class View {

    /* Possible symbols for each cell in the board
     *      - '.' -> empty space
     *      - 'X' -> space occupied by a token belonging to Player 1
     *      - 'O' -> space occupied by a token belonging to Player 2
     */
    static final char[] symbols = { '.', 'X', '0' };


    /**
     * Prints the selection of modes of play
     */
    public static void showModeMenu() {
        System.out.println("Choose mode:");
        System.out.println("    1 - player vs player");
        System.out.println("    2 - player vs computer");
    }


    /**
     * Prints the current state of the board
     * @param board reference to the Model
     */
    public static void showBoard(Model board) {
        // top border
        StringBuilder output = new StringBuilder();
        output.append("-".repeat(Math.max(0, (board.getWidth() * 4 + 1))));
        output.append('\n');

        // column numbers
        output.append("| ");
        for (int i = 1; i <= board.getWidth(); i++) {
            output.append(i).append(" | ");
        }
        output.append('\n');

        // board top border
        output.append("-".repeat(Math.max(0, (board.getWidth() * 4 + 1))));
        output.append('\n');

        // board
        for (int i = 0; i < board.getHeight(); i++) {
            output.append("| ");
            for (int j = 0; j < board.getWidth(); j++) {
                output.append(symbols[board.get(i, j)]).append(" | ");
            }
            output.append('\n');
        }

        // bottom boarder
        output.append("-".repeat(Math.max(0, (board.getWidth() * 4 + 1))));

        System.out.println(output);
    }
}
