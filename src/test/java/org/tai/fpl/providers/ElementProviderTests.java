package org.tai.fpl.providers;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Test;
import org.tai.fpl.providers.impl.ElementProvider;

import static org.junit.Assert.assertTrue;

public class ElementProviderTests {

    private static final String FIRST_NAME_COL = "first_name";
    private static final String SECOND_NAME_COL = "second_name";
    private static final String WEB_NAME_COL = "web_name";
    private static final String ID_COL = "id";
    private static final String ELEMENT_TYPE_COL = "element_type";
    private static final String TEAM_COL = "team";
    private static final String EP_THIS_COL = "ep_this";
    private static final String TEST_COL1 = "test1";
    private static final String TEST_COL2 = "test2";
    private static final String TEST_COL3 = "test3";

    private static ElementProvider ELEMENT_PROVIDER;

    private static final JSONObject VALID_JSON_OBJECT_1 = new JSONObject()
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
    private static final JSONObject VALID_JSON_OBJECT_2 = new JSONObject()
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

    private static final JSONArray VALID_JSON_ARRAY = new JSONArray()
            .put(VALID_JSON_OBJECT_1)
            .put(VALID_JSON_OBJECT_2);

    private static final JSONObject VALID_PLAYER_JSON_OBJECT_1 = new JSONObject()
            .put(FIRST_NAME_COL, "first_name1")
            .put(SECOND_NAME_COL, "second_name1")
            .put(WEB_NAME_COL, "second_name1")
            .put(ID_COL, "1")
            .put(ELEMENT_TYPE_COL, "1")
            .put(TEAM_COL, "1")
            .put(EP_THIS_COL, "1");
    private static final JSONObject VALID_PLAYER_JSON_OBJECT_2 = new JSONObject()
            .put(FIRST_NAME_COL, "first_name2")
            .put(SECOND_NAME_COL, "second_name2")
            .put(WEB_NAME_COL, "second_name2")
            .put(ID_COL, "2")
            .put(ELEMENT_TYPE_COL, "2")
            .put(TEAM_COL, "2")
            .put(EP_THIS_COL, "2");

    private static final String INVALID_JSON_STRING = "test1:value1,test2:value2";

    private static final JSONArray EXPECTED_VALID_PLAYERS_JSON_ARRAY = new JSONArray()
            .put(VALID_PLAYER_JSON_OBJECT_1)
            .put(VALID_PLAYER_JSON_OBJECT_2);

    private static final JSONArray EXPECTED_EMPTY_JSON_ARRAY = new JSONArray();

    @Test
    public void getDataValidJsonArray() {
        ELEMENT_PROVIDER = new ElementProvider(VALID_JSON_ARRAY);
        JSONArray dataArray = ELEMENT_PROVIDER.getData();

        JSONObject jsonObject1 = new JSONObject()
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
        JSONObject jsonObject2 = new JSONObject()
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

        JSONArray expectedJsonArray = new JSONArray()
                .put(jsonObject1)
                .put(jsonObject2);

        assertTrue(expectedJsonArray.similar(dataArray));
    }

    @Test
    public void getPlayersValidJsonArray() {
        ELEMENT_PROVIDER = new ElementProvider(VALID_JSON_ARRAY);
        JSONArray playersArray = ELEMENT_PROVIDER.getPlayers();

        assertTrue(EXPECTED_VALID_PLAYERS_JSON_ARRAY.similar(playersArray));
    }

    @Test
    public void getDataEmptyJsonArray() {
        ELEMENT_PROVIDER = new ElementProvider(new JSONArray());
        JSONArray dataArray = ELEMENT_PROVIDER.getData();

        assertTrue(EXPECTED_EMPTY_JSON_ARRAY.similar(dataArray));
    }

    @Test
    public void getPlayersEmptyJsonArray() {
        ELEMENT_PROVIDER = new ElementProvider(new JSONArray());
        JSONArray playersArray = ELEMENT_PROVIDER.getPlayers();

        assertTrue(EXPECTED_EMPTY_JSON_ARRAY.similar(playersArray));
    }

    @Test(expected = IllegalArgumentException.class)
    public void nullJsonArray() {
        new ElementProvider(null);
    }

    @Test(expected = JSONException.class)
    public void invalidJsonArray() {
        new ElementProvider(new JSONArray(INVALID_JSON_STRING));
    }
}
