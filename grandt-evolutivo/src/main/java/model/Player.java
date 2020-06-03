package model;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;

/**
 * Captures the information of a player.
 * @author aguirre
 *
 */
public class Player {
    
    /**
     * Name of the player
     */
    private String name = "No Name";
    
    /**
     * Club of the player
     */
    private String club = "No Club";
    
    /**
     * Position of the player
     */
    private Position position = Position.VOL;
    
    /**
     * Value (price) associated with the player
     */
    private double value = 0d;
    
    /**
     * Score estimated for the player
     */
    private double score = 0d;

    /**
     * Default constructor for Player class.
     * @param name is the name of the player.
     * @param club is the club of the player.
     * @param position is the position of the player.
     * @param value is the value of the player.
     * @param score is the estimated score of the player.
     */
    public Player(String name, String club, Position position, double value, double score) {
        super();
        this.name = name;
        this.club = club;
        this.position = position;
        this.value = value;
        this.score = score;
    }

    public Player(Row row) {
        if (row == null) throw new IllegalArgumentException("null row");
        if (row.getLastCellNum() == 0) throw new IllegalArgumentException("empty row");
        int col = 0;
        while (col < row.getLastCellNum()) {
            Cell cell = row.getCell(col);
            switch (col) {
            case 0:
                // Name
                if (cell == null || cell.getStringCellValue() == null) {
                    throw new IllegalArgumentException("invalid player name");
                }
                else {
                    this.name = cell.getStringCellValue();
                }
                break;
            case 1:
                // Position
                if (cell == null || cell.getStringCellValue() == null) {
                    throw new IllegalArgumentException("invalid player position");
                }
                else {
                    this.position = Position.valueOf(cell.getStringCellValue());
                }
                break;
            case 2:
                // Club
                if (cell == null || cell.getStringCellValue() == null) {
                    throw new IllegalArgumentException("invalid player club");
                }
                else {
                    this.club = cell.getStringCellValue();
                }
                break;
            case 3:
                // Value 
                if (cell == null) {
                    throw new IllegalArgumentException("invalid player value");
                }
                else {
                    this.value = cell.getNumericCellValue();
                }
                break;
            case 4:
                // Score
                if (cell == null) {
                    this.score = 0;
                }
                else {
                    this.score = cell.getNumericCellValue();
                }
                break;
            }   
            col++;
        }
        
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the club
     */
    public String getClub() {
        return club;
    }

    /**
     * @param club the club to set
     */
    public void setClub(String club) {
        this.club = club;
    }

    /**
     * @return the position
     */
    public Position getPosition() {
        return position;
    }

    /**
     * @param position the position to set
     */
    public void setPosition(Position position) {
        this.position = position;
    }

    /**
     * @return the value
     */
    public double getValue() {
        return value;
    }

    /**
     * @param value the value to set
     */
    public void setValue(int value) {
        this.value = value;
    }

    /**
     * @return the score
     */
    public double getScore() {
        return score;
    }

    /**
     * @param score the score to set
     */
    public void setScore(int score) {
        this.score = score;
    }
    
    
    
}
