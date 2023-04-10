package org.tai.fpl;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONException;
import org.json.JSONObject;
import org.tai.fpl.connectors.UrlConnector;
import org.tai.fpl.parsers.JsonParser;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

public class DataExtractor {
    private static final Logger LOGGER = LogManager.getLogger(Main.class);
    private final String url;

    public DataExtractor(String url) {
        this.url = url;
    }

    public JSONObject getJsonFromUrl() {
        JsonParser jsonParser;
        JSONObject data = new JSONObject();

        try {
            UrlConnector urlConnector = new UrlConnector(new URL(this.url));
            jsonParser = new JsonParser(urlConnector.getResponseString());
            data = jsonParser.parseJsonObject();
        } catch(MalformedURLException malformedURLException) {
            LOGGER.error("Invalid target url provided: " + malformedURLException.getMessage());
        } catch(IOException ioException) {
            LOGGER.error("Error connecting to the provided target url: " + ioException.getMessage());
        } catch(RuntimeException runtimeException) {
            if (runtimeException instanceof JSONException) {
                LOGGER.error("Error parsing JSON data using JsonParser: " + runtimeException.getMessage());
            } else {
                LOGGER.error("Error connecting to the provided target url: " + runtimeException.getMessage());
            }
        }
        return data;
    }
}
