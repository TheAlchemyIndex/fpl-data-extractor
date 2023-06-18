package org.tai.fpl.providers.impl;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

public class PlayerProviderTest {
    private static PlayerProvider PLAYER_PROVIDER;

    private static final JSONObject VALID_JSON_OBJECT_1 = new JSONObject()
            .put("first_name", "name1")
            .put("second_name", "surname1")
            .put("web_name", "webname1")
            .put("id", "id1")
            .put("element_type", "element1")
            .put("team", "team1")
            .put("ep_this", "ep1")
            .put("test1", "value1")
            .put("test2", "value2")
            .put("test3", "value3");
    private static final JSONObject VALID_JSON_OBJECT_2 = new JSONObject()
            .put("first_name", "name2")
            .put("second_name", "surname2")
            .put("web_name", "webname2")
            .put("id", "id2")
            .put("element_type", "element2")
            .put("team", "team2")
            .put("ep_this", "ep2")
            .put("test1", "value4")
            .put("test2", "value5")
            .put("test3", "value6");

    private static final JSONArray VALID_JSON_ARRAY = new JSONArray()
            .put(VALID_JSON_OBJECT_1)
            .put(VALID_JSON_OBJECT_2);

    private static final String INVALID_JSON_STRING = "test1:value1,test2:value2";
    private static final JSONArray EXPECTED_EMPTY_JSON_ARRAY = new JSONArray();

    @Test
    public void givenValidJsonArray_getData_thenReturnValidJsonArray() {
        PLAYER_PROVIDER = new PlayerProvider(VALID_JSON_ARRAY);
        JSONArray dataArray = PLAYER_PROVIDER.getData();

        JSONObject jsonObject1 = new JSONObject()
                .put("first_name", "name1")
                .put("second_name", "surname1")
                .put("web_name", "webname1")
                .put("id", "id1")
                .put("element_type", "element1")
                .put("team", "team1")
                .put("ep_this", "ep1");
        JSONObject jsonObject2 = new JSONObject()
                .put("first_name", "name2")
                .put("second_name", "surname2")
                .put("web_name", "webname2")
                .put("id", "id2")
                .put("element_type", "element2")
                .put("team", "team2")
                .put("ep_this", "ep2");

        JSONArray expectedJsonArray = new JSONArray()
                .put(jsonObject1)
                .put(jsonObject2);

        assertTrue(expectedJsonArray.similar(dataArray));
    }

    @Test
    public void givenEmptyJsonArray_getData_thenReturnEmptyJsonArray() {
        PLAYER_PROVIDER = new PlayerProvider(new JSONArray());
        JSONArray dataArray = PLAYER_PROVIDER.getData();

        assertTrue(EXPECTED_EMPTY_JSON_ARRAY.similar(dataArray));
    }

    @Test(expected = JSONException.class)
    public void givenInvalidJsonString_playerProvider_thenThrowJSONException() {
        new PlayerProvider(new JSONArray(INVALID_JSON_STRING));
    }

    @Test(expected = RuntimeException.class)
    public void givenInvalidJsonArrayNoIdOrName_getData_thenThrowJSONException() {
        JSONObject jsonObject1 = new JSONObject()
                .put("test1", "value1")
                .put("test2", "value2")
                .put("test3", "value3");
        JSONObject jsonObject2 = new JSONObject()
                .put("test1", "value4")
                .put("test2", "value5")
                .put("test3", "value6");

        JSONArray invalidJsonArrayNoIdOrName = new JSONArray()
                .put(jsonObject1)
                .put(jsonObject2);

        PLAYER_PROVIDER = new PlayerProvider(invalidJsonArrayNoIdOrName);
        PLAYER_PROVIDER.getData();
    }
}
