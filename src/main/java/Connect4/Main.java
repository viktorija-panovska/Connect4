package Connect4;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.concurrent.ForkJoinPool;


/**
 * The <code>Main</code> class acts as the Controller for the game. It contains the code to process the input from the
 * player(s) and delegate tasks to the Model and the View. Also contains the starting point of the program.
 */
public class Main {

    static BufferedReader reader;

    static ComputerPlayer computerPlayer;


    /**
     * Prompts the player to enter the list number of their desired mode of play. Reads user input from stdin until the
     * player enters an integer: either 1 or 2.
     * @return the list number of the chosen mode of play
     * @throws IOException if an error is encountered while reading the user input
     */
    private static int getMode() throws IOException {
        while (true) {
            try {
                System.out.print("Choose a mode: ");
                int mode = Integer.parseInt(reader.readLine());

                if (mode < 1 || mode > 2)
                    System.out.println("The mode you have selected does not exist. Try again.");
                else
                    return mode;

            } catch (NumberFormatException e) {
                System.out.println("The input you entered is not a number. Try again.");
            }
        }
    }


    /**
     * Prompts the player to enter a column number. Reads user input from stdin until the user enters an integer
     * between 1 and the width of the board.
     * @param board reference to the Model
     * @return the number of the column chosen by the player
     * @throws IOException if an I/O error occurs when reading from standard input
     */
    private static int getColumnNumber(Model board) throws IOException {
        while (true) {
            try {
                System.out.println("Player " + board.getPlayer());
                System.out.print("Enter column: ");
                int column = Integer.parseInt(reader.readLine());

                if (column < 1 || column > board.getWidth())
                    System.out.println("The column you have selected does not exist. Try again.");
                else
                    return column;

            } catch (NumberFormatException e) {
                System.out.println("The input you entered is not a number. Try again.");
            }
        }
    }


    /**
     * Prompts the player to enter the characters Y (for yes) or N (for no), indicating whether they want to continue
     * with the game or not. Reads user input from stdin until the player enters the proper character.
     * @return true if the player wants to continue the game, false otherwise
     * @throws IOException if an error is encountered while reading the user input
     */
    private static boolean getPlayAgain() throws IOException {
        while (true) {
            System.out.println("Do you want to play again? Y/N");
            String input = reader.readLine().toUpperCase();

            if (input.equals("Y"))
                return true;
            else if (input.equals("N"))
                return false;
            else
                System.out.println("Invalid response. Try again.");
        }
    }


    /**
     * Entry point of the program. Loops the game until one of the players wins or a draw is reached.
     * @param args command line arguments
     */
    public static void main(String[] args) {
        reader = new BufferedReader(new InputStreamReader(System.in));
        try {
            while (true) {
                // Choose Player vs Player or Player vs Computer
                View.showModeMenu();
                int mode = getMode();

                Model board = new Model(7, 6, mode == 2, 1);

                if (mode == 2)
                    computerPlayer = new ComputerPlayer(board);

                ForkJoinPool pool = new ForkJoinPool();

                while (true) {
                    WinChecker winChecker = new WinChecker(board, 0, board.getWidth() - 1);
                    boolean end = pool.invoke(winChecker);
                    if (end) break;

                    int columnIndex;
                    if (board.isComputerPlayer()) {
                        if (board.getPlayer() == 1) {
                            View.showBoard(board);
                            columnIndex = getColumnNumber(board) - 1;
                        } else {
                            columnIndex = computerPlayer.getMove();
                        }
                    } else {
                        View.showBoard(board);
                        columnIndex = getColumnNumber(board) - 1;
                    }

                    if (!board.move(columnIndex))
                        System.out.println("Column is full. Try again.");
                    else
                        board.swapPlayer();
                }

                // The game is over, show the result
                View.showBoard(board);
                if (board.getWinner() == 0)
                    System.out.println("It is a draw!");
                else
                    System.out.println("The winner is Player " + board.getWinner() + "!");

                boolean playAgain = getPlayAgain();
                if (!playAgain)
                    System.exit(0);
            }
        } catch (IOException ioe) {
            System.out.println("Error: encountered while reading from standard input");
        }
    }
}
