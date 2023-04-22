package org.tai.fpl.extractors;

import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.Test;
import org.tai.fpl.TestWriterHelper;
import org.tai.fpl.writers.FileWriter;

import java.io.File;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class GameweekExtractorTests extends TestWriterHelper {
    private static GameweekExtractor GAMEWEEK_EXTRACTOR;
    private static final FileWriter FILE_WRITER = new FileWriter(BASE_FILEPATH, SEASON);

    private static final JSONObject VALID_EVENT_JSON_OBJECT_1 = new JSONObject()
            .put("is_current", "false")
            .put("id", "1");
    private static final JSONObject VALID_EVENT_JSON_OBJECT_2 = new JSONObject()
            .put("is_current", "true")
            .put("id", "2");

    private static final JSONArray VALID_EVENT_JSON_ARRAY = new JSONArray()
            .put(VALID_EVENT_JSON_OBJECT_1)
            .put(VALID_EVENT_JSON_OBJECT_2);

    private static final JSONObject VALID_ELEMENT_JSON_OBJECT_1 = new JSONObject()
            .put("first_name", "first_name1")
            .put("second_name", "second_name1")
            .put("id", "1")
            .put("element_type", "1")
            .put("team", "1")
            .put("ep_this", "1")
            .put("test1", "value1")
            .put("test2", "value1")
            .put("test3", "value1");
    private static final JSONObject VALID_ELEMENT_JSON_OBJECT_2 = new JSONObject()
            .put("first_name", "first_name2")
            .put("second_name", "second_name2")
            .put("id", "2")
            .put("element_type", "2")
            .put("team", "2")
            .put("ep_this", "2")
            .put("test1", "value2")
            .put("test2", "value2")
            .put("test3", "value2");

    private static final JSONArray VALID_ELEMENT_JSON_ARRAY = new JSONArray()
            .put(VALID_ELEMENT_JSON_OBJECT_1)
            .put(VALID_ELEMENT_JSON_OBJECT_2);

    private static final JSONObject VALID_TEAM_JSON_OBJECT_1 = new JSONObject()
            .put("id", "1")
            .put("name", "team1")
            .put("test1", "value1")
            .put("test2", "value2")
            .put("test3", "value3");
    private static final JSONObject VALID_TEAM_JSON_OBJECT_2 = new JSONObject()
            .put("id", "2")
            .put("name", "team2")
            .put("test1", "value4")
            .put("test2", "value5")
            .put("test3", "value6");

    private static final JSONArray VALID_TEAM_JSON_ARRAY = new JSONArray()
            .put(VALID_TEAM_JSON_OBJECT_1)
            .put(VALID_TEAM_JSON_OBJECT_2);

    private static final JSONObject VALID_MAIN_DATA_JSON_OBJECT = new JSONObject()
            .put("events", VALID_EVENT_JSON_ARRAY)
            .put("elements", VALID_ELEMENT_JSON_ARRAY)
            .put("teams", VALID_TEAM_JSON_ARRAY);

    private static final JSONObject INVALID_MAIN_DATA_JSON_OBJECT = new JSONObject()
            .put("test1", "value1")
            .put("test2", "value2")
            .put("test3", "value3");

    private static final int EXPECTED_CURRENT_GAMEWEEK_NUMBER = 2;

    @Test
    public void getCurrentGameweekNumberValidJsonData() {
        GAMEWEEK_EXTRACTOR = new GameweekExtractor(VALID_MAIN_DATA_JSON_OBJECT);
        int currentGameweekNumber = GAMEWEEK_EXTRACTOR.getCurrentGameweekNumber();

        assertEquals(EXPECTED_CURRENT_GAMEWEEK_NUMBER, currentGameweekNumber);
    }

    @Test
    public void getGameweekDataValidJsonDataPlayerIds() {
        JSONObject validPlayerJsonObject1 = new JSONObject()
                .put("first_name", "first_name1")
                .put("second_name", "second_name1")
                .put("id", "1")
                .put("element_type", "1")
                .put("team", "1")
                .put("ep_this", "1");
        JSONObject validPlayerJsonObject2 = new JSONObject()
                .put("first_name", "first_name2")
                .put("second_name", "second_name2")
                .put("id", "2")
                .put("element_type", "2")
                .put("team", "2")
                .put("ep_this", "2");

        JSONArray expectedValidPlayersJsonArray = new JSONArray()
                .put(validPlayerJsonObject1)
                .put(validPlayerJsonObject2);

        GAMEWEEK_EXTRACTOR = new GameweekExtractor(VALID_MAIN_DATA_JSON_OBJECT);
        GAMEWEEK_EXTRACTOR.getGameweekData(FILE_WRITER);

        File playerIdsFile = new File("src/test/data/2022-23/player_idlist.csv");
        assertTrue(playerIdsFile.exists());

        assertTrue(readDataFromFile(String.format("%s%s", FULL_FILEPATH, "player_idlist.csv")).similar(expectedValidPlayersJsonArray));
    }

    @Test
    public void getGameweekDataValidJsonDataPlayersRaw() {
        GAMEWEEK_EXTRACTOR = new GameweekExtractor(VALID_MAIN_DATA_JSON_OBJECT);
        GAMEWEEK_EXTRACTOR.getGameweekData(FILE_WRITER);

        File playersRawFile = new File("src/test/data/2022-23/players_raw.csv");
        assertTrue(playersRawFile.exists());

        assertTrue(readDataFromFile(String.format("%s%s", FULL_FILEPATH, "players_raw.csv")).similar(VALID_ELEMENT_JSON_ARRAY));
    }

    @Test
    public void getGameweekDataValidJsonDataTeams() {
        GAMEWEEK_EXTRACTOR = new GameweekExtractor(VALID_MAIN_DATA_JSON_OBJECT);
        GAMEWEEK_EXTRACTOR.getGameweekData(FILE_WRITER);

        File teamsFile = new File("src/test/data/2022-23/teams.csv");
        assertTrue(teamsFile.exists());

        assertTrue(readDataFromFile(String.format("%s%s", FULL_FILEPATH, "teams.csv")).similar(VALID_TEAM_JSON_ARRAY));
    }

    @Test
    public void getGameweekDataInvalidJsonData() {
        GAMEWEEK_EXTRACTOR = new GameweekExtractor(INVALID_MAIN_DATA_JSON_OBJECT);
        GAMEWEEK_EXTRACTOR.getGameweekData(FILE_WRITER);
    }

    @Test(expected = IllegalArgumentException.class)
    public void getGameweekDataNullFileWriter() {
        GAMEWEEK_EXTRACTOR = new GameweekExtractor(VALID_MAIN_DATA_JSON_OBJECT);
        GAMEWEEK_EXTRACTOR.getGameweekData(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void constructorNullJsonData() {
        GAMEWEEK_EXTRACTOR = new GameweekExtractor(null);
    }
}
