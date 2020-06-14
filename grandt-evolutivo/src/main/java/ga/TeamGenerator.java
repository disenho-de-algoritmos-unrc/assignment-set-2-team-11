package ga;
import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.LinkedList;

import org.jgap.InvalidConfigurationException;

import model.Player;
import model.PlayersCatalogue;
import model.TeamConfiguration;
import org.jgap.*;
import org.jgap.impl.DefaultConfiguration;
import org.jgap.impl.IntegerGene;

public class TeamGenerator {

    PlayersCatalogue catalogue;

    TeamConfiguration configuration = new TeamConfiguration();

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


    public void setTeamConfiguration(TeamConfiguration newConfiguration) {
        this.configuration = newConfiguration;
    }


    /**
     * Generates a near-optimal team using a genetic algorithm. 
     * @return the generated team, as a list of players
     * @throws InvalidConfigurationException when called on an invalid configuration (JGAP).
     */
    public List<Player> generateTeam() throws InvalidConfigurationException {
        Configuration conf = new DefaultConfiguration();
        conf.setPreservFittestIndividual(true);
        conf.setKeepPopulationSizeConstant(false);
        FitnessFunction myFunc =
                new GranDTFitnessFunction(  );
        conf.setFitnessFunction( myFunc );
        Gene[] sampleGenes = new Gene[ 15 ];
        sampleGenes[0] = new IntegerGene(conf,1,707);
        sampleGenes[1] = new IntegerGene(conf,1,707);
        sampleGenes[2] = new IntegerGene(conf,1,707);
        sampleGenes[3] = new IntegerGene(conf,1,707);
        sampleGenes[4] = new IntegerGene(conf,1,707);
        sampleGenes[5] = new IntegerGene(conf,1,707);
        sampleGenes[6] = new IntegerGene(conf,1,707);
        sampleGenes[7] = new IntegerGene(conf,1,707);
        sampleGenes[8] = new IntegerGene(conf,1,707);
        sampleGenes[9] = new IntegerGene(conf,1,707);
        sampleGenes[10] = new IntegerGene(conf,1,707);
        sampleGenes[11] = new IntegerGene(conf,1,707);
        sampleGenes[12] = new IntegerGene(conf,1,707);
        sampleGenes[13] = new IntegerGene(conf,1,707);
        sampleGenes[14] = new IntegerGene(conf,1,707);
        Chromosome sampleChromosome = new Chromosome(conf, sampleGenes);
        conf.setSampleChromosome( sampleChromosome );

        conf.setPopulationSize( 500 );
        Genotype population = Genotype.randomInitialGenotype(conf);
        int MAX_ALLOWED_EVOLUTION = 30;
        for (int i = 0; i<=MAX_ALLOWED_EVOLUTION; i++) {
            population.evolve();
        }
        IChromosome equipo =population.getFittestChromosome();
        return null;
    }

}
