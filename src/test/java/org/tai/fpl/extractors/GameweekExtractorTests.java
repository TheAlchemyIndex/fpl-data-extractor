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

    private static final String FIRST_NAME_COL = "first_name";
    private static final String SECOND_NAME_COL = "second_name";
    private static final String WEB_NAME_COL = "web_name";
    private static final String NAME_COL = "name";
    private static final String ID_COL = "id";
    private static final String ELEMENT_TYPE_COL = "element_type";
    private static final String TEAM_COL = "team";
    private static final String EP_THIS_COL = "ep_this";
    private static final String TEST_COL1 = "test1";
    private static final String TEST_COL2 = "test2";
    private static final String TEST_COL3 = "test3";
    private static final String IS_CURRENT_COL = "is_current";
    private static final String SEASON_COL = "season";

    private static GameweekExtractor GAMEWEEK_EXTRACTOR;
    private static final FileWriter FILE_WRITER = new FileWriter(BASE_FILEPATH, SEASON);

    private static final JSONObject VALID_EVENT_JSON_OBJECT_1 = new JSONObject()
            .put(IS_CURRENT_COL, "false")
            .put(ID_COL, "1");
    private static final JSONObject VALID_EVENT_JSON_OBJECT_2 = new JSONObject()
            .put(IS_CURRENT_COL, "true")
            .put(ID_COL, "2");

    private static final JSONArray VALID_EVENT_JSON_ARRAY = new JSONArray()
            .put(VALID_EVENT_JSON_OBJECT_1)
            .put(VALID_EVENT_JSON_OBJECT_2);

    private static final JSONObject VALID_ELEMENT_JSON_OBJECT_1 = new JSONObject()
            .put(FIRST_NAME_COL, "first_name1")
            .put(SECOND_NAME_COL, "second_name1")
            .put(WEB_NAME_COL, "second_name1")
            .put(ID_COL, "1")
            .put(ELEMENT_TYPE_COL, "1")
            .put(TEAM_COL, "1")
            .put(EP_THIS_COL, "1")
            .put(TEST_COL1, "value1")
            .put(TEST_COL2, "value2")
            .put(TEST_COL3, "value3");
    private static final JSONObject VALID_ELEMENT_JSON_OBJECT_2 = new JSONObject()
            .put(FIRST_NAME_COL, "first_name2")
            .put(SECOND_NAME_COL, "second_name2")
            .put(WEB_NAME_COL, "second_name2")
            .put(ID_COL, "2")
            .put(ELEMENT_TYPE_COL, "2")
            .put(TEAM_COL, "2")
            .put(EP_THIS_COL, "2")
            .put(TEST_COL1, "value4")
            .put(TEST_COL2, "value5")
            .put(TEST_COL3, "value6");

    private static final JSONArray VALID_ELEMENT_JSON_ARRAY = new JSONArray()
            .put(VALID_ELEMENT_JSON_OBJECT_1)
            .put(VALID_ELEMENT_JSON_OBJECT_2);

    private static final JSONObject VALID_TEAM_JSON_OBJECT_1 = new JSONObject()
            .put(ID_COL, "1")
            .put(NAME_COL, "team1")
            .put(TEST_COL1, "value1")
            .put(TEST_COL2, "value2")
            .put(TEST_COL3, "value3");
    private static final JSONObject VALID_TEAM_JSON_OBJECT_2 = new JSONObject()
            .put(ID_COL, "2")
            .put(NAME_COL, "team2")
            .put(TEST_COL1, "value4")
            .put(TEST_COL2, "value5")
            .put(TEST_COL3, "value6");

    private static final JSONArray VALID_TEAM_JSON_ARRAY = new JSONArray()
            .put(VALID_TEAM_JSON_OBJECT_1)
            .put(VALID_TEAM_JSON_OBJECT_2);

    private static final JSONObject VALID_MAIN_DATA_JSON_OBJECT = new JSONObject()
            .put("events", VALID_EVENT_JSON_ARRAY)
            .put("elements", VALID_ELEMENT_JSON_ARRAY)
            .put("teams", VALID_TEAM_JSON_ARRAY);

    private static final JSONObject INVALID_MAIN_DATA_JSON_OBJECT = new JSONObject()
            .put(TEST_COL1, "value1")
            .put(TEST_COL2, "value2")
            .put(TEST_COL3, "value3");

    @Test
    public void getCurrentGameweekNumberValidJsonData() {
        GAMEWEEK_EXTRACTOR = new GameweekExtractor(VALID_MAIN_DATA_JSON_OBJECT, SEASON_COL);
        int currentGameweekNumber = GAMEWEEK_EXTRACTOR.getCurrentGameweekNumber();

        assertEquals(2, currentGameweekNumber);
    }

    @Test
    public void getGameweekDataValidJsonDataPlayerIds() {
        JSONObject validPlayerJsonObject1 = new JSONObject()
                .put(FIRST_NAME_COL, "first_name1")
                .put(SECOND_NAME_COL, "second_name1")
                .put(WEB_NAME_COL, "second_name1")
                .put(ID_COL, "1")
                .put(ELEMENT_TYPE_COL, "1")
                .put(TEAM_COL, "1")
                .put(EP_THIS_COL, "1");
        JSONObject validPlayerJsonObject2 = new JSONObject()
                .put(FIRST_NAME_COL, "first_name2")
                .put(SECOND_NAME_COL, "second_name2")
                .put(WEB_NAME_COL, "second_name2")
                .put(ID_COL, "2")
                .put(ELEMENT_TYPE_COL, "2")
                .put(TEAM_COL, "2")
                .put(EP_THIS_COL, "2");

        JSONArray expectedValidPlayersJsonArray = new JSONArray()
                .put(validPlayerJsonObject1)
                .put(validPlayerJsonObject2);

        GAMEWEEK_EXTRACTOR = new GameweekExtractor(VALID_MAIN_DATA_JSON_OBJECT, SEASON_COL);
        GAMEWEEK_EXTRACTOR.getGameweekData(FILE_WRITER);

        File playerIdsFile = new File("src/test/resources/2022-23/player_idlist.csv");
        assertTrue(playerIdsFile.exists());

        assertTrue(readDataFromFile(String.format("%s%s", FULL_FILEPATH, "player_idlist.csv")).similar(expectedValidPlayersJsonArray));
    }

    @Test
    public void getGameweekDataValidJsonDataPlayersRaw() {
        GAMEWEEK_EXTRACTOR = new GameweekExtractor(VALID_MAIN_DATA_JSON_OBJECT, SEASON_COL);
        GAMEWEEK_EXTRACTOR.getGameweekData(FILE_WRITER);

        File playersRawFile = new File(String.format("%s%s", FULL_FILEPATH, "players_raw.csv"));
        assertTrue(playersRawFile.exists());

        assertTrue(readDataFromFile(String.format("%s%s", FULL_FILEPATH, "players_raw.csv")).similar(VALID_ELEMENT_JSON_ARRAY));
    }

    @Test
    public void getGameweekDataValidJsonDataTeams() {
        GAMEWEEK_EXTRACTOR = new GameweekExtractor(VALID_MAIN_DATA_JSON_OBJECT, SEASON_COL);
        GAMEWEEK_EXTRACTOR.getGameweekData(FILE_WRITER);

        File teamsFile = new File(String.format("%s%s", FULL_FILEPATH, "teams.csv"));
        assertTrue(teamsFile.exists());

        assertTrue(readDataFromFile(String.format("%s%s", FULL_FILEPATH, "teams.csv")).similar(VALID_TEAM_JSON_ARRAY));
    }

    @Test
    public void getGameweekDataInvalidJsonData() {
        GAMEWEEK_EXTRACTOR = new GameweekExtractor(INVALID_MAIN_DATA_JSON_OBJECT, SEASON_COL);
        GAMEWEEK_EXTRACTOR.getGameweekData(FILE_WRITER);
    }

    @Test(expected = IllegalArgumentException.class)
    public void getGameweekDataNullFileWriter() {
        GAMEWEEK_EXTRACTOR = new GameweekExtractor(VALID_MAIN_DATA_JSON_OBJECT, SEASON_COL);
        GAMEWEEK_EXTRACTOR.getGameweekData(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void constructorNullJsonData() {
        GAMEWEEK_EXTRACTOR = new GameweekExtractor(null, SEASON_COL);
    }
}
