package runners;


import java.util.concurrent.TimeUnit;

import ThreadingUtils.TimeLimitedCodeBlock;
import engines.ThreadedNoRandomEngine;
import model.LaskerMorrisGameState;

/**
 * A sample application where white plays black using random engines.
 * 
 * @author aguirre
 *
 */
public class NoRandomGameWithTimeout {
    
    /**
     * Max number of total moves before considering a match a draw.
     */
    public static final int MAXMOVES = 120;
    
    /**
     * Timeout in seconds for each player's timeout. 
     */
    public static final int TIMEOUT = 60;
    
	/**
	 * Creates a game where whites play blacks using random engines for both players.
	 * Game is considered a draw if MAXMOVES total moves are reached without a winner.
	 */
	public static void main(String[] args) {

		LaskerMorrisGameState state = new LaskerMorrisGameState();
		
		System.out.println(state.toString());
		
		int moves = 0;
		
		boolean error = false;
		
		while (!error && !state.isFinal() && moves < MAXMOVES) {
		    
		    try {
                TimeLimitedCodeBlock.runWithTimeout(new ThreadedNoRandomEngine(state), TIMEOUT, TimeUnit.SECONDS);
            }
            catch (Exception e) {
                // Got timeout.
            }
            LaskerMorrisGameState result = ThreadedNoRandomEngine.result;
            if (result == null) {
                error = true;
            }
            else {
                state = result;
                System.out.println(state.toString());
            }
            
            moves++;
		    
		}
		
		if (state.isFinal()) {
		    if (state.whiteWins()) {
		        System.out.println("WHITE WINS!");
		    }
		    else {
		        System.out.println("BLACK WINS!");
		    }
		}
		else {
		    if (error) {
		        System.out.println("Error in move. " + (state.isWhitesTurn() ? "BLACK WINS!" : "WHITE WINS"));
		    }
		    else {
		        System.out.println("It's a draw.");
		    }
		}
	}

}
