package model;

import java.util.LinkedList;
import java.util.List;

/**
 * Represents static board information, such as adjacencies and the 
 * coordinates of all mills in the board.
 * Since static board info may be accessible by different classes,
 * the class is implemented using the Singleton Pattern.
 * 
 * @author aguirre
 *
 */
public class LaskerMorrisBoard {
    
    /**
     * Number of positions of the board. See the representation below.
     */
    public static final int POSITIONS = 24;

    /**
     * Maximum number of stones per player
     */
    public static final int MAXSTONES = 10;

    /**
     * Captures the adjacencies of the board. Board coordinates are borrowed from
     * https://stackoverflow.com/questions/15405419/bitboard-representation-for-nine-men-morris-game
     * so they are as follows:
     * 
     * 0        1        2
     *    3     4     5
     *       6  7  8
     * 9  10 11    12 13 14
     *       15 16 17
     *    18    19    20
     * 21       22       23
     * 
     */
    private LinkedList<LinkedList<Integer>> adjacencies = null;

    /**
     * Captures the mills in the board. These are static mill coordinates, each represented
     * as an array. For instance, array {0, 1, 2} represents a mill, but {1, 3, 6} does not.
     */
    private LinkedList<LinkedList<Integer[]>> mills = null;

    /**
     * Reference to singleton instance.
     */
    private static LaskerMorrisBoard instance = null;

    /**
     * Static method for obtaining singleton.
     * @return a reference to a Lasker Morris Board. The object is created on-demand and
     * contains solely static information regarding the board.
     */
    public static LaskerMorrisBoard getInstance() {
        if (instance == null) {
            instance = new LaskerMorrisBoard();
        }
        return instance;
    }

    /**
     * Default constructor. Sets the adjacencies between coordinates and 
     * the sets of mill coordinates.
     */
    private LaskerMorrisBoard() {
        initializeAdjacencies();
        initializeMills();
    }



    /**
     * Initialization of static board adjacencies. Recall that the
     * board coordinates are as follows:
     * 
     * 0        1        2
     *    3     4     5
     *       6  7  8
     * 9  10 11    12 13 14
     *       15 16 17
     *    18    19    20
     * 21       22       23
     * 
     */
    private void initializeAdjacencies() {
        adjacencies = new LinkedList<LinkedList<Integer>>();
        for (int pos = 0; pos < POSITIONS; pos++) {
            adjacencies.add(new LinkedList<Integer>());
        }
        adjacencies.get(0).add(1);
        adjacencies.get(0).add(9);

        adjacencies.get(1).add(0);
        adjacencies.get(1).add(4);
        adjacencies.get(1).add(2);

        adjacencies.get(2).add(1);
        adjacencies.get(2).add(14);

        adjacencies.get(3).add(4);
        adjacencies.get(3).add(10);

        adjacencies.get(4).add(1);
        adjacencies.get(4).add(3);
        adjacencies.get(4).add(5);
        adjacencies.get(4).add(7);

        
        adjacencies.get(5).add(4);
        adjacencies.get(5).add(13);

        adjacencies.get(6).add(7);
        adjacencies.get(6).add(11);

        adjacencies.get(7).add(4);
        adjacencies.get(7).add(6);
        adjacencies.get(7).add(8);

        adjacencies.get(8).add(7);
        adjacencies.get(8).add(12);

        adjacencies.get(9).add(0);
        adjacencies.get(9).add(10);
        adjacencies.get(9).add(21);

        adjacencies.get(10).add(3);
        adjacencies.get(10).add(9);
        adjacencies.get(10).add(11);
        adjacencies.get(10).add(18);

        adjacencies.get(11).add(6);
        adjacencies.get(11).add(10);
        adjacencies.get(11).add(15);

        adjacencies.get(12).add(8);
        adjacencies.get(12).add(13);
        adjacencies.get(12).add(17);

        adjacencies.get(13).add(5);
        adjacencies.get(13).add(12);
        adjacencies.get(13).add(14);
        adjacencies.get(13).add(20);

        adjacencies.get(14).add(2);
        adjacencies.get(14).add(13);
        adjacencies.get(14).add(23);

        adjacencies.get(15).add(11);
        adjacencies.get(15).add(16);

        adjacencies.get(16).add(15);
        adjacencies.get(16).add(17);
        adjacencies.get(16).add(19);

        adjacencies.get(17).add(12);
        adjacencies.get(17).add(16);

        adjacencies.get(18).add(10);
        adjacencies.get(18).add(19);

        adjacencies.get(19).add(16);
        adjacencies.get(19).add(18);
        adjacencies.get(19).add(20);
        adjacencies.get(19).add(22);

        adjacencies.get(20).add(13);
        adjacencies.get(20).add(19);

        adjacencies.get(21).add(9);
        adjacencies.get(21).add(22);

        adjacencies.get(22).add(19);
        adjacencies.get(22).add(21);
        adjacencies.get(22).add(23);


        adjacencies.get(23).add(14);
        adjacencies.get(23).add(22);

    }

    /**
     * Initialization of static board mills. Recall that the board coordinates
     * are as follows:
     * 
     * 0        1        2
     *    3     4     5
     *       6  7  8
     * 9  10 11    12 13 14
     *       15 16 17
     *    18    19    20
     * 21       22       23
     */
    private void initializeMills() {
        mills = new LinkedList<LinkedList<Integer[]>>();
        for (int i = 0; i < POSITIONS; i++) {
            mills.add(new LinkedList<Integer[]>());
        }
        mills.get(0).add(new Integer[] {0,1,2});
        mills.get(0).add(new Integer[] {0,9,21});

        mills.get(1).add(new Integer[] {0,1,2});
        mills.get(1).add(new Integer[] {1,4,7});

        mills.get(2).add(new Integer[] {0,1,2});
        mills.get(2).add(new Integer[] {2,14,23});

        mills.get(3).add(new Integer[] {3,4,5});
        mills.get(3).add(new Integer[] {3,10,18});

        mills.get(4).add(new Integer[] {1,4,7});
        mills.get(4).add(new Integer[] {3,4,5});

        mills.get(5).add(new Integer[] {3,4,5});
        mills.get(5).add(new Integer[] {5,13,20});
        
        mills.get(6).add(new Integer[] {6,11,15});
        mills.get(6).add(new Integer[] {6,7,8});

        mills.get(7).add(new Integer[] {1,4,7});
        mills.get(7).add(new Integer[] {6,7,8});

        mills.get(8).add(new Integer[] {8,12,17});
        mills.get(8).add(new Integer[] {6,7,8});

        mills.get(9).add(new Integer[] {0,9,21});
        mills.get(9).add(new Integer[] {9,10,11});

        mills.get(10).add(new Integer[] {9,10,11});
        mills.get(10).add(new Integer[] {3,10,18});

        mills.get(11).add(new Integer[] {6,11,15});
        mills.get(11).add(new Integer[] {9,10,11});

        mills.get(12).add(new Integer[] {8,12,17});
        mills.get(12).add(new Integer[] {12,13,14});

        mills.get(13).add(new Integer[] {5,13,20});
        mills.get(13).add(new Integer[] {12,13,14});

        mills.get(14).add(new Integer[] {2,14,23});
        mills.get(14).add(new Integer[] {12,13,14});

        mills.get(15).add(new Integer[] {6,11,15});
        mills.get(15).add(new Integer[] {15,16,17});

        mills.get(16).add(new Integer[] {16,19,22});
        mills.get(16).add(new Integer[] {15,16,17});

        mills.get(17).add(new Integer[] {8,12,17});
        mills.get(17).add(new Integer[] {15,16,17});

        mills.get(18).add(new Integer[] {3,10,18});
        mills.get(18).add(new Integer[] {18,19,20});

        mills.get(19).add(new Integer[] {16,19,22});
        mills.get(19).add(new Integer[] {18,19,20});

        mills.get(20).add(new Integer[] {5,13,20});
        mills.get(20).add(new Integer[] {18,19,20});
        
        mills.get(21).add(new Integer[] {0,9,21});
        mills.get(21).add(new Integer[] {21,22,23});
        
        mills.get(22).add(new Integer[] {16,19,22});
        mills.get(22).add(new Integer[] {21,22,23});
        
        mills.get(23).add(new Integer[] {2,14,23});
        mills.get(23).add(new Integer[] {21,22,23});
    }

    /**
     * Returns the list of mills for a given position
     * @param position
     * @return
     */
    public List<Integer[]> getMills(int position) {
        return mills.get(position);
    }

    /**
     * Returns the list of coordinates adjacent to a given coordinate
     * @param i is the coordinate to query for adjacencies.
     * @return the list of coordinates adjacent to i.
     */
    public List<Integer> getAdjacencies(int i) {
        return adjacencies.get(i);
    }

}
