package model;

import java.lang.Math;
/**
 * Represents a board state of the Lasker Morris Game.
 * Contains most of the functionality of the game, including
 * adjacency relations between positions, mills combinations, etc.
 * 
 * @author aguirre
 *
 */
public class LaskerMorrisGameState {

    /**
     * Constant to represent empty position in board.
     */
    public static final int EMPTY = 0;

    /**
     * Constant to represent white stone.
     */
    public static final int WHITE = 1;

    /**
     * Constant to represent black stone.
     */
    public static final int BLACK = 2;

    /**
     * Indicates whether it's white's turn or not.
     * If false, it's black's turn.
     */
    private boolean whitePlays = true;

    /**
     * Indicates how many white stones still
     * have to be put in the board
     */
    private int remainingWhiteStones = LaskerMorrisBoard.MAXSTONES;

    /**
     * Indicates how many black stones still
     * have to be put in the board
     */
    private int remainingBlackStones = LaskerMorrisBoard.MAXSTONES;

    /**
     * Represents the state of the board. Each position of the array 
     * is a "corner" in the board. Board coordinates are borrowed from
     * https://stackoverflow.com/questions/15405419/bitboard-representation-for-nine-men-morris-game
     * so they are as follows:
     * 
     * 0        1        2
     *    3     4     5
     *       6  7  8
     * 9  10 11    12 13 14
     *       15 16 17
     *    18    19    20
     * 21       22       23
     * 
     * A zero in position i means the space is free.
     * A one in position i means a white stone in that position.
     * A two in position i means a black stone in that position.
     * See constants in this class.
     */
    private int[] board = new int[LaskerMorrisBoard.POSITIONS];

    /**
     * Default constructor. Board empty, white plays.
     */
    public LaskerMorrisGameState() { }

    /**
     * Constructor that receives turn and board contents.
     * @param whitePlays indicates whether white plays.
     * @param board is the contents to set in the board.
     * @param whiteStonesToPlay is the number of white stones still remaining to be put.
     * @param blackStonesToPlay is the number of black stones still remaining to be put.
     */
    public LaskerMorrisGameState(boolean whitePlays, int[] board, int whiteStonesToPlay, int blackStonesToPlay) {
        if (board == null || board.length != LaskerMorrisBoard.POSITIONS) throw new IllegalArgumentException("invalid contents for board");
        if (whiteStonesToPlay < 0 || blackStonesToPlay < 0) throw new IllegalArgumentException("invalid number of stones");

        this.whitePlays = whitePlays;
        this.remainingWhiteStones = whiteStonesToPlay;
        this.remainingBlackStones = blackStonesToPlay;
        for (int i = 0; i < this.board.length; i++) {
            this.board[i] = board[i];
        }

        if (!isValid()) {
            throw new IllegalArgumentException("invalid contents for board:" + toString());
        }
    }

    /**
     * Creates a clone of a game state.
     * @return a clone object of the game state.
     */
    public LaskerMorrisGameState clone() {
        LaskerMorrisGameState newState = new LaskerMorrisGameState(this.whitePlays, this.board, this.remainingWhiteStones, this.remainingBlackStones);

        return newState;
    }


    /**
     * Sets a stone in the specified position, according to the player's turn.
     * @param position is the position where to set the stone.
     */
    public void putStone(int position) {
        if (!isValidPosition(position)) throw new IllegalArgumentException("invalid position");
        if (this.board[position] != EMPTY) throw new IllegalArgumentException("occupied position");
        if (this.whitePlays) {
            if (this.remainingWhiteStones > 0) this.remainingWhiteStones--;
            else throw new IllegalStateException("no stones left");
        }
        else {
            if (this.remainingBlackStones > 0) this.remainingBlackStones--;
            else throw new IllegalStateException("no stones left");
        }
        this.board[position] = (this.whitePlays?1:2);
    }

    /**
     * Indicates whether a specified position is occupied or not.
     * @param position is the position to query.
     * @return true iff the position is occupied.
     */
    public boolean isOccupied(int position) {
        if (!isValidPosition(position)) throw new IllegalArgumentException("invalid position");
        return this.board[position] != EMPTY;
    }

    /**
     * Indicates whether white's play.
     * @return true iff it's white's turn.
     */
    public boolean isWhitesTurn() {
        return this.whitePlays;
    }

    /**
     * Counts number of white stones on the board
     * @return number of white stones on the board.
     */
    public int numberOfWhiteStonesOnBoard() {
        int result = 0;
        for (int i = 0; i < this.board.length; i++) {
            if (this.board[i] == WHITE) result++;
        }
        return result;
    }

    /**
     * Counts number of black stones on the board
     * @return number of black stones on the board.
     */
    public int numberOfBlackStonesOnBoard() {
        int result = 0;
        for (int i = 0; i < this.board.length; i++) {
            if (this.board[i] == BLACK) result++;
        }
        return result;
    }

    /**
     * Produces string representation of the state. Prints board, turn, 
     * remaining stones per player.
     * @return a string representing the state of the game.
     */
    public String toString() {
        String result = "";
        result += "Turn: " + (isWhitesTurn() ? "Whites play" : "Blacks play") + "\n";
        result += "White stones remaining: " + this.remainingWhiteStones + "\n";
        result += "Black stones remaining: " + this.remainingBlackStones + "\n"; 
        result += boardToString();
        return result;
    }

    /**
     * Produces string representation of the board state. It ignores the turn.
     * @return a string representing the state of the board.
     */
    public String boardToString() {
        String result = "";

        String firstLine = positionAsString(0) + "--------" + positionAsString(1) + "--------" + positionAsString(2) + "\n";
        result += firstLine;

        String secondLine = " +--" + positionAsString(3) + "----" + positionAsString(4) + "----" + positionAsString(5) + "--+" + "\n";
        result += secondLine;

        String thirdLine = " +---+--" + positionAsString(6) + positionAsString(7) + positionAsString(8) + "--+---+" + "\n";
        result += thirdLine;

        String fourthLine = positionAsString(9) + "-" + positionAsString(10) + "-" + positionAsString(11);
        fourthLine += "---";
        fourthLine += positionAsString(12) + "-" + positionAsString(13) + "-" + positionAsString(14) + "\n";
        result += fourthLine;

        String fifthLine = " +---+--" + positionAsString(15) + positionAsString(16) + positionAsString(17) + "--+---+" + "\n";
        result += fifthLine;

        String sixthLine = " +--" + positionAsString(18) + "----" + positionAsString(19) + "----" + positionAsString(20) + "--+" + "\n";
        result += sixthLine;

        String seventhLine = positionAsString(21) + "--------" + positionAsString(22) + "--------" + positionAsString(23) + "\n";
        result += seventhLine;

        return result;
    }

    /**
     * Produces a string representation of a position of the board.
     * @param i is the position for which to generate a string representation.
     * @return a string representation of position i in the board.
     */
    private String positionAsString(int i) {
        return "[" + (board[i] == EMPTY ? " ": (board[i] == WHITE ? "W" : "B")) + "]";
    }

    /**
     * Returns value in specified position.
     * @param position is the position to query about.
     * @return zero if position free, 1 if occupied by white stone, 2 if occupied by black stone.
     */
    public int getValue(int position) {
        if (!isValidPosition(position)) throw new IllegalArgumentException("invalid position");
        return this.board[position];
    }

    /**
     * Sets the turn of the game. 
     * @param indicates whether is white's turn or not.
     */
    public void setWhitesTurn(boolean whitesTurn) {
        this.whitePlays = whitesTurn;		
    }

    /**
     * Checks whether given position belongs to a mill.
     * @param position is the position to query.
     * @return true iff position is in a mill.
     */
    public boolean isInMill(int position) {
        if (!isValidPosition(position)) throw new IllegalArgumentException("invalid position");
        if (this.board[position] == EMPTY) return false;
        for (Integer[] m: LaskerMorrisBoard.getInstance().getMills(position)) {
            if (this.board[m[0]] == this.board[m[1]] && this.board[m[0]] == this.board[m[2]]) return true;
        }
        return false;
    }


    /**
     * Checks whether the current state is valid
     * @return true iff the current state is valid.
     */
    public boolean isValid() {
        if (board == null) return false;
        if (board.length != LaskerMorrisBoard.POSITIONS) return false;
        int countWhites = 0;
        int countBlacks = 0;
        for (int i = 0; i < board.length; i++) {
            if (board[i] < 0 || board[i] > 2) return false;
            if (board[i] == WHITE) countWhites++;
            if (board[i] == BLACK) countBlacks++;
        }
        return (((remainingWhiteStones + countWhites) <= LaskerMorrisBoard.MAXSTONES) && ((remainingBlackStones + countBlacks) <= LaskerMorrisBoard.MAXSTONES));
    }

    /**
     * Removes stone from given position
     * @param pos is the position from which to remove a stone
     */
    public void removeStone(int pos) {
        if (!isValidPosition(pos)) throw new IllegalArgumentException("invalid position");
        if (this.board[pos] == EMPTY) throw new IllegalStateException("position already free");
        this.board[pos] = EMPTY;
    }

    /**
     * Checks whether the current state is final, i.e., cannot
     * continue playing
     * @return true iff the state is final, arrived to a winner.
     */
    public boolean isFinal() {
        if (whitePlays && remainingWhiteStones > 0) return false;
        if (!whitePlays && remainingBlackStones > 0) return false;
        if (whitePlays && this.numberOfWhiteStonesOnBoard() <= 2) return true;
        if (!whitePlays && this.numberOfBlackStonesOnBoard() <= 2) return true;
        if (whitePlays && this.numberOfWhiteStonesOnBoard() == 3) return false;
        if (!whitePlays && this.numberOfBlackStonesOnBoard() == 3) return false;
        if (whitePlays) return this.getNumberOfFreeWhiteAdjacent() == 0;
        else return this.getNumberOfFreeBlackAdjacent() == 0;
    }


    /**
     * Indicates whether white won in current state. 
     * @return true iff state is final and white wins
     */
    public boolean whiteWins() {
        return (isFinal() && (numberOfBlackStonesOnBoard() <= 2 || getNumberOfFreeBlackAdjacent() == 0));
    }

    
    /**
     * Indicates whether black won in current state.
     * @return true iff state is final and black wins.
     */
    public boolean blackWins() {
        return (isFinal() && (numberOfWhiteStonesOnBoard() <= 2 || getNumberOfFreeWhiteAdjacent() == 0));
    }

    /**
     * Computes estimated value for current state.
     * @return an estimated value of the current state.
     */
    public int estimatedValue() {
        if (this.whiteWins()) return 1000;
        if (this.blackWins()) return -1000;
        return 0;
    }

    /**
     * Computes number of free positions adjacent to white positions.
     * @return number of free positions adjacent to white positions.
     */
    private int getNumberOfFreeWhiteAdjacent() {
        int moves = 0;
        for (int i = 0; i < this.board.length; i++) {
            if (this.board[i] == WHITE) {
                for (Integer adj: LaskerMorrisBoard.getInstance().getAdjacencies(i)) {
                    if (this.board[adj] == EMPTY) moves++;
                }
            }
        }
        return moves;
    }

    /**
     * Computes number of free positions adjacent to black positions.
     * @return number of free positions adjacent to black positions.
     */
    private int getNumberOfFreeBlackAdjacent() {
        int moves = 0;
        for (int i = 0; i < this.board.length; i++) {
            if (this.board[i] == BLACK) {
                for (Integer adj: LaskerMorrisBoard.getInstance().getAdjacencies(i)) {
                    if (this.board[adj] == EMPTY) moves++;
                }
            }
        }
        return moves;
    }

    /**
     * Checks whether current state is max (max is if white plays).
     * Useful for minimax function.
     * @return true iff white plays.
     */
    public boolean isMax() {
        return this.whitePlays;
    }

    /**
     * Returns max possible value for estimations. Value returned by
     * estimatedValue() must always be smaller than this value.
     * Useful for minimax and minimax alpha beta.
     * @return max possible value for estimations
     */
    public static int maxValue() {
        return 100000;
    }

    /**
     * Returns min possible value for estimations. Value returned by
     * estimatedValue() must always be greater than this value.
     * Useful for minimax and minimax alpha beta.
     * @return min possible value for estimations
     */
    public static int minValue() {
        return -100000;
    }


    /**
     * Moves a stone in the board.
     * @param position is the source position of the stone.
     * @param adjacent is the target position of the stone.
     */
    public void moveStone(int position, Integer adjacent) {
        if (!isValidPosition(position)) throw new IllegalArgumentException("invalid position");
        if (!isValidPosition(adjacent)) throw new IllegalArgumentException("invalid adjacent position");
        if (this.board[adjacent] != EMPTY) throw new IllegalArgumentException("occupied adjacent position");
        this.board[adjacent] = this.board[position];
        this.board[position] = EMPTY;
    }

    /**
     * Checks whether a given position is a valid position
     * @param position is the position to query about
     * @return true iff the position is a valid position
     */
    public static boolean isValidPosition(int position) {
        return (position >= 0 && position < LaskerMorrisBoard.POSITIONS);
    }

    /**
     * Remaining white stones to put in the board.
     * @return the number of white stones that still can be put in the board.
     */
    public int remainingWhiteStones() {
        return this.remainingWhiteStones;
    }

    /**
     * Remaining black stones to put on the board.
     * @return the number of black stones that still can be put on the board.
     */
    public int remainingBlackStones() {
        return this.remainingBlackStones;
    }

    /**
     * Checks whether a stone in the board can be moved.
     * @return true iff current player can move a stone in the board.
     */
    public boolean canMoveStone() {
        if (whitePlays) {
            return (getNumberOfFreeWhiteAdjacent() > 0 || (numberOfWhiteStonesOnBoard() == 3 && remainingWhiteStones == 0));
        }
        else {
            return (getNumberOfFreeBlackAdjacent() > 0 || (numberOfBlackStonesOnBoard() == 3 && remainingBlackStones == 0));
        }
    }
    
    /**
     * Returns the number of free adjacent positions to a given position in the board.
     * @param position is the position to query about.
     * @return the number of adjacent positions to position, which are free.
     */
    public int getNumberOfFreeAdjacent(int position) {
        if (!isValidPosition(position)) throw new IllegalArgumentException("invalid position");
        int numAdjacencies = 0;
        for (Integer adjs: LaskerMorrisBoard.getInstance().getAdjacencies(position)) {
            if (board[adjs] == EMPTY) numAdjacencies++;
        }
        return numAdjacencies;
    }
}