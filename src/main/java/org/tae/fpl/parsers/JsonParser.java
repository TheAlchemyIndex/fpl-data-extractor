package org.tae.fpl.parsers;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class JsonParser {
    private final String jsonString;

    public JsonParser(String jsonString) {
        this.jsonString = jsonString;
    }

    public JSONObject parseJsonObject() {
        JSONObject parsedJson = new JSONObject();

        try {
            parsedJson = new JSONObject(this.jsonString);
        } catch (JSONException jsonException) {
            System.out.println("Error parsing data string to JSONObject: " + jsonException.getMessage());
        }
        return parsedJson;
    }

    public JSONArray parseJsonArray() {
        JSONArray parsedJson = new JSONArray();

        try {
            parsedJson = new JSONArray(this.jsonString);
        } catch (JSONException jsonException) {
            System.out.println("Error parsing data string to JSONArray: " + jsonException.getMessage());
        }
        return parsedJson;
    }
}
