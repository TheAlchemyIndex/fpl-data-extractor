package org.tai.fpl.connectors;

import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.Test;

import java.io.IOException;
import java.net.URL;

import static org.junit.Assert.*;

public class UrlConnectorIT {
    private static final String MAIN_URL = "https://fantasy.premierleague.com/api/bootstrap-static/";
    private static final String FIXTURE_URL = "https://fantasy.premierleague.com/api/fixtures/";

    @Test
    public void givenValidMainUrl_getResponseString_thenReturnValidJsonString() throws IOException {
        UrlConnector urlConnector = new UrlConnector(new URL(MAIN_URL));
        String responseString = urlConnector.getResponseString();

        assertNotNull(responseString);
        assertNotEquals("", responseString);

        JSONObject stringToJson = new JSONObject(responseString);
        assertEquals(JSONObject.class, stringToJson.getClass());
    }

    @Test
    public void givenValidFixtureUrl_getResponseString_thenReturnValidJsonString() throws IOException {
        UrlConnector urlConnector = new UrlConnector(new URL(FIXTURE_URL));
        String responseString = urlConnector.getResponseString();

        assertNotNull(responseString);
        assertNotEquals("", responseString);

        JSONArray stringToJson = new JSONArray(responseString);
        assertEquals(JSONArray.class, stringToJson.getClass());
    }
}
