package ga;

import static org.junit.Assert.*;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import org.jgap.InvalidConfigurationException;
import org.junit.Test;

import model.Player;
import model.TeamConfiguration;

public class TeamGeneratorTest {

    @Test
    public void test() throws InvalidConfigurationException {
        Path resourceDirectory = Paths.get("src", "test", "resources", "players-statistics.xlsx");
        
        TeamConfiguration configuration = new TeamConfiguration();
        
        TeamGenerator generator = new TeamGenerator(resourceDirectory);
        
        generator.setTeamConfiguration(configuration);
        
        boolean validTeamObtained = false;
        int maxIterations = 30;
        int iteration = 1;
        while (iteration <= maxIterations && !validTeamObtained) {
            
            List<Player> result = generator.generateTeam(); 

            if (configuration.isValidTeam(result)) {
                validTeamObtained = true;
                int value = 0;
                // in case you want to see the team... uncomment the following
                // for (Player player: result) {

                //    System.out.println(player.getName() + " " + player.getPosition());

               //     value += player.getScore();

               //  }

               //  System.out.println(value);
            }
            
            iteration++;
        }
        
        assertTrue(validTeamObtained);
    }

}
