package engines;

import model.LaskerMorrisGameState;

/**
 * Interface to register requirements for game engines.
 * 
 * @author aguirre
 *
 */
public interface GameEngine {
   
    /**
     * Computes next state from a given non-final game state.
     * Method computeMove should be "stoppable", i.e., when issued a TimeOutException
     * or ExecutionException, the routine must return immediately.
     * See random engine implementation for an example of how to deal with this issue.
     * @param state is the game state to move from
     * @return the computed next state.
     */
    public LaskerMorrisGameState computeMove(LaskerMorrisGameState state);
}
