package org.example;

import org.json.JSONArray;
import java.io.IOException;
import java.net.URL;

import static org.example.JsonParser.extractData;
import static org.example.JsonParser.getJson;
import static org.example.Parsers.extractPlayerData;

public class Main {

    private static final String TARGET_URL = "https://fantasy.premierleague.com/api/bootstrap-static/";
    private static final String SEASON = "2022-23";
    private static final String BASE_FILENAME = String.format("data/%s/", SEASON);

    public static void main(String[] args) throws IOException {

        String jsonData = getJson(new UrlConnector(new URL(TARGET_URL)));
        JSONArray elements = extractData(jsonData, "elements");
        extractPlayerData(elements, BASE_FILENAME);
        System.out.println("test");

    }
}