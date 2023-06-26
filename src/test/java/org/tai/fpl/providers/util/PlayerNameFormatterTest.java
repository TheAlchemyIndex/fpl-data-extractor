package org.tai.fpl.providers.util;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.tai.fpl.providers.util.formatters.PlayerNameFormatter.formatName;

public class PlayerNameFormatterTest {

    private static final String TEST_NAME1 = "Adama Traoré Diarra";
    private static final String EXPECTED_FORMATTED_NAME1 = "Adama Traoré";

    private static final String TEST_NAME2 = "Juan Camilo Hernández Suárez";
    private static final String EXPECTED_FORMATTED_NAME2 = "Juan Camilo Hernández";

    private static final String TEST_NAME3 = "Willian Borges da Silva";
    private static final String EXPECTED_FORMATTED_NAME3 = "Willian";

    private static final String TEST_NAME4 = "George Best";

    /* Basic tests to test functionality */
    @Test
    public void givenValidName_formatName_thenReturnFormattedName_1() {
        assertEquals(EXPECTED_FORMATTED_NAME1, formatName(TEST_NAME1));
    }

    @Test
    public void givenValidName_formatName_thenReturnFormattedName_2() {
        assertEquals(EXPECTED_FORMATTED_NAME2, formatName(TEST_NAME2));
    }

    @Test
    public void givenValidName_formatName_thenReturnFormattedName_3() {
        assertEquals(EXPECTED_FORMATTED_NAME3, formatName(TEST_NAME3));
    }

    @Test
    public void givenValidNameNotInSwitchStatement_formatName_thenReturnSameName() {
        assertEquals(TEST_NAME4, formatName(TEST_NAME4));
    }
}
