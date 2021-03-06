package edu.jsu.mcis.cs408.project1;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

public class TicTacToeModel {

    public static final int DEFAULT_SIZE = 3;

    private Mark[][] grid;      /* the game grid */
    private boolean xTurn;      /* is TRUE if X is the current player */
    private int size;           /* the size (width and height) of the game grid */

    private TicTacToeController controller;

    protected PropertyChangeSupport propertyChangeSupport;

    public TicTacToeModel(TicTacToeController controller, int size) {

        this.size = size;
        this.controller = controller;
        propertyChangeSupport = new PropertyChangeSupport(this);

        resetModel(size);

    }

    public void resetModel(int size) {


        /* This method resets the Model to its default state.*/

        this.size = size;
        this.xTurn = true;

        grid = new Mark[size][size];

        for (int row = 0; row < size; ++row) {

            for (int col = 0; col < size; ++col) {

                grid[row][col] = Mark.EMPTY;
            }
        }

    }

    public boolean setMark(TicTacToeSquare square) {

        /* This method accepts the target square as a TicTacToeSquare argument, and adds the
           current player's mark to this square.  First, it uses "isValidSquare()" to check if
           the specified square is within range, and then it uses "isSquareMarked()" to see if
           this square is already occupied!  If the specified location is valid, makes a mark for the
           current player, then uses "firePropertyChange()" to fire the corresponding property change
           event, which will inform the Controller that a change of state has taken place which
           requires a change to the View.  Finally, toggles "xTurn" (from TRUE to FALSE, or vice-
           versa) to switch to the other player.  Retursn TRUE if the mark was successfully added to
           the grid; otherwise, return FALSE. */

        int row = square.getRow();
        int col = square.getCol();
        boolean markMade = false;

        if ( (isValidSquare(row, col)) && (!isSquareMarked(row, col)) ) {

            if (xTurn) {
                grid[row][col] = Mark.X;
                firePropertyChange(TicTacToeController.SET_SQUARE_X, this, square);
                xTurn = false;
            }
            else {
                grid[row][col] = Mark.O;
                firePropertyChange(TicTacToeController.SET_SQUARE_O, this, square);
                xTurn = true;
            }
            markMade = true;

        }

        return markMade;

    }

    private boolean isValidSquare(int row, int col) {

        /* Returns TRUE if the specified location is within the bounds of the board */

        boolean isValidSquare = false;
        if( (row >= 0 && row < size) && (col >= 0 && col < size) ) {
            isValidSquare = true;
        }
        return isValidSquare;

    }

    private boolean isSquareMarked(int row, int col) {

        /* Returns TRUE if the square at specified location is marked */

        boolean isSquareMarked = false;
        if( (getMark(row, col) == Mark.X) || (getMark(row, col) == Mark.O) ) {
            isSquareMarked = true;
        }
        return isSquareMarked;

    }

    public Mark getMark(int row, int col) {

        return grid[row][col];

    }

    public Result getResult() {

         /* Calls "isMarkWin()" to see if X or O is the winner, if the game is a
            TIE, or if the game is not over.  Returns the corresponding Result
            value */

        if (isMarkWin(Mark.X)) {
            return Result.X;
        }
        else if (isMarkWin(Mark.O)) {
            return Result.O;
        }
        else if (isTie()) {
            return Result.TIE;
        }
        else {
            return Result.NONE;
        }

    }

    private boolean isMarkWin(Mark mark) {

        /* Checks the squares of the board to see if the specified mark is the winner */

        boolean isMarkWin = false;
        String userMarks = "";
        int count = size - 1;
        String mark1 = "", mark2 = "", mark3 = "",mark4 = "";

        for (int j = 0; j < size; ++j) {
            userMarks += mark.toString();
        }

        for (int row = 0; row < size; ++row) {

            for (int col = 0; col < size; ++col) {
                mark1 += (grid[row][col]).toString();
                mark2 += (grid[col][row]).toString();
            }

            mark3 += (grid[row][row]).toString();
            mark4 += (grid[row][count]).toString();
            count--;

            if ( ( userMarks.equals(mark1) ) || ( userMarks.equals(mark2) ) ||
                 ( userMarks.equals(mark3) ) || ( userMarks.equals(mark4) ) ) {

                isMarkWin = true;
                break;
            }

            else {
                isMarkWin = false;
            }

            mark1 = "";
            mark2 = "";

        }

        return isMarkWin;

    }

    private boolean isTie() {

        /* Checks the squares of the board to see if the game is a tie */

        boolean isTie = false;
        int count = 0;

        for (int row = 0; row < size; ++row) {
            for (int col = 0; col < size; ++col) {

                if (isSquareMarked(row, col)) {
                    count++;
                }

            }
        }

        if ( (isMarkWin(Mark.X) == false) && (isMarkWin(Mark.O) == false) && (count == size * size) ) {
            isTie = true;
        }

        return isTie;

    }

    public boolean isXTurn() {

        return xTurn;

    }

    public int getSize() {

        return size;

    }

    // Property Change Methods (adds/removes a PropertyChangeListener, or fires a property change)

    public void addPropertyChangeListener(PropertyChangeListener listener) {
        propertyChangeSupport.addPropertyChangeListener(listener);
    }

    public void removePropertyChangeListener(PropertyChangeListener listener) {
        propertyChangeSupport.removePropertyChangeListener(listener);
    }

    protected void firePropertyChange(String propertyName, Object oldValue, Object newValue) {
        propertyChangeSupport.firePropertyChange(propertyName, oldValue, newValue);
    }

    /* ENUM TYPE DEFINITIONS */

    // Mark (represents X, O, or an empty square)

    public enum Mark {

        X("X"),
        O("O"),
        EMPTY("-");

        private String message;

        private Mark(String msg) {
            message = msg;
        }

        @Override
        public String toString() {
            return message;
        }

    };

    // Result (represents the game state: X wins, O wins, a TIE, or NONE if the game is not over)

    public enum Result {

        X("X"),
        O("O"),
        TIE("TIE"),
        NONE("NONE");

        private String message;

        private Result(String msg) {
            message = msg;
        }

        @Override
        public String toString() {
            return message;
        }

    };

}