package ga;
import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

import org.jgap.InvalidConfigurationException;

import model.Player;
import model.PlayersCatalogue;
import model.TeamConfiguration;

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
        // TODO Implement this method
        return null;
    }

}
