package engines;
import java.util.*;
import java.util.stream.Collectors;


    
import model.LaskerMorrisBoard;
import model.LaskerMorrisGameState;

/**
 * Engine implementing random moves.
 * 
 * @author aguirre
 *
 */
public class Team11Engine implements GameEngine {

   /**
    * For a given game state, returns all the possible successor game states.
    * @param e a game state.
    * @return a priority queue (whose priority is given by the heuristic evaluator) containing all successor moves given e.
    */
    public static PriorityQueue<LaskerMorrisGameState> successorStates(LaskerMorrisGameState e) {

        PriorityQueue<LaskerMorrisGameState> resultMax = new PriorityQueue<LaskerMorrisGameState>(24, (a,b) -> -(a.estimatedValue() - b.estimatedValue()));

        PriorityQueue<LaskerMorrisGameState> resultMin = new PriorityQueue<LaskerMorrisGameState>(24, (a,b) -> a.estimatedValue() - b.estimatedValue());
    

        if (e.isFinal() ) {

            return e.isMax()? resultMax : resultMin;
        }

        else {

        	for (int i = 0; i < LaskerMorrisBoard.POSITIONS; i++) {

        		if (e.getValue(i) == LaskerMorrisGameState.EMPTY ) {

        			if ((e.isWhitesTurn() && e.remainingWhiteStones() > 0 )||( !e.isWhitesTurn() && e.remainingBlackStones() > 0)) {

	        			LaskerMorrisGameState successor = e.clone();

	        			successor.putStone(i);

	        			if (successor.isInMill(i)) {

	        				LinkedList<LaskerMorrisGameState> listOfSuccessors = millStates(successor);

	        			    if (e.isMax()) resultMax.addAll(listOfSuccessors); else resultMin.addAll(listOfSuccessors);
                        }	

	        			else {

	        			    successor.setWhitesTurn(!e.isWhitesTurn());

                            successor.setClosedMill(false);

	        				if (e.isMax()) resultMax.add(successor); else resultMin.add(successor);
                            
	        			}
        			}
        		}

        		else {

                    if (e.canMoveStone() && ((e.getValue(i) == 1 && e.isWhitesTurn())|| (e.getValue(i) == 2 && !e.isWhitesTurn() ))) {

	        			int stone = e.getValue(i);

                        List<Integer> adjacencies = new LinkedList<Integer>();

	        			if ((e.isWhitesTurn() && e.remainingWhiteStones() == 0 && e.numberOfWhiteStonesOnBoard() == 3 )  || (!e.isWhitesTurn() && e.remainingBlackStones() == 0 && e.numberOfBlackStonesOnBoard() == 3)) {

                            for (int k = 0; k < LaskerMorrisBoard.POSITIONS; k++ ) {

                                if ( e.getValue(k) == LaskerMorrisGameState.EMPTY ) {

                                    adjacencies.add(k);
                                }
                            }
                        }

                        else {

                            adjacencies = LaskerMorrisBoard.getInstance().getAdjacencies(i);
                        }    

	        			for (int j = 0; j < adjacencies.size() ; j++) {

	    					if (e.getValue(adjacencies.get(j)) == 0) {

	        					LaskerMorrisGameState successor = e.clone();

		        				successor.moveStone(i, adjacencies.get(j));

		        				if (successor.isInMill(adjacencies.get(j))) {

			        				LinkedList<LaskerMorrisGameState> listOfSuccessors = millStates(successor);

                                    if (e.isMax()) resultMax.addAll(listOfSuccessors); else resultMin.addAll(listOfSuccessors);
			        			}

                                else {

                                    successor.setWhitesTurn(!e.isWhitesTurn());

			        				successor.setClosedMill(false);

                                    if (e.isMax()) resultMax.add(successor); else resultMin.add(successor);
			        			}
	    					}	
	        			}
                    }
        		}
        	}

            return e.isMax()? resultMax : resultMin;
        }
    }

   /**
    * Creates all states containing the possible ways in which you may remove an opponent's stone after forming a mill.
    * @param e a game state.
    * @return a list with all possible states formed from removing any available adversary's stone.
    */
    private static LinkedList<LaskerMorrisGameState> millStates (LaskerMorrisGameState e) {
        
        LinkedList<LaskerMorrisGameState> result = new LinkedList<LaskerMorrisGameState>();

        for (int j = 0; j < LaskerMorrisBoard.POSITIONS; j++ ) {

            if (e.isWhitesTurn() && e.getValue(j) == LaskerMorrisGameState.BLACK ) {

                LaskerMorrisGameState deletionState = e.clone();

                deletionState.removeStone(j);

                deletionState.setWhitesTurn(!e.isWhitesTurn());

                deletionState.setClosedMill(true);

                result.add(deletionState);
            }

            else if (!e.isWhitesTurn() && e.getValue(j) == LaskerMorrisGameState.WHITE) {

                LaskerMorrisGameState deletionState = e.clone();

                deletionState.removeStone(j);

                deletionState.setWhitesTurn(!e.isWhitesTurn());

                deletionState.setClosedMill(true);

                result.add(deletionState);
            }
        }

        return result;
    }

   /**
    * For a given game state, a maximum depth of search, and alpha and beta values for alpha-beta pruning, calculate an optimal move.
    * @param e a game state.
    * @param maxDepth the depth to which the game tree will be searched.
    * @param alpha for alpha-beta pruning optimization.
    * @param beta for alpha-beta pruning optimization.
    * @return value for the best possible play up to the given depth.
    */
    public static int miniMax(LaskerMorrisGameState e, int maxDepth, int alpha, int beta) {

        if (Thread.currentThread().isInterrupted()) return e.estimatedValue();

        if ( e.isFinal() || maxDepth == 0) {

            return e.estimatedValue();
        }

        else {

            if (e.isMax()) {

                PriorityQueue<LaskerMorrisGameState> successors = Team11Engine.successorStates(e);

                for (LaskerMorrisGameState s : successors) {

                    if (Thread.currentThread().isInterrupted()) break;

                    alpha = Math.max(alpha , miniMax(successors.peek(), maxDepth - 1 , alpha, beta));

                    if (beta <= alpha) break;
                }

                return alpha;
            }

            else {

                PriorityQueue<LaskerMorrisGameState> successors = Team11Engine.successorStates(e);

                for (LaskerMorrisGameState s : successors) {

                    if (Thread.currentThread().isInterrupted()) break;

                    beta = Math.min(beta , miniMax(successors.peek(), maxDepth - 1 , alpha, beta));

                    if (beta <= alpha) break;
                }

                return beta;
            }
        }
    }

   /**
    * For a given turn, compute the best possible play.
    * @param e a game state.
    * @return the game state to be played in the given turn.
    */
    public LaskerMorrisGameState computeMove(LaskerMorrisGameState e) {

        if (e.isFinal()) {

            return null;
        }

        else {

            if (e.isMax()) {
                
                PriorityQueue<LaskerMorrisGameState> successors = Team11Engine.successorStates(e);

                LaskerMorrisGameState result = successors.peek();

                int value = miniMax(successors.peek(), ThreadedTeam11Engine.depth, LaskerMorrisGameState.minValue(), LaskerMorrisGameState.maxValue());

                for (LaskerMorrisGameState s : successors) {

                    LaskerMorrisGameState current = successors.peek();

                    int currentValue = miniMax(current, ThreadedTeam11Engine.depth, LaskerMorrisGameState.minValue(), LaskerMorrisGameState.maxValue());

                    if (currentValue > value) {

                        value = currentValue;

                        result = current;
                    }
                }

                return result;
            } 

            else {

                PriorityQueue<LaskerMorrisGameState> successors = Team11Engine.successorStates(e);

                LaskerMorrisGameState result = successors.peek();

                int value = miniMax(successors.peek(), ThreadedTeam11Engine.depth, LaskerMorrisGameState.minValue(), LaskerMorrisGameState.maxValue());

                for (LaskerMorrisGameState s : successors) {

                    LaskerMorrisGameState current = successors.peek();

                    int currentValue = miniMax(current, ThreadedTeam11Engine.depth, LaskerMorrisGameState.minValue(), LaskerMorrisGameState.maxValue());

                    if (currentValue < value) {

                        value = currentValue;

                        result = current;
                    }
                }

                return result;
            }
        }  
    }

}