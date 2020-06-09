package engines;
import java.util.List;
import java.util.LinkedList;
import java.util.Random;

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
	        			sucesor.setWhitesTurn(!e.isWhitesTurn());
	        			if (sucesor.isInMill(i)) {
	        				for (int j = 0; j < LaskerMorrisBoard.POSITIONS; j++ ) {
	        					if (e.isWhitesTurn() && sucesor.getValue(j) == LaskerMorrisGameState.BLACK ) {
	        						LaskerMorrisGameState sucesorDelete = sucesor.clone();
	        						sucesorDelete.removeStone(j);
	        						result.add(sucesorDelete);
	        					}
	        					else if(!e.isWhitesTurn() && sucesor.getValue(j) == LaskerMorrisGameState.WHITE){
	        						LaskerMorrisGameState sucesorDelete = sucesor.clone();
	        						sucesorDelete.removeStone(j);
	        						result.add(sucesorDelete);
	        					}
	        				}
	        			}	
	        			else{
	        				result.add(sucesor);
	        			}
        			}
        		}
        		else if(e.canMoveStone()){
	        		if ((e.getValue(i) == 1 && e.isWhitesTurn() )|| (e.getValue(i) == 2 && !e.isWhitesTurn())) {
	        			int ficha = e.getValue(i);
	        			List<Integer> adyacencias = LaskerMorrisBoard.getInstance().getAdjacencies(i);
	        			for (int j = 0; j < adyacencias.size() ; j++) {
	    					if(e.getValue(adyacencias.get(j)) == 0){
	        					LaskerMorrisGameState sucesor = e.clone();
		        				sucesor.moveStone(i,adyacencias.get(j));
		        				sucesor.setWhitesTurn(!e.isWhitesTurn());
		        				if (sucesor.isInMill(adyacencias.get(j))) {
			        				for (int k = 0; k < LaskerMorrisBoard.POSITIONS; k++ ) {
			        					if (e.isWhitesTurn() && sucesor.getValue(k) == LaskerMorrisGameState.BLACK ) {
			        						LaskerMorrisGameState sucesorDelete = sucesor.clone();
			        						sucesorDelete.removeStone(k);
			        						result.add(sucesorDelete);
			        					}
			        					else if(!e.isWhitesTurn() && sucesor.getValue(k) == LaskerMorrisGameState.WHITE){
			        						LaskerMorrisGameState sucesorDelete = sucesor.clone();
			        						sucesorDelete.removeStone(k);
			        						result.add(sucesorDelete);
			        					}
			        				}
			        			}	
			        			else{
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

    public static int miniMax(LaskerMorrisGameState e, int maxDepth) {
        if ( e.isFinal() || maxDepth == 0) {
            if (e.whiteWins()) return 1;
            if (e.blackWins()) return -1;
            return e.estimatedValue();
        }
        else {
            List<LaskerMorrisGameState> sucesores = NoRandomEngine.sucesores(e);
            int valor = miniMax(sucesores.get(0),maxDepth-1);
            for (int i = 1; i < sucesores.size(); i++) {
                LaskerMorrisGameState corriente = sucesores.get(i);
                int valorCorriente = miniMax(corriente,maxDepth-1);
                if (e.isMax() && valorCorriente > valor) valor = valorCorriente;
                if (!e.isMax() && valorCorriente < valor) valor = valorCorriente;
            }
            return valor;
        }
    }

    public  LaskerMorrisGameState computeMove(LaskerMorrisGameState e) {
        int maxDepth = 5;
        if (e.isFinal()) return null;
        else {
            if (e.isMax()) {
                // jugar como blancas
                List<LaskerMorrisGameState> sucesores = NoRandomEngine.sucesores(e);
                int valor = miniMax(sucesores.get(0),maxDepth);
                LaskerMorrisGameState resultado = sucesores.get(0);
                for (int i = 1; i < sucesores.size(); i++) {
                    LaskerMorrisGameState corriente = sucesores.get(i);
                    int valorCorriente = miniMax(corriente,maxDepth);
                    if (valorCorriente > valor) {
                        valor = valorCorriente;
                        resultado = corriente;
                    }
                }
                return resultado;   
            }
            else {
                // jugar como negras
                List<LaskerMorrisGameState> sucesores = sucesores(e);
                int valor = miniMax(sucesores.get(0),maxDepth);
                LaskerMorrisGameState resultado = sucesores.get(0);
                for (int i = 1; i < sucesores.size(); i++) {
                    LaskerMorrisGameState corriente = sucesores.get(i);
                    int valorCorriente = miniMax(corriente,maxDepth);
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
    }*/


}