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

    TeamConfiguration myTeam;

   	PlayersCatalogue catalogue;

    public GranDTFitnessFunction(TeamConfiguration team, PlayersCatalogue catalogue) {

    	this.myTeam = team;

    	this.catalogue = catalogue;
    }


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