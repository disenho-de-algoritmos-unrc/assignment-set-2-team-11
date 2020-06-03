package model;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 * Catalogue of players.
 * 
 * @author aguirre
 *
 */
public class PlayersCatalogue {
    
    public List<Player> goalkeepers;
    
    public List<Player> defenders;
    
    public List<Player> midfielders;
    
    public List<Player> strikers;
    
    
    /**
     * Creates a catalogue from an excel file.
     * @param resourceDirectory is the file name of the excel file containing the catalogue.
     * @throws IOException 
     */
    public PlayersCatalogue(Path resourceDirectory) throws IOException {
        if (resourceDirectory == null) throw new IllegalArgumentException("null file name");

        String absolutePath = resourceDirectory.toFile().getAbsolutePath();
        FileInputStream file = new FileInputStream(absolutePath);

        //Create Workbook instance holding reference to .xlsx file
        XSSFWorkbook workbook = new XSSFWorkbook(file);

        //Get first/desired sheet from the workbook
        XSSFSheet sheet = workbook.getSheetAt(0);

        goalkeepers = new ArrayList<Player>();
        defenders = new ArrayList<Player>();
        midfielders = new ArrayList<Player>();
        strikers = new ArrayList<Player>();

        Iterator<Row> rowIterator = sheet.iterator();

        while (rowIterator.hasNext()) {
            Row row = rowIterator.next();
            if ((row.getCell(0) == null) || (row.getCell(0).getCellType()== Cell.CELL_TYPE_BLANK)) break;
            Player player = new Player(row);
            switch (player.getPosition()) {
            case ARQ:
                goalkeepers.add(player);
                break;
            case DEF:
                defenders.add(player);
                break;
            case VOL:
                midfielders.add(player);
                break;
            case DEL:
                strikers.add(player);
                break;
            default:
                break;
            }
        }

        workbook.close();
        file.close();
    }

    /**
     * Returns the number of goalkeepers in the catalogue
     * @return the number of goalkeepers in the catalogue
     */
    public int numGoalkeepers() {
        return goalkeepers.size();      
    }

    /**
     * Returns the ith goalkeeper in the catalogue
     * @param i is the index of the goalkeeper
     * @return the ith goalkeeper.
     */
    public Player getGoalkeeper(int i) {
        if (i < 0 || i >= goalkeepers.size()) throw new IllegalArgumentException("invalid goalkeeper index");
        return goalkeepers.get(i);
    }

    /**
     * Returns the ith defender in the catalogue
     * @param i is the index of the defender
     * @return the ith defender.
     */
    public Player getDefender(int i) {
        if (i < 0 || i >= defenders.size()) throw new IllegalArgumentException("invalid defender index");
        return defenders.get(i);
    }
    
    /**
     * Returns the ith midfielder in the catalogue
     * @param i is the index of the midfielder
     * @return the ith midfielder.
     */
    public Player getMidfielder(int i) {
        if (i < 0 || i >= midfielders.size()) throw new IllegalArgumentException("invalid midfielder index");
        return midfielders.get(i);
    }
    
    /**
     * Returns the ith striker in the catalogue
     * @param i is the index of the striker
     * @return the ith striker.
     */
    public Player getStriker(int i) {
        if (i < 0 || i >= strikers.size()) throw new IllegalArgumentException("invalid striker index");
        return strikers.get(i);
    }
    
    /**
     * Returns the number of defenders in the catalogue
     * @return the number of defenders in the catalogue
     */
    public int numDefenders() {
        return defenders.size(); 
    }

    /**
     * Returns the number of midfielders in the catalogue
     * @return the number of midfielders in the catalogue
     */
    public int numMidfielders() {
        return midfielders.size(); 
    }

    /**
     * Returns the number of strikers in the catalogue
     * @return the number of strikers in the catalogue
     */
    public int numStrikers() {
        return strikers.size(); 
    }

}
