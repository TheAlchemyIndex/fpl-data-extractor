package org.tai.fpl.providers.impl;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Test;
import org.tai.fpl.providers.impl.TeamProvider;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class TeamProviderTest {
    private static TeamProvider TEAM_PROVIDER;

    private static final JSONObject VALID_JSON_OBJECT_1 = new JSONObject()
            .put("id", "1")
            .put("name", "team1")
            .put("test1", "value1")
            .put("test2", "value2")
            .put("test3", "value3");
    private static final JSONObject VALID_JSON_OBJECT_2 = new JSONObject()
            .put("id", "2")
            .put("name", "team2")
            .put("test1", "value4")
            .put("test2", "value5")
            .put("test3", "value6");

    private static final JSONArray VALID_JSON_ARRAY = new JSONArray()
            .put(VALID_JSON_OBJECT_1)
            .put(VALID_JSON_OBJECT_2);

    private static final String INVALID_JSON_STRING = "test1:value1,test2:value2";

    private static final Map<Integer, String> EXPECTED_TEAMS_MAP  = new HashMap<>() {{
        put(1, "team1");
        put(2, "team2");
    }};
    private static final JSONArray EXPECTED_EMPTY_JSON_ARRAY = new JSONArray();

    @Test
    public void givenValidJsonArray_getData_thenReturnValidJsonArray() {
        TEAM_PROVIDER = new TeamProvider(VALID_JSON_ARRAY);
        JSONArray dataArray = TEAM_PROVIDER.getData();

        JSONObject jsonObject1 = new JSONObject()
                .put("id", "1")
                .put("name", "team1")
                .put("test1", "value1")
                .put("test2", "value2")
                .put("test3", "value3");
        JSONObject jsonObject2 = new JSONObject()
                .put("id", "2")
                .put("name", "team2")
                .put("test1", "value4")
                .put("test2", "value5")
                .put("test3", "value6");

        JSONArray expectedJsonArray = new JSONArray()
                .put(jsonObject1)
                .put(jsonObject2);

        assertTrue(expectedJsonArray.similar(dataArray));
    }

    @Test
    public void givenValidJsonArray_getTeams_thenReturnValidTeamsMap() {
        TEAM_PROVIDER = new TeamProvider(VALID_JSON_ARRAY);
        Map<Integer, String> teamsMap = TEAM_PROVIDER.getTeams();

        assertEquals(EXPECTED_TEAMS_MAP, teamsMap);
    }

    @Test
    public void givenEmptyJsonArray_getData_thenReturnEmptyJsonArray() {
        TEAM_PROVIDER = new TeamProvider(new JSONArray());
        JSONArray dataArray = TEAM_PROVIDER.getData();

        assertTrue(EXPECTED_EMPTY_JSON_ARRAY.similar(dataArray));
    }

    @Test
    public void givenEmptyJsonArray_getTeams_thenReturnEmptyMap() {
        TEAM_PROVIDER = new TeamProvider(new JSONArray());
        Map<Integer, String> teamsMap = TEAM_PROVIDER.getTeams();

        assertEquals(new HashMap<>(), teamsMap);
    }

    @Test(expected = JSONException.class)
    public void givenInvalidJsonString_teamProvider_thenThrowJSONException() {
        new TeamProvider(new JSONArray(INVALID_JSON_STRING));
    }

    @Test(expected = RuntimeException.class)
    public void givenInvalidJsonArrayNoIdOrName_getTeams_thenThrowJSONException() {
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

        TEAM_PROVIDER = new TeamProvider(invalidJsonArrayNoIdOrName);
        TEAM_PROVIDER.getTeams();
    }
}
