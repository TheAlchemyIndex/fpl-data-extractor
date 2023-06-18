package org.tai.fpl.connectors;

import org.json.JSONObject;
import org.junit.Test;

import java.io.IOException;
import java.net.URL;

import static org.junit.Assert.*;

public class UrlConnectorIT {
    private static final String TEST_URL = "https://fantasy.premierleague.com/api/bootstrap-static/";

    @Test
    public void givenValidUrl_getResponseString_thenReturnValidJsonString() throws IOException {
        UrlConnector urlConnector = new UrlConnector(new URL(TEST_URL));
        String responseString = urlConnector.getResponseString();

        assertNotNull(responseString);
        assertNotEquals("", responseString);

        JSONObject stringToJson = new JSONObject(responseString);
        assertEquals(JSONObject.class, stringToJson.getClass());
    }
}
