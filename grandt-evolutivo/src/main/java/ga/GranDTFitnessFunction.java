package ga;

import org.jgap.*;
import org.jgap.impl.DefaultConfiguration;
import model.Player;
import model.PlayersCatalogue;
import model.TeamConfiguration;
import java.util.List;
import java.util.LinkedList;
import org.jgap.*;
import org.jgap.impl.DefaultConfiguration;
import org.jgap.impl.IntegerGene;


public class GranDTFitnessFunction extends FitnessFunction {
    @Override
    protected double evaluate(IChromosome c) {
        List<Player> team = new LinkedList<Player>();
        for (int i = 0; i < 15 /* saber cuantos genes tiene un cromosoma*/; i++  ) {
        	int player = (int)(c.getGene(i).getAllele());
        	if ( player >= 0 && player < TeamGenerator.catalogue.numGoalkeepers() ) {
        		Player current = TeamGenerator.catalogue.getGoalkeeper(player); 	
        		}
        	else if( player >= TeamGenerator.catalogue.numGoalkeepers() && player < TeamGenerator.catalogue.numGoalkeepers() + TeamGenerator.catalogue.numDefenders() ){
        		Player current = TeamGenerator.catalogue.getDefender(player - TeamGenerator.catalogue.numGoalkeepers());
        	}
        	else if( player >= TeamGenerator.catalogue.numGoalkeepers() + TeamGenerator.catalogue.numDefenders() && player < TeamGenerator.catalogue.numGoalkeepers() + TeamGenerator.catalogue.numDefenders() + TeamGenerator.catalogue.numMidfielders()){
        		Player current = TeamGenerator.catalogue.getMidfielder(player - TeamGenerator.catalogue.numGoalkeepers() - TeamGenerator.catalogue.numDefenders());
        	}
        	else{
        		Player current = TeamGenerator.catalogue.getStriker( player - TeamGenerator.catalogue.numGoalkeepers() - TeamGenerator.catalogue.numDefenders() - TeamGenerator.catalogue.numMidfielders() );
        	}
        	team.add(current);
        }


        if (!isValidTeam(team)){
        	return 0;
        }
        else{
        	//Calcular que score suma, y si lo maximiza devolver el valor.
        	double valor = 0;
        	for (Player player: team ) {
        	 	valor = valor + player.getScore();
        	} 
        	return valor;
        }
    }
}