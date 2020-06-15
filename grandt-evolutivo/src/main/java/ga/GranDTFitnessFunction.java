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

    TeamConfiguration myTeam;

   	PlayersCatalogue myCatalogue;

    public GranDTFitnessFunction(TeamConfiguration team, PlayersCatalogue catalogue){
    	this.myTeam = team;
    	this.myCatalogue = catalogue;
    }


    @Override
    protected double evaluate(IChromosome c) {
       
        List<Player> candidate = new LinkedList<Player>();
        for (int i = 0; i < 15 ; i++ ) {
        	int player = (int) (c.getGene(i).getAllele()) -1;
        	if (player < myCatalogue.numGoalkeepers()) {
        		candidate.add(myCatalogue.getGoalkeeper(player) ); 	
        	}
        	else{
        		player = player - myCatalogue.numGoalkeepers();
        	}
        	if(player <  myCatalogue.numDefenders() ){
        		candidate.add(myCatalogue.getDefender(player));
        	}
        	else{
        		player = player - myCatalogue.numDefenders();
        	}
        	if(player < myCatalogue.numMidfielders()){
        		candidate.add(myCatalogue.getMidfielder(player));
        	}
        	else{
        		player = player - myCatalogue.numMidfielders();
        		candidate.add(myCatalogue.getStriker(player));
        	}
        }
       
        if (!myTeam.isValidTeam(candidate)) return 0;
        double valor = 0;
        for (Player player : candidate ) {
                valor = valor + player.getScore();
        } 
        return valor;
    }
}