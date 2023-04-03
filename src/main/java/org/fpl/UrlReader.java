package org.fpl;

import org.fpl.connectors.UrlConnector;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class UrlReader {

    private final UrlConnector urlConnector;

    public UrlReader(UrlConnector urlConnector) {
        this.urlConnector = urlConnector;
    }

    private String getData() throws IOException {
        if (this.urlConnector.getResponseCode() != 200) {
            throw new RuntimeException("HttpResponseCode: " + this.urlConnector.getResponseCode());
        } else {
            String inputLine;
            BufferedReader bufferedReader =
                    new BufferedReader(new InputStreamReader(this.urlConnector.getConnection().getInputStream()));
            StringBuilder responseString = new StringBuilder();

            while ((inputLine = bufferedReader.readLine()) != null) {
                responseString.append(inputLine);
            }
            bufferedReader.close();

            return String.valueOf(responseString);
        }
    }

    public JSONObject parseJSONObject() {
        JSONObject jsonResponse = new JSONObject();

        try {
            jsonResponse = new JSONObject(getData());
        } catch (IOException ioException) {
            System.out.println("Error reading data from url: " + ioException.getMessage());
        } catch (JSONException jsonException) {
            System.out.println("Error passing data string to JSONObject: " + jsonException.getMessage());
        }
        return jsonResponse;
    }

    public JSONArray parseJSONArray() {
        JSONArray jsonResponse = new JSONArray();

        try {
            jsonResponse = new JSONArray(getData());
        } catch (IOException ioException) {
            System.out.println("Error reading data from url: " + ioException.getMessage());
        } catch (JSONException jsonException) {
            System.out.println("Error passing data string to JSONArray: " + jsonException.getMessage());
        }
        return jsonResponse;
    }
}
