package org.tai.fpl.joiners;

import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.Test;
import org.tai.fpl.TestWriterHelper;
import org.tai.fpl.joiners.impl.SeasonJoiner;
import org.tai.fpl.writers.FileWriter;

import java.io.File;

import static org.junit.Assert.assertTrue;

public class SeasonJoinerTests extends TestWriterHelper {
    protected static final int VALID_STARTING_SEASON_START = 2020;
    protected static final int VALID_STARTING_SEASON_END = 21;
    protected static final int VALID_ENDING_SEASON_END = 22;
    private static SeasonJoiner SEASON_JOINER;
    private static final String JOINED_SEASON_FILEPATH = String.format("%s%s", BASE_FILEPATH, JOINED_SEASON_FILENAME);
    private static final FileWriter FILE_WRITER = new FileWriter(BASE_FILEPATH);

    private static final JSONObject VALID_JSON_OBJECT_1 = new JSONObject()
            .put("test1", "value1")
            .put("test2", "value2")
            .put("test3", "value3");
    private static final JSONObject VALID_JSON_OBJECT_2 = new JSONObject()
            .put("test1", "value4")
            .put("test2", "value5")
            .put("test3", "value6");
    private static final JSONObject VALID_JSON_OBJECT_3 = new JSONObject()
            .put("test1", "value7")
            .put("test2", "value8")
            .put("test3", "value9");
    private static final JSONObject VALID_JSON_OBJECT_4 = new JSONObject()
            .put("test1", "value10")
            .put("test2", "value11")
            .put("test3", "value12");

    private static final JSONArray EXPECTED_VALID_JSON_ARRAY = new JSONArray()
            .put(VALID_JSON_OBJECT_1)
            .put(VALID_JSON_OBJECT_2)
            .put(VALID_JSON_OBJECT_3)
            .put(VALID_JSON_OBJECT_4);

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
