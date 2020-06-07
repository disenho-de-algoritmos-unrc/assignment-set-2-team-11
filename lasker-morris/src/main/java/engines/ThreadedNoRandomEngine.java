package engines;

import model.LaskerMorrisGameState;

/**
 * Class that runs a thread with a random engine.
 * 
 */
public class ThreadedNoRandomEngine implements Runnable {

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
    public ThreadedNoRandomEngine(LaskerMorrisGameState state) {
        this.state = state;
    }

    /**
     * Runs a stoppable random engine.
     */
    public void run() {
        NoRandomEngine engine = new NoRandomEngine();
        ThreadedNoRandomEngine.result = engine.computeMove(state);
    }

}