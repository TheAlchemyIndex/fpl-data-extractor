package org.tai.fpl.joiners;

import org.junit.Test;
import org.tai.fpl.TestHelper;
import org.tai.fpl.joiners.impl.SeasonJoiner;

import java.io.File;

import static org.junit.Assert.assertTrue;

public class SeasonJoinerTest extends TestHelper {
    private static SeasonJoiner SEASON_JOINER;
    private static final String JOINED_SEASON_FILEPATH = String.format("%s%s", BASE_FILEPATH, JOINED_SEASON_FILENAME);

    @Test
    public void givenValidSeason_join_thenWriteToFile() {
        SEASON_JOINER = new SeasonJoiner(VALID_STARTING_SEASON_START, VALID_STARTING_SEASON_END, VALID_ENDING_SEASON_END,
                FILE_WRITER);
        SEASON_JOINER.join();

        File joinedSeasons = new File(JOINED_SEASON_FILEPATH);
        assertTrue(joinedSeasons.exists());

        assertTrue(readDataFromFile(JOINED_SEASON_FILEPATH).similar(EXPECTED_VALID_JSON_ARRAY));
    }

    @Test(expected = RuntimeException.class)
    public void givenNonExistentSeason_join_thenThrowRuntimeException() {
        SEASON_JOINER = new SeasonJoiner(2019, 20, 21, FILE_WRITER);
        SEASON_JOINER.join();
    }
}
