package org.tai.fpl.joiners;

import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.Test;
import org.tai.fpl.TestWriterHelper;
import org.tai.fpl.joiners.impl.GameweekJoiner;
import org.tai.fpl.writers.FileWriter;

import java.io.File;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class GameweekProviderJoinerTests extends TestWriterHelper {
    private static GameweekJoiner GAMEWEEK_JOINER;
    private static final String BASE_FILEPATH = "src/test/resources/gameweekJoiner/";
    private static final String MERGED_FILENAME = "merged_gw.csv";
    private static final String FULL_FILEPATH = String.format("%s%s/%s", BASE_FILEPATH, SEASON, MERGED_FILENAME);
    private static final FileWriter FILE_WRITER = new FileWriter(BASE_FILEPATH, "2022-23");
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
    public void joinGameweeks() {
        GAMEWEEK_JOINER = new GameweekJoiner(CURRENT_GAMEWEEK_NUMBER);
        GAMEWEEK_JOINER.join(FILE_WRITER, BASE_FILEPATH, MERGED_FILENAME);

        File playersRawFile = new File(FULL_FILEPATH);
        assertTrue(playersRawFile.exists());

        assertTrue(readDataFromFile(FULL_FILEPATH).similar(EXPECTED_VALID_JSON_ARRAY));
    }

    @Test
    public void joinGameweeksNoGwFiles() {
        GAMEWEEK_JOINER = new GameweekJoiner(CURRENT_GAMEWEEK_NUMBER);
        GAMEWEEK_JOINER.join(FILE_WRITER, "src/test/resources/", MERGED_FILENAME);

        File playersRawFile = new File(FULL_FILEPATH);
        assertFalse(playersRawFile.exists());
    }

    @Test(expected = IllegalArgumentException.class)
    public void invalidCurrentGameweekNumberZero() {
        GAMEWEEK_JOINER = new GameweekJoiner(0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void invalidCurrentGameweekNumberLessThanZero() {
        GAMEWEEK_JOINER = new GameweekJoiner(-1);
    }
}
