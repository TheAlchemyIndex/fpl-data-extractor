package org.tai.fpl.config;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class FplConfigTests {

    private static final FplConfig CONFIG = new FplConfig();
    private static final String EXPECTED_FULL_SEASON = "2022-23";
    private static final String EXPECTED_BASE_FILEPATH = "data/2022-23/";

    @Test
    public void validConfig() {
        assertEquals(EXPECTED_FULL_SEASON, CONFIG.getFullSeason());
        assertEquals(EXPECTED_BASE_FILEPATH, CONFIG.getBaseFilePath());
    }
}
