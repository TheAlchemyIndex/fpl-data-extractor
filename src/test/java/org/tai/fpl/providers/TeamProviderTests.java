package org.tai.fpl.providers;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Test;
import org.tai.fpl.providers.impl.TeamProvider;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class TeamProviderTests {

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
            .put("test4", "value4")
            .put("test5", "value5")
            .put("test6", "value6");

    private static final JSONArray VALID_JSON_ARRAY = new JSONArray()
            .put(VALID_JSON_OBJECT_1)
            .put(VALID_JSON_OBJECT_2);

    private static final String INVALID_JSON_STRING = "test1:value1,test2:value2";

    private static final JSONArray EXPECTED_EMPTY_JSON_ARRAY = new JSONArray();
    private static final Map<Integer, String> EXPECTED_TEAMS_MAP  = new HashMap<>() {{
        put(1, "team1");
        put(2, "team2");
    }};

    @Test
    public void getDataValidJsonArray() {
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
                .put("test4", "value4")
                .put("test5", "value5")
                .put("test6", "value6");

        JSONArray expectedJsonArray = new JSONArray()
                .put(jsonObject1)
                .put(jsonObject2);

        assertTrue(expectedJsonArray.similar(dataArray));
    }

    @Test
    public void getTeamsValidJsonArray() {
        TEAM_PROVIDER = new TeamProvider(VALID_JSON_ARRAY);
        Map<Integer, String> teamsMap = TEAM_PROVIDER.getTeams();

        assertEquals(EXPECTED_TEAMS_MAP, teamsMap);
    }

    @Test
    public void getDataEmptyJsonArray() {
        TEAM_PROVIDER = new TeamProvider(new JSONArray());
        JSONArray dataArray = TEAM_PROVIDER.getData();

        assertTrue(EXPECTED_EMPTY_JSON_ARRAY.similar(dataArray));
    }

    @Test
    public void getTeamsEmptyJsonArray() {
        TEAM_PROVIDER = new TeamProvider(new JSONArray());
        Map<Integer, String> teamsMap = TEAM_PROVIDER.getTeams();

        assertEquals(new HashMap<>(), teamsMap);
    }

    @Test(expected = IllegalArgumentException.class)
    public void nullJsonArray() {
        new TeamProvider(null);
    }

    @Test(expected = JSONException.class)
    public void invalidJsonArray() {
        new TeamProvider(new JSONArray(INVALID_JSON_STRING));
    }
}
