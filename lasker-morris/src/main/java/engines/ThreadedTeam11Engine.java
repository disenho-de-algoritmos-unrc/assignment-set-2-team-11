package engines;

import java.util.List;
import model.LaskerMorrisGameState;

/**
 * Class that runs a thread with a random engine.
 * 
 */
public class ThreadedTeam11Engine implements Runnable {

    /**
     * state where the move starts from
     */
    LaskerMorrisGameState state;

    /**
     * Resulting move is left here
     */
    public static LaskerMorrisGameState result;

    public static int depth;

    /**
     * Default constructor
     * @param state is the source state to play from
     */
    public ThreadedTeam11Engine(LaskerMorrisGameState state) {
        this.state = state;
    }

    /**
     * Runs a stoppable random engine.
     */
    public void run() {

        Team11Engine engine = new Team11Engine();

        depth = 1;

        while (!Thread.currentThread().isInterrupted()) {

            ThreadedTeam11Engine.result = engine.computeMove(state);

            depth++;
        }
    }

}