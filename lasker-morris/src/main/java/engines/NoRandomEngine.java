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
public class NoRandomEngine implements GameEngine {
	 // Funcion que genera los sucesores de un estado dado
    public static LinkedList<LaskerMorrisGameState> sucesores (LaskerMorrisGameState e){
        LinkedList<LaskerMorrisGameState> result = new LinkedList<LaskerMorrisGameState>();
      
        if (e.isFinal() ) {
            return result;
        }
        else{
        	for (int i = 0; i < LaskerMorrisBoard.POSITIONS; i++) {
        		if (e.getValue(i) == 0 ) {
        			if ((e.isWhitesTurn() && e.remainingWhiteStones() > 0 )||( !e.isWhitesTurn() && e.remainingBlackStones() > 0)) {
	        			LaskerMorrisGameState sucesor = e.clone();
	        			sucesor.putStone(i);
	        			if (sucesor.isInMill(i)) {
	        				LinkedList<LaskerMorrisGameState> listSucesores = millsStates(sucesor);
	        			    result.addAll(listSucesores);
                        }	
	        			else{
	        			    sucesor.setWhitesTurn(!e.isWhitesTurn());
                            sucesor.setClosedMill(false);
	        				result.add(sucesor);
	        			}
        			}
        		}
        		else{
                    if (e.canMoveStone() && ((e.getValue(i) == 1 && e.isWhitesTurn())|| (e.getValue(i) == 2 && !e.isWhitesTurn() ))){
	        			int ficha = e.getValue(i);
                        List<Integer> adyacencias = new LinkedList<Integer>();
	        			if ((e.isWhitesTurn() && e.remainingWhiteStones() == 0 && e.numberOfWhiteStonesOnBoard() == 3 )  || (!e.isWhitesTurn() && e.remainingBlackStones() == 0 && e.numberOfBlackStonesOnBoard() == 3)){       
                            for (int k = 0; k < LaskerMorrisBoard.POSITIONS; k++ ) {
                                if ( e.getValue(k) == LaskerMorrisGameState.EMPTY ) {
                                    adyacencias.add(k);
                                }
                            }
                        }
                        else{
                            adyacencias = LaskerMorrisBoard.getInstance().getAdjacencies(i);
                        }    
	        			for (int j = 0; j < adyacencias.size() ; j++) {
	    					if(e.getValue(adyacencias.get(j)) == 0){
	        					LaskerMorrisGameState sucesor = e.clone();
		        				sucesor.moveStone(i,adyacencias.get(j));
		        				if (sucesor.isInMill(adyacencias.get(j))) {
			        				LinkedList<LaskerMorrisGameState> listSucesores = millsStates(sucesor);
                                    result.addAll(listSucesores);
			        			}
                                else{
                                    sucesor.setWhitesTurn(!e.isWhitesTurn());
			        				sucesor.setClosedMill(false);
                                    result.add(sucesor);
			        			}
	    					}	
	        			}
                    }
        		}
        	}
        	return result;
        }
    }


    private static LinkedList<LaskerMorrisGameState> millsStates (LaskerMorrisGameState e){
        
        LinkedList<LaskerMorrisGameState> result = new LinkedList<LaskerMorrisGameState>();

        for (int j = 0; j < LaskerMorrisBoard.POSITIONS; j++ ) {
            if (e.isWhitesTurn() && e.getValue(j) == LaskerMorrisGameState.BLACK ) {
                LaskerMorrisGameState sucesorDelete = e.clone();
                sucesorDelete.removeStone(j);
                sucesorDelete.setWhitesTurn(!e.isWhitesTurn());
                sucesorDelete.setClosedMill(true);
                result.add(sucesorDelete);
            }
            else if(!e.isWhitesTurn() && e.getValue(j) == LaskerMorrisGameState.WHITE){
                LaskerMorrisGameState sucesorDelete = e.clone();
                sucesorDelete.removeStone(j);
                sucesorDelete.setWhitesTurn(!e.isWhitesTurn());
                sucesorDelete.setClosedMill(true);
                result.add(sucesorDelete);
            }
        }
        return result;
    }

    public static int miniMax(LaskerMorrisGameState e, int maxDepth, int alpha, int beta) {
        if (Thread.currentThread().isInterrupted())
            return e.estimatedValue();
        if ( e.isFinal() || maxDepth == 0) {
            return e.estimatedValue();
        }
        else {
            if (e.isMax()) {
                List<LaskerMorrisGameState> sucesores = NoRandomEngine.sucesores(e);
                for (int i = 0 ; i < sucesores.size() ; i++ ) {
                    if (Thread.currentThread().isInterrupted())
                        break;
                    alpha = Math.max(alpha , miniMax(sucesores.get(i), maxDepth-1 , alpha, beta));
                    if (beta <= alpha ) {
                        break;
                    }
                }
                return alpha;
            }
            else{
                List<LaskerMorrisGameState> sucesores = NoRandomEngine.sucesores(e);
                for (int i = 0 ; i < sucesores.size() ; i++ ) {
                    if (Thread.currentThread().isInterrupted())
                        break;
                    beta = Math.min(beta , miniMax(sucesores.get(i), maxDepth-1 , alpha, beta));
                    if (beta <= alpha ) {
                        break;
                    }
                }
                return beta;
            }
        }
    }


    public  LaskerMorrisGameState computeMove(LaskerMorrisGameState e, int maxDepth) {
        if (e.isFinal()) return null;
        else {
            if (e.isMax()) {
                // jugar como blancas
                List<LaskerMorrisGameState> sucesores = NoRandomEngine.sucesores(e);
                LaskerMorrisGameState resultado = sucesores.get(0);
                int valor = miniMax(sucesores.get(0), maxDepth, -10000, 10000);
                for (int i = 1; i < sucesores.size(); i++) {
                    LaskerMorrisGameState corriente = sucesores.get(i);
                    int valorCorriente = miniMax(corriente, maxDepth, -10000, 10000);
                    if (valorCorriente > valor) {
                        valor = valorCorriente;
                        resultado = corriente;
                    }
                }
                return resultado;
            } else {
                // jugar como negras
                List<LaskerMorrisGameState> sucesores = NoRandomEngine.sucesores(e);
                LaskerMorrisGameState resultado = sucesores.get(0);
                int valor = miniMax(sucesores.get(0), maxDepth, -10000, 10000);
                for (int i = 1; i < sucesores.size(); i++) {
                    LaskerMorrisGameState corriente = sucesores.get(i);
                    int valorCorriente = miniMax(corriente, maxDepth, -10000, 10000);
                    if (valorCorriente < valor) {
                        valor = valorCorriente;
                        resultado = corriente;
                    }
                }
                return resultado;
            }
        }  
    }

         /**
     * Quickly computes a move when issued an interrupt or timeout.
     * @param state is the state from which to move
     * @return the first available move, prioritizing putting a stone.
     */
    public LaskerMorrisGameState failSafeResult(LaskerMorrisGameState state) {
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

        //System.out.println("Removing!");

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




}