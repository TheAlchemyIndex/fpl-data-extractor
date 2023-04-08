package org.tai.fpl.parsers;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Test;

import static org.junit.Assert.*;

public class JsonParserTests {

    private static final String VALID_JSON_OBJECT_STRING = "{\"test1\":\"value1\",\"test2\":value2}";
    private static final String VALID_JSON_ARRAY_STRING = "[{\"test1\": \"value1\",\"test2\":value2},{\"test3\": \"value3\",\"test4\":value4}]";
    private static JsonParser JSON_PARSER;

    private static final JSONObject EXPECTED_VALID_JSON_OBJECT_1 = new JSONObject()
            .put("test1", "value1")
            .put("test2", "value2");
    private static final JSONObject EXPECTED_VALID_JSON_OBJECT_2 = new JSONObject()
            .put("test3", "value3")
            .put("test4", "value4");

    private static final JSONArray EXPECTED_VALID_JSON_ARRAY = new JSONArray()
            .put(EXPECTED_VALID_JSON_OBJECT_1)
            .put(EXPECTED_VALID_JSON_OBJECT_2);

    private static final String INVALID_JSON_STRING = "test1:value1,test2:value2";

    @Test
    public void parseJsonObjectValidString() {
        JSON_PARSER = new JsonParser(VALID_JSON_OBJECT_STRING);
        JSONObject jsonObject = JSON_PARSER.parseJsonObject();

        assertTrue(EXPECTED_VALID_JSON_OBJECT_1.similar(jsonObject));
    }

    @Test
    public void parseJsonArrayValidString() {
        JSON_PARSER = new JsonParser(VALID_JSON_ARRAY_STRING);
        JSONArray jsonArray = JSON_PARSER.parseJsonArray();

        assertTrue(EXPECTED_VALID_JSON_ARRAY.similar(jsonArray));
    }

    @Test(expected = JSONException.class)
    public void parseJsonObjectValidArrayString() {
        JSON_PARSER = new JsonParser(VALID_JSON_ARRAY_STRING);
        JSON_PARSER.parseJsonObject();
    }

    @Test(expected = JSONException.class)
    public void parseJsonArrayValidObjectString() {
        JSON_PARSER = new JsonParser(VALID_JSON_OBJECT_STRING);
        JSON_PARSER.parseJsonArray();
    }

    @Test(expected = JSONException.class)
    public void parseJsonObjectInvalidString() {
        JSON_PARSER = new JsonParser(INVALID_JSON_STRING);
        JSON_PARSER.parseJsonObject();
    }

    @Test(expected = JSONException.class)
    public void parseJsonArrayInvalidString() {
        JSON_PARSER = new JsonParser(INVALID_JSON_STRING);
        JSON_PARSER.parseJsonArray();
    }

    @Test(expected = JSONException.class)
    public void parseJsonObjectNullString() {
        JSON_PARSER = new JsonParser(INVALID_JSON_STRING);
        JSON_PARSER.parseJsonObject();
    }

    @Test(expected = JSONException.class)
    public void parseJsonArrayNullString() {
        JSON_PARSER = new JsonParser(INVALID_JSON_STRING);
        JSON_PARSER.parseJsonArray();
    }

    @Test(expected = JSONException.class)
    public void parseJsonObjectEmptyString() {
        JSON_PARSER = new JsonParser("");
        JSON_PARSER.parseJsonObject();
    }

    @Test(expected = JSONException.class)
    public void parseJsonArrayEmptyString() {
        JSON_PARSER = new JsonParser("");
        JSON_PARSER.parseJsonArray();
    }
}
