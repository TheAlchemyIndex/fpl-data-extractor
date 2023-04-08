package org.tai.fpl.parsers;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class JsonParser {
    private final String jsonString;

    public JsonParser(String jsonString) {
        this.jsonString = jsonString;
    }

    public JSONObject parseJsonObject() throws JSONException {
        return new JSONObject(this.jsonString);
    }

    public JSONArray parseJsonArray() throws JSONException {
        return new JSONArray(this.jsonString);
    }
}
