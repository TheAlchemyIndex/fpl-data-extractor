package org.tai.fpl.providers;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Test;
import org.tai.fpl.providers.impl.ElementProvider;

import static org.junit.Assert.assertTrue;

public class ElementProviderTests {

    private static ElementProvider ELEMENT_PROVIDER;

    private static final JSONObject VALID_JSON_OBJECT_1 = new JSONObject()
            .put("first_name", "first_name1")
            .put("second_name", "second_name1")
            .put("id", "1")
            .put("element_type", "1")
            .put("team", "1")
            .put("ep_this", "1")
            .put("test1", "value1")
            .put("test2", "value1")
            .put("test3", "value1");
    private static final JSONObject VALID_JSON_OBJECT_2 = new JSONObject()
            .put("first_name", "first_name2")
            .put("second_name", "second_name2")
            .put("id", "2")
            .put("element_type", "2")
            .put("team", "2")
            .put("ep_this", "2")
            .put("test1", "value2")
            .put("test2", "value2")
            .put("test3", "value2");

    private static final JSONArray VALID_JSON_ARRAY = new JSONArray()
            .put(VALID_JSON_OBJECT_1)
            .put(VALID_JSON_OBJECT_2);

    private static final JSONObject VALID_PLAYER_JSON_OBJECT_1 = new JSONObject()
            .put("first_name", "first_name1")
            .put("second_name", "second_name1")
            .put("id", "1")
            .put("element_type", "1")
            .put("team", "1")
            .put("ep_this", "1");
    private static final JSONObject VALID_PLAYER_JSON_OBJECT_2 = new JSONObject()
            .put("first_name", "first_name2")
            .put("second_name", "second_name2")
            .put("id", "2")
            .put("element_type", "2")
            .put("team", "2")
            .put("ep_this", "2");

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
                .put("first_name", "first_name1")
                .put("second_name", "second_name1")
                .put("id", "1")
                .put("element_type", "1")
                .put("team", "1")
                .put("ep_this", "1")
                .put("test1", "value1")
                .put("test2", "value1")
                .put("test3", "value1");
        JSONObject jsonObject2 = new JSONObject()
                .put("first_name", "first_name2")
                .put("second_name", "second_name2")
                .put("id", "2")
                .put("element_type", "2")
                .put("team", "2")
                .put("ep_this", "2")
                .put("test1", "value2")
                .put("test2", "value2")
                .put("test3", "value2");

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
