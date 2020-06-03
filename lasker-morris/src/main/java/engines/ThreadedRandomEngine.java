package engines;

import model.LaskerMorrisGameState;

/**
 * Class that runs a thread with a random engine.
 * 
 */
public class ThreadedRandomEngine implements Runnable {

    /**
     * state where the move starts from
     */
    LaskerMorrisGameState state;

    /**
     * Resulting move is left here
     */
    public static LaskerMorrisGameState result;

    /**
     * Default constructor
     * @param state is the source state to play from
     */
    public ThreadedRandomEngine(LaskerMorrisGameState state) {
        this.state = state;
    }

    /**
     * Runs a stoppable random engine.
     */
    public void run() {
        RandomEngine engine = new RandomEngine();
        ThreadedRandomEngine.result = engine.computeMove(state);
    }

}