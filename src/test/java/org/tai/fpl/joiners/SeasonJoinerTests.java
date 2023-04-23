package org.tai.fpl.joiners;

import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.Test;
import org.tai.fpl.TestWriterHelper;
import org.tai.fpl.writers.FileWriter;

import java.io.File;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class SeasonJoinerTests extends TestWriterHelper {
    private static final int VALID_STARTING_SEASON_START = 2021;
    private static final int VALID_STARTING_SEASON_END = 2022;
    private static final int VALID_ENDING_SEASON_END = 2023;
    private static SeasonJoiner SEASON_JOINER;
    private static final String BASE_FILEPATH = "src/test/resources/seasonJoiner/";
    private static final String SEASON_FILEPATH = String.format("%s-%s seasons.csv", VALID_STARTING_SEASON_START, VALID_ENDING_SEASON_END);
    private static final String FULL_FILEPATH = String.format("%s/%s", BASE_FILEPATH, SEASON_FILEPATH);
    private static final FileWriter FILE_WRITER = new FileWriter(BASE_FILEPATH, "2022-23");

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
    public void joinSeasons() {
        SEASON_JOINER = new SeasonJoiner(VALID_STARTING_SEASON_START, VALID_STARTING_SEASON_END, VALID_ENDING_SEASON_END);
        SEASON_JOINER.joinSeasons(FILE_WRITER, BASE_FILEPATH, SEASON_FILEPATH);

        File playersRawFile = new File(FULL_FILEPATH);
        assertTrue(playersRawFile.exists());

        assertTrue(readDataFromFile(FULL_FILEPATH).similar(EXPECTED_VALID_JSON_ARRAY));
    }

    @Test
    public void joinSeasonsNoGwFiles() {
        SEASON_JOINER = new SeasonJoiner(VALID_STARTING_SEASON_START, VALID_STARTING_SEASON_END, VALID_ENDING_SEASON_END);
        SEASON_JOINER.joinSeasons(FILE_WRITER, "src/test/resources/", SEASON_FILEPATH);

        File playersRawFile = new File(FULL_FILEPATH);
        assertFalse(playersRawFile.exists());
    }

    @Test(expected = IllegalArgumentException.class)
    public void invalidStartingSeasonStartLessThan2016() {
        SEASON_JOINER = new SeasonJoiner(2015, 2016, VALID_ENDING_SEASON_END);
    }

    @Test(expected = IllegalArgumentException.class)
    public void invalidStartingSeasonStartLessThan0() {
        SEASON_JOINER = new SeasonJoiner(-1, 0, VALID_ENDING_SEASON_END);
    }

    @Test(expected = IllegalArgumentException.class)
    public void invalidStartingSeasonEndLessThanStartingSeasonStart() {
        SEASON_JOINER = new SeasonJoiner(2016, 2015, VALID_ENDING_SEASON_END);
    }

    @Test(expected = IllegalArgumentException.class)
    public void invalidStartingSeasonEndEqualToStartingSeasonStart() {
        SEASON_JOINER = new SeasonJoiner(2016, 2016, VALID_ENDING_SEASON_END);
    }

    @Test(expected = IllegalArgumentException.class)
    public void invalidStartingSeasonEndGreaterThan1YearHigherThanStartingSeasonStart() {
        SEASON_JOINER = new SeasonJoiner(2016, 2018, VALID_ENDING_SEASON_END);
    }

    @Test(expected = IllegalArgumentException.class)
    public void invalidEndingSeasonEndLessThanStartingSeasonStart() {
        SEASON_JOINER = new SeasonJoiner(2016, 2017, 2015);
    }

    @Test(expected = IllegalArgumentException.class)
    public void invalidEndingSeasonEndEqualToStartingSeasonStart() {
        SEASON_JOINER = new SeasonJoiner(2016, 2017, 2016);
    }
}
