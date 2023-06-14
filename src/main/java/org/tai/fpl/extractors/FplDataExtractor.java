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
            LOGGER.info(String.format("Data extraction from {%s} complete.", this.url));
            return data;
        } catch(MalformedURLException malformedURLException) {
            throw new RuntimeException(String.format("Invalid target url provided {%s}: %s", this.url,
                    malformedURLException.getMessage()));
        } catch(IOException ioException) {
            throw new RuntimeException(String.format("Error connecting to the provided target url {%s}: %s", this.url,
                    ioException.getMessage()));
        } catch(RuntimeException runtimeException) {
            if (runtimeException instanceof JSONException) {
                throw new RuntimeException(String.format("Error parsing JSON data using JsonParser: %s",
                        runtimeException.getMessage()));
            } else {
                throw new RuntimeException(String.format("Error connecting to the provided target url {%s}: %s", this.url,
                        runtimeException.getMessage()));
            }
        }
    }
}
