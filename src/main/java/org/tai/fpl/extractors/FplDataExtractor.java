package org.tai.fpl.extractors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONException;
import org.json.JSONObject;
import org.tai.fpl.connectors.UrlConnector;
import org.tai.fpl.util.parsers.JsonParser;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

public class FplDataExtractor {
    private static final Logger LOGGER = LogManager.getLogger(FplDataExtractor.class);
    private final String url;

    public FplDataExtractor(String url) {
        this.url = url;
    }

    public JSONObject getData() {
        JsonParser jsonParser;
        JSONObject data;
        try {
            UrlConnector urlConnector = new UrlConnector(new URL(this.url));
            jsonParser = new JsonParser(urlConnector.getResponseString());
            data = jsonParser.parseJsonObject();
            LOGGER.info("Data extraction from {} complete.", this.url);
            return data;
        } catch (MalformedURLException e) {
            throw new RuntimeException(String.format("Invalid target URL provided {%s}: %s", this.url, e.getMessage()));
        } catch (IOException | JSONException e) {
            throw new RuntimeException(String.format("Error processing the target URL {%s}: %s", this.url, e.getMessage()));
        }
    }
}
