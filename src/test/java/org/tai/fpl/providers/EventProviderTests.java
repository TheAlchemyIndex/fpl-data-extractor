package org.tai.fpl.providers;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Test;
import org.tai.fpl.providers.impl.EventProvider;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class EventProviderTests {

    private static EventProvider EVENT_PROVIDER;

    private static final JSONObject VALID_JSON_OBJECT_1 = new JSONObject()
            .put("is_current", "false")
            .put("id", "1");
    private static final JSONObject VALID_JSON_OBJECT_2 = new JSONObject()
            .put("is_current", "true")
            .put("id", "2");

    private static final JSONArray VALID_JSON_ARRAY = new JSONArray()
            .put(VALID_JSON_OBJECT_1)
            .put(VALID_JSON_OBJECT_2);

    private static final String INVALID_JSON_STRING = "test1:value1,test2:value2";

    private static final JSONArray EXPECTED_EMPTY_JSON_ARRAY = new JSONArray();
    private static final int EXPECTED_GAMEWEEK_NUMBER = 2;

    @Test
    public void getDataValidJsonArray() {
        EVENT_PROVIDER = new EventProvider(VALID_JSON_ARRAY);
        JSONArray dataArray = EVENT_PROVIDER.getData();

        JSONObject jsonObject1 = new JSONObject()
                .put("is_current", "false")
                .put("id", "1");
        JSONObject jsonObject2 = new JSONObject()
                .put("is_current", "true")
                .put("id", "2");

        JSONArray expectedJsonArray = new JSONArray()
                .put(jsonObject1)
                .put(jsonObject2);

        assertTrue(expectedJsonArray.similar(dataArray));
    }

    @Test
    public void getCurrentGameweekValidJsonArray() {
        EVENT_PROVIDER = new EventProvider(VALID_JSON_ARRAY);
        int currentGameweek = EVENT_PROVIDER.getCurrentGameweek();

        assertEquals(EXPECTED_GAMEWEEK_NUMBER, currentGameweek);
    }

    @Test
    public void getDataEmptyJsonArray() {
        EVENT_PROVIDER = new EventProvider(new JSONArray());
        JSONArray dataArray = EVENT_PROVIDER.getData();

        assertTrue(EXPECTED_EMPTY_JSON_ARRAY.similar(dataArray));
    }

    @Test
    public void getCurrentGameweekEmptyJsonArray() {
        EVENT_PROVIDER = new EventProvider(new JSONArray());
        int currentGameweek = EVENT_PROVIDER.getCurrentGameweek();

        assertEquals(0, currentGameweek);
    }

    @Test(expected = IllegalArgumentException.class)
    public void nullJsonArray() {
        new EventProvider(null);
    }

    @Test(expected = JSONException.class)
    public void invalidJsonArray() {
        new EventProvider(new JSONArray(INVALID_JSON_STRING));
    }
}
