package model;

import static org.junit.Assert.*;

import org.junit.Test;

/**
 * Sample tests for class LaskerMorrisGameState.
 * 
 * @author aguirre
 *
 */
public class LaskerMorrisGameStateTest {

    /**
     * Checks that toString on default state is non-null
     */
    @Test
    public void testToString() {
        LaskerMorrisGameState state = new LaskerMorrisGameState();
        assertNotNull(state.toString());
    }

}
