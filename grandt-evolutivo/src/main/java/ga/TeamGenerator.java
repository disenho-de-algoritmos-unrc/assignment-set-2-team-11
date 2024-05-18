package ga;
import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.LinkedList;
import java.util.Arrays;

import org.jgap.InvalidConfigurationException;

import model.Player;
import model.PlayersCatalogue;
import model.TeamConfiguration;
import org.jgap.*;
import org.jgap.impl.DefaultConfiguration;
import org.jgap.impl.IntegerGene;

public class TeamGenerator {

   /**
    * A catalogue of all the available players.
    */
    PlayersCatalogue catalogue;

   /**
    * A description of how the team is composed.
    */
    TeamConfiguration configuration = new TeamConfiguration();

    int POPULATION_SIZE = 4000;

    int GENERATIONS = 40;

    /**
     * Creates the team generator receiving as a parameter the catalogue of players.
     * The catalogue of players comes in an excel sheet.
     * @param resourceDirectory is the name of the excel file containing the players' catalogue.
     */
    public TeamGenerator(Path resourceDirectory) {
        try {
            catalogue = new PlayersCatalogue(resourceDirectory);
        } catch (IOException e) {
            throw new IllegalArgumentException("problem loading catalogue file");
        }
    }

   /**
    * Class setter.
    * @param newConfiguration The given configuration to be set.
    */
    public void setTeamConfiguration(TeamConfiguration newConfiguration) {
        this.configuration = newConfiguration;
    }

   /**
    * Parses and converts a chromosome into a list of players (a team).
    * @param c A given chromosome.
    * @param config The given team configuration.
    * @param catalogue The given players catalogue.
    * @return A list of players given by the chromosome c.
    */
    public static List<Player> chromosomeToTeam(IChromosome c, TeamConfiguration config, PlayersCatalogue catalogue) {

        Integer[] candidates = new Integer[config.getTeamSize()];

        for (int i = 0 ; i < candidates.length ; i++ ) {

            candidates[i] = (Integer) c.getGene(i).getAllele();
        }

        if (!Arrays.equals(candidates, Arrays.stream(candidates).distinct().toArray())) return null; 

        int goalies = 0;
        int mids = 0;
        int defs = 0;
        int stks = 0;

        List<Player> team = new LinkedList<Player>();

        for (int i = 0 ; i < candidates.length; i++) {

            if (goalies < 2 && candidates[i] < catalogue.numGoalkeepers()) {

                team.add(catalogue.getGoalkeeper(candidates[i]));

                goalies++;
            }

            else if (defs < 5 && candidates[i] < catalogue.numDefenders()) {

                team.add(catalogue.getDefender(candidates[i]));

                defs++;
            }

            else if (mids < 4 && candidates[i] < catalogue.numMidfielders()) {

                team.add(catalogue.getMidfielder(candidates[i]));

                mids++;

            }

            else if (stks < 4 && candidates[i] < catalogue.numStrikers()) {

                team.add(catalogue.getStriker(candidates[i]));

                stks++;
            }
        }

        return team;
    }


    /**
     * Generates a near-optimal team using a genetic algorithm. 
     * @return the generated team, as a list of players
     * @throws InvalidConfigurationException when called on an invalid configuration (JGAP).
     */
    public List<Player> generateTeam() throws InvalidConfigurationException {

        Configuration conf = new DefaultConfiguration();


        FitnessFunction myFunc = new GranDTFitnessFunction(this.configuration,this.catalogue);

        conf.setFitnessFunction( myFunc );

        Gene [] sampleGenes = new Gene[ configuration.getTeamSize()];

        sampleGenes[0] = new IntegerGene(conf, 1, catalogue.numGoalkeepers());
        sampleGenes[1] = new IntegerGene(conf, 1, catalogue.numGoalkeepers());

        sampleGenes[2] = new IntegerGene(conf, 1, catalogue.numDefenders());
        sampleGenes[3] = new IntegerGene(conf, 1, catalogue.numDefenders());
        sampleGenes[4] = new IntegerGene(conf, 1, catalogue.numDefenders());
        sampleGenes[5] = new IntegerGene(conf, 1, catalogue.numDefenders());
        sampleGenes[6] = new IntegerGene(conf, 1, catalogue.numDefenders());

        sampleGenes[7] = new IntegerGene(conf, 1, catalogue.numMidfielders());
        sampleGenes[8] = new IntegerGene(conf, 1, catalogue.numMidfielders());
        sampleGenes[9] = new IntegerGene(conf, 1, catalogue.numMidfielders());
        sampleGenes[10] = new IntegerGene(conf, 1, catalogue.numMidfielders());

        sampleGenes[11] = new IntegerGene(conf, 1, catalogue.numStrikers());
        sampleGenes[12] = new IntegerGene(conf, 1, catalogue.numStrikers());
        sampleGenes[13] = new IntegerGene(conf, 1, catalogue.numStrikers());
        sampleGenes[14] = new IntegerGene(conf, 1, catalogue.numStrikers());

        Chromosome sampleChromosome = new Chromosome(conf, sampleGenes);

        conf.setSampleChromosome(sampleChromosome);

        conf.setPopulationSize(POPULATION_SIZE);

        int generations = GENERATIONS;

        Genotype population = Genotype.randomInitialGenotype(conf);

        IChromosome bestTeamSoFar = population.getFittestChromosome();

        for (int i = 0; i < GENERATIONS; i++) {
            
            population.evolve();

            bestTeamSoFar = population.getFittestChromosome();
        }       

       return chromosomeToTeam(bestTeamSoFar, configuration, catalogue);
    }

}
