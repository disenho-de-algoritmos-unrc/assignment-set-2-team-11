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
import java.util.Arrays;


public class GranDTFitnessFunction extends FitnessFunction {

   /**
    * A catalogue of all the available players.
    */
    TeamConfiguration myTeam;

   /**
    * A description of how the team is composed.
    */
   	PlayersCatalogue catalogue;


   /**
    * Class constructor.
    * @param team A given team configuration
    * @param catalogue A given players catalogue.
    */
    public GranDTFitnessFunction(TeamConfiguration team, PlayersCatalogue catalogue) {

    	this.myTeam = team;

    	this.catalogue = catalogue;
    }

   /**
    * Determines the fitness for any given chromosome
    * @param c A given chromosome
    * @return A value expressing the fitness of the given chromosome.
    */
    @Override
    protected double evaluate(IChromosome c) {

        List<Player> team = TeamGenerator.chromosomeToTeam(c, myTeam, catalogue);
       
        if (team == null || !myTeam.isValidTeam(team)) return 0;

        double value = 0;

        for (Player player : team ) {

                value += player.getScore();
        } 

        return value;
    }
}