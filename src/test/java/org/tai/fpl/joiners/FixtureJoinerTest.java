package org.tai.fpl.joiners;

import org.junit.Test;
import org.tai.fpl.TestHelper;
import org.tai.fpl.joiners.impl.FixtureJoiner;

import java.io.File;

import static org.junit.Assert.assertTrue;

public class FixtureJoinerTest extends TestHelper {
    private static FixtureJoiner FIXTURE_JOINER;
    private static final String JOINED_FIXTURE_FILEPATH = String.format("%s%s", BASE_FILEPATH, JOINED_FIXTURE_FILENAME);

    @Test
    public void givenValidSeason_join_thenWriteToFile() {
        FIXTURE_JOINER = new FixtureJoiner(VALID_STARTING_SEASON_START, VALID_STARTING_SEASON_END, VALID_ENDING_SEASON_END,
                FILE_WRITER);
        FIXTURE_JOINER.join();

        File joinedFixtures = new File(JOINED_FIXTURE_FILEPATH);
        assertTrue(joinedFixtures.exists());

        assertTrue(readDataFromFile(JOINED_FIXTURE_FILEPATH).similar(EXPECTED_VALID_JSON_ARRAY));
    }

    @Test(expected = RuntimeException.class)
    public void givenNonExistentSeason_join_thenThrowRuntimeException() {
        FIXTURE_JOINER = new FixtureJoiner(2019, 20, 21, FILE_WRITER);
        FIXTURE_JOINER.join();
    }
}
