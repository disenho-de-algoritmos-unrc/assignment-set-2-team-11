package engines;
import java.util.List;
import java.util.Random;

import model.LaskerMorrisBoard;
import model.LaskerMorrisGameState;

/**
 * Engine implementing random moves.
 * 
 * @author aguirre
 *
 */
public class RandomEngine implements GameEngine {

    /**
     * Generator for random decisions.
     */
    private Random randomGenerator = null;


    /**
     * Default constructor
     */
    public RandomEngine() {
        randomGenerator = new Random();
    }


    /**
     * Computes move to take, according to random decision value.
     * @param state is the state from which to move.
     * @return the resulting state to move to, according to a random decision.
     */
    public LaskerMorrisGameState computeMove(LaskerMorrisGameState state) {
        if (state == null) throw new IllegalArgumentException("null state");
        if (state.isFinal()) throw new IllegalArgumentException("can't move from a final state");


        if (state.isWhitesTurn()) {
            if (state.remainingWhiteStones() > 0 && state.canMoveStone()) {
                // Decide between "putting" or "moving"
                if (randomGenerator.nextBoolean()) {
                    // Putting a stone.
                    return putRandomStone(state);                   
                }
                else {
                    // moving a stone.
                    return moveRandomStone(state);
                }
            }
            else {
                if (state.remainingWhiteStones() > 0) {
                    // Putting a stone is the only option
                    return putRandomStone(state);
                }
                else {
                    // Moving a stone is the only option
                    return moveRandomStone(state);
                }
            }
        }
        else {
            if (state.remainingBlackStones() > 0 && state.canMoveStone()) {
                // Decide between "putting" or "moving"
                if (randomGenerator.nextBoolean()) {
                    // Putting a stone.
                    return putRandomStone(state);
                }
                else {
                    // moving a stone.
                    return moveRandomStone(state);                    
                }
            }
            else {
                if (state.remainingBlackStones() > 0) {
                    // Putting a stone is the only option
                    return putRandomStone(state);
                }
                else {
                    // Moving a stone is the only option
                    return moveRandomStone(state);
                }
            }
        }
    }

    /**
     * Moves a random stone in the board
     * @param state is a state with at least one stone of the player, with a free adjacent cell
     * @return a state consisting of performing a random move by the corresponding player
     */
    private LaskerMorrisGameState moveRandomStone(LaskerMorrisGameState state) {
        if (state == null) throw new IllegalArgumentException("null state");
        if (!state.canMoveStone()) throw new IllegalArgumentException("no stones to move");

        boolean found = false;
        int position = 0;
        while (!found && !Thread.currentThread().isInterrupted()) {
            position = Math.abs(randomGenerator.nextInt()) % LaskerMorrisBoard.POSITIONS;
            if (state.getValue(position) == (state.isWhitesTurn() ? LaskerMorrisGameState.WHITE : LaskerMorrisGameState.BLACK) && state.getNumberOfFreeAdjacent(position) > 0) found = true;
        }
        LaskerMorrisGameState newState = state.clone();
        List<Integer> adjs = LaskerMorrisBoard.getInstance().getAdjacencies(position);
        boolean done = false;
        while (!done && !Thread.currentThread().isInterrupted()) {
            int target = Math.abs(randomGenerator.nextInt()) % adjs.size();
            if (!newState.isOccupied(adjs.get(target))) {
                newState.moveStone(position, adjs.get(target));
                if (newState.isInMill(adjs.get(target))) {
                    newState = removeRandomStone(newState);                    
                }
                newState.setWhitesTurn(!newState.isWhitesTurn()); 
                done = true;
            }
        }
        if (Thread.currentThread().isInterrupted()) {
            return failSafeResult(state);
        }
        else {
            return newState;
        }
    }

    /**
     * Quickly computes a move when issued an interrupt or timeout.
     * @param state is the state from which to move
     * @return the first available move, prioritizing putting a stone.
     */
    private LaskerMorrisGameState failSafeResult(LaskerMorrisGameState state) {
        if (state == null) throw new IllegalArgumentException("null state");
        if (state.isFinal()) throw new IllegalArgumentException("final state");
        LaskerMorrisGameState output = state.clone();
        if (state.isWhitesTurn()) {
            if (state.remainingWhiteStones() > 0) {
                // Put stone in first available place
                for (int position = 0; position < LaskerMorrisBoard.POSITIONS; position++) {
                    if (output.getValue(position) == LaskerMorrisGameState.EMPTY) {
                        output.putStone(position);
                        if (output.isInMill(position)) {
                            output = removeFirstStone(output);                    
                        }
                        output.setWhitesTurn(!output.isWhitesTurn());
                        return output;
                    }
                }
            }
            else {
                // Move first possible stone
                for (int position = 0; position < LaskerMorrisBoard.POSITIONS; position++) {
                    if (output.getValue(position) == LaskerMorrisGameState.WHITE && output.getNumberOfFreeAdjacent(position) > 0) {
                        for (int target: LaskerMorrisBoard.getInstance().getAdjacencies(position)) {
                            if (!output.isOccupied(target)) {
                                output.moveStone(position, target);
                                if (output.isInMill(target)) {
                                    output = removeFirstStone(output);                    
                                }
                                output.setWhitesTurn(!output.isWhitesTurn());
                                return output;
                            }
                        }

                    }
                }
            }
        }
        else {
            if (state.remainingBlackStones() > 0) {
                // Put stone in first available place
                for (int position = 0; position < LaskerMorrisBoard.POSITIONS; position++) {
                    if (output.getValue(position) == LaskerMorrisGameState.EMPTY) {
                        output.putStone(position);
                        if (output.isInMill(position)) {
                            output = removeFirstStone(output);                    
                        }
                        output.setWhitesTurn(!output.isWhitesTurn());
                        return output;
                    }
                }
            }
            else {
                // Move first possible stone
                for (int position = 0; position < LaskerMorrisBoard.POSITIONS; position++) {
                    if (output.getValue(position) == LaskerMorrisGameState.BLACK && output.getNumberOfFreeAdjacent(position) > 0) {
                        for (int target: LaskerMorrisBoard.getInstance().getAdjacencies(position)) {
                            if (!output.isOccupied(target)) {
                                output.moveStone(position, target);
                                if (output.isInMill(target)) {
                                    output = removeFirstStone(output);                    
                                }
                                output.setWhitesTurn(!output.isWhitesTurn());
                                return output;
                            }
                        }

                    }
                }
            }
        }
        // should never reach this point.
        return null;
    }

    /**
     * Removes the first stone found of the adversary
     * @param state is the state from which to remove an adversary's stone
     * @return a clone of state, where the first available stone of the adversary was removed.
     */
    private LaskerMorrisGameState removeFirstStone(LaskerMorrisGameState state) {
        if (state == null) throw new IllegalArgumentException("null state");

        System.out.println("Removing!");

        LaskerMorrisGameState output = state.clone();

        if (output.isWhitesTurn()) {
            // remove black stone
            if (output.numberOfBlackStonesOnBoard() == 0) throw new IllegalArgumentException("no stone to remove");
            for (int position = 0; position < LaskerMorrisBoard.POSITIONS; position++) {
                if (output.getValue(position) == LaskerMorrisGameState.BLACK) {
                    output.removeStone(position);
                    return output;
                }
            }
            return output;
        }
        else {
            // remove white stone
            if (output.numberOfBlackStonesOnBoard() == 0) throw new IllegalArgumentException("no stone to remove");
            for (int position = 0; position < LaskerMorrisBoard.POSITIONS; position++) {
                if (output.getValue(position) == LaskerMorrisGameState.WHITE) {
                    output.removeStone(position);
                    return output;
                }
            }
            return output;
        }
    }


    /**
     * Moves by putting a random stone in the board
     * @param state is the state from which to move.
     * @return a state resulting from putting a random stone on the parameter.
     * If a mill results from this move, an adversary's state is removed.
     * The "turn" of the state is also updated.
     */
    private LaskerMorrisGameState putRandomStone(LaskerMorrisGameState state) {
        if (state == null) throw new IllegalArgumentException("null state");
        if (state.isWhitesTurn() && state.remainingWhiteStones() == 0) throw new IllegalArgumentException("no stones left to put");
        if (!state.isWhitesTurn() && state.remainingBlackStones() == 0) throw new IllegalArgumentException("no stones left to put");

        boolean found = false;
        int position = 0;
        while (!found) {
            position = Math.abs(randomGenerator.nextInt()) % LaskerMorrisBoard.POSITIONS;
            if (!state.isOccupied(position)) found = true;
        }
        LaskerMorrisGameState output = state.clone();
        output.putStone(position);


        if (output.isInMill(position)) {
            output = removeRandomStone(output);
        }

        output.setWhitesTurn(!output.isWhitesTurn());

        return output;
    }


    /**
     * Removes a random stone of the adversary.
     * @param state is the state from which to remove an adversary's stone
     * @return a clone of state, where a stone of the adversary has been removed.
     */
    private LaskerMorrisGameState removeRandomStone(LaskerMorrisGameState state) {
        if (state == null) throw new IllegalArgumentException("null state");

        System.out.println("Removing!");

        LaskerMorrisGameState output = state.clone();

        if (output.isWhitesTurn()) {
            // remove black stone
            if (output.numberOfBlackStonesOnBoard() == 0) throw new IllegalArgumentException("no stone to remove");
            boolean found = false;
            int position = 0;
            while (!found) {
                position = Math.abs(randomGenerator.nextInt()) % LaskerMorrisBoard.POSITIONS;
                if (output.getValue(position) == LaskerMorrisGameState.BLACK) found = true;
            }
            output.removeStone(position);
            return output;
        }
        else {
            // remove white stone
            if (output.numberOfWhiteStonesOnBoard() == 0) throw new IllegalArgumentException("no stone to remove");
            boolean found = false;
            int position = 0;
            while (!found) {
                position = Math.abs(randomGenerator.nextInt()) % LaskerMorrisBoard.POSITIONS;
                if (output.getValue(position) == LaskerMorrisGameState.WHITE) found = true;
            }
            output.removeStone(position);
            return output;
        }
    }

}


