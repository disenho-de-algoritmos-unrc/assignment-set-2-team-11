package model;

import java.util.List;

/**
 * Captures a team configuration, including the budget,
 * team size and formation.
 * @author aguirre
 *
 */
public class TeamConfiguration {

    /**
     * Constructor that receives budget, team size and number of players.
     * @param budget
     * @param goalkeepers
     * @param defenders
     * @param midfielders
     * @param strikers
     */
    public TeamConfiguration(int budget, int goalkeepers, int defenders, int midfielders, int strikers) {
        super();
        this.budget = budget;
        this.goalkeepers = goalkeepers;
        this.defenders = defenders;
        this.midfielders = midfielders;
        this.strikers = strikers;
        this.teamSize = this.goalkeepers + this.defenders + this.midfielders + this.strikers;
    }

    /**
     * Default team configuration constructor.
     * It sets budget to 65000000, team size to 15, 
     * and formation to 1-4-3-3
     * with one substitution per position.
     */
    public TeamConfiguration() { }

    /**
     * Budget for team generation
     */
    private int budget = 65000000;

    /**
     * Team size including substitutions
     */
    private int teamSize = 15;

    /**
     * Goalkeepers including substitions
     */
    private int goalkeepers = 2;

    /**
     * Defenders including substitions
     */
    private int defenders = 5;

    /**
     * Midfielders including substitions
     */
    private int midfielders = 4;

    /**
     * Strikers including substitutions
     */
    private int strikers = 4;

    /**
     * @return the budget
     */
    public int getBudget() {
        return budget;
    }

    /**
     * @return the teamSize
     */
    public int getTeamSize() {
        return teamSize;
    }

    /**
     * @return the goalkeepers
     */
    public int getGoalkeepers() {
        return goalkeepers;
    }

    /**
     * @return the defenders
     */
    public int getDefenders() {
        return defenders;
    }

    /**
     * @return the midfielders
     */
    public int getMidfielders() {
        return midfielders;
    }

    /**
     * @return the strikers
     */
    public int getStrikers() {
        return strikers;
    }

    /**
     * @param budget the budget to set
     */
    public void setBudget(int budget) {
        this.budget = budget;
    }

    /**
     * @param teamSize the teamSize to set
     */
    public void setTeamSize(int teamSize) {
        this.teamSize = teamSize;
    }

    /**
     * @param goalkeepers the goalkeepers to set
     */
    public void setGoalkeepers(int goalkeepers) {
        this.goalkeepers = goalkeepers;
    }

    /**
     * @param defenders the defenders to set
     */
    public void setDefenders(int defenders) {
        this.defenders = defenders;
    }

    /**
     * @param midfielders the midfielders to set
     */
    public void setMidfielders(int midfielders) {
        this.midfielders = midfielders;
    }

    /**
     * @param strikers the strikers to set
     */
    public void setStrikers(int strikers) {
        this.strikers = strikers;
    }

    /**
     * Checks whether a list of players is a valid team.
     * @param team is the list of players to be checked.
     * @return true iff the team is valid, i.e., satisfies formation and is within budget.
     */
    public boolean isValidTeam(List<Player> team) {
        if (team == null) throw new IllegalArgumentException("null list of players");
        if (team.size() != this.getTeamSize()) {
            return false;
        }
        int value = 0;
        int goaliesCount = 0;
        int defendersCount = 0;
        int midfieldersCount = 0;
        int strikersCount = 0;
        for (Player currPlayer: team) {
            if (currPlayer == null) {
                return false;
            }
            value += currPlayer.getValue();
            switch (currPlayer.getPosition()) {
            case ARQ:
                goaliesCount++;
                break;
            case DEF:
                defendersCount++;
                break;
            case VOL:
                midfieldersCount++;
                break;
            case DEL:
                strikersCount++;
                break;
            default: break;
            }

        }
        boolean result = true;
        result = result && (value <= this.getBudget());
        result = result && (goaliesCount == this.getGoalkeepers());
        result = result && (defendersCount == this.getDefenders());
        result = result && (midfieldersCount == this.getMidfielders());
        result = result && (strikersCount == this.getStrikers());
        return result;
    }

}
