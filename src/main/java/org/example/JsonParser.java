package org.example;

import org.json.JSONArray;
import org.json.JSONObject;
import java.io.IOException;

public class JsonParser {

    public static String getJson(UrlConnector urlConnector) {

        String responseString = "";
        try {
            responseString = UrlReader.readData(urlConnector);
        } catch (IOException ioException) {
            System.out.println("Error reading data from url using readData method of UrlReader: "
                    + ioException.getMessage());
        }
        return responseString;
    }

    public static JSONArray extractData(String responseString, String key) {

        JSONArray elements = new JSONArray();
        try {
            JSONObject jsonResponse = new JSONObject(responseString);
            elements = jsonResponse.getJSONArray((key));
        } catch (RuntimeException runtimeException) {
            System.out.println(runtimeException.getMessage());
        }
        return elements;
    }
}