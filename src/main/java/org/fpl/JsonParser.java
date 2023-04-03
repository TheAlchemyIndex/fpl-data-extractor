package org.fpl;

import org.fpl.connectors.UrlConnector;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;

public class JsonParser {

    public static JSONObject getJSONObject(UrlConnector urlConnector) {
        JSONObject jsonResponse = new JSONObject();

        try {
            jsonResponse = new JSONObject(UrlReader.readData(urlConnector));
        } catch (IOException ioException) {
            System.out.println("Error reading data from url: " + ioException.getMessage());
        } catch (JSONException jsonException) {
            System.out.println("Error passing data string to JSONObject: " + jsonException.getMessage());
        }
        return jsonResponse;
    }

    public static JSONArray getJSONArray(UrlConnector urlConnector) {
        JSONArray jsonResponse = new JSONArray();

        try {
            jsonResponse = new JSONArray(UrlReader.readData(urlConnector));
        } catch (IOException ioException) {
            System.out.println("Error reading data from url: " + ioException.getMessage());
        } catch (JSONException jsonException) {
            System.out.println("Error passing data string to JSONArray: " + jsonException.getMessage());
        }
        return jsonResponse;
    }
}
