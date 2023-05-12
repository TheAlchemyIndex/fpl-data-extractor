package org.tai.fpl.gameweek;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.tai.fpl.gameweek.GameweekNameFormatter.formatName;

public class GameweekNameFormatterTest {

    private static final String TEST_NAME1 = "Adama Traoré Diarra";
    private static final String EXPECTED_FORMATTED_NAME1 = "Adama Traoré";

    private static final String TEST_NAME2 = "Juan Camilo Hernández Suárez";
    private static final String EXPECTED_FORMATTED_NAME2 = "Juan Camilo Hernández";

    private static final String TEST_NAME3 = "Willian Borges da Silva";
    private static final String EXPECTED_FORMATTED_NAME3 = "Willian";

    /* Basic temporary tests to test functionality */
    @Test
    public void formatNameTest1() {
        assertEquals(EXPECTED_FORMATTED_NAME1, formatName(TEST_NAME1));
    }

    @Test
    public void formatNameTest2() {
        assertEquals(EXPECTED_FORMATTED_NAME2, formatName(TEST_NAME2));
    }

    @Test
    public void formatNameTest3() {
        assertEquals(EXPECTED_FORMATTED_NAME3, formatName(TEST_NAME3));
    }
}
