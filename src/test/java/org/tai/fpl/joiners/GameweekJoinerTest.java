package org.tai.fpl.joiners;

import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.Test;
import org.tai.fpl.TestWriterHelper;
import org.tai.fpl.joiners.impl.GameweekJoiner;
import org.tai.fpl.writers.FileWriter;

import java.io.File;

import static org.junit.Assert.assertTrue;

public class GameweekJoinerTest extends TestWriterHelper {
    private static GameweekJoiner GAMEWEEK_JOINER;
    private static final String JOINED_GW_FILEPATH = String.format("%sgws/%s", FULL_FILEPATH, JOINED_GW_FILENAME);
    private static final FileWriter FILE_WRITER = new FileWriter(BASE_FILEPATH);
    private static final int CURRENT_GAMEWEEK_NUMBER = 2;

    private static final JSONObject VALID_JSON_OBJECT_1 = new JSONObject()
            .put("test1", "value1")
            .put("test2", "value2")
            .put("test3", "value3");
    private static final JSONObject VALID_JSON_OBJECT_2 = new JSONObject()
            .put("test1", "value4")
            .put("test2", "value5")
            .put("test3", "value6");

    private static final JSONArray EXPECTED_VALID_JSON_ARRAY = new JSONArray()
            .put(VALID_JSON_OBJECT_1)
            .put(VALID_JSON_OBJECT_2);

    @Test
    public void givenValidGwAndSeason_join_thenWriteToFile() {
        GAMEWEEK_JOINER = new GameweekJoiner(CURRENT_GAMEWEEK_NUMBER, SEASON, FILE_WRITER);
        GAMEWEEK_JOINER.join();

        File joinedGws = new File(JOINED_GW_FILEPATH);

        assertTrue(joinedGws.exists());
        assertTrue(readDataFromFile(JOINED_GW_FILEPATH).similar(EXPECTED_VALID_JSON_ARRAY));
    }

    @Test(expected = RuntimeException.class)
    public void givenNonExistentSeason_join_thenThrowRuntimeException() {
        GAMEWEEK_JOINER = new GameweekJoiner(CURRENT_GAMEWEEK_NUMBER, "2022-21", FILE_WRITER);
        GAMEWEEK_JOINER.join();
    }

    @Test(expected = RuntimeException.class)
    public void givenNonExistentGw_join_thenThrowRuntimeException() {
        GAMEWEEK_JOINER = new GameweekJoiner(3, "2022-23", FILE_WRITER);
        GAMEWEEK_JOINER.join();
    }
}
