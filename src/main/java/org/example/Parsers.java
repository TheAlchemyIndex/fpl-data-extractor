package org.example;

import org.apache.commons.io.FileUtils;
import org.json.CDL;
import org.json.JSONArray;
import org.json.JSONObject;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;

public class Parsers {

    public static ArrayList<String> extractPlayerStatHeaders(JSONObject statHeaders) {
        ArrayList<String> headers = new ArrayList<>();
        statHeaders.keys().forEachRemaining(headers::add);
        return headers;
    }

    public static void extractPlayerData(JSONArray elements, String baseFileName) {
        try {
            String fileName = String.format("%splayers_raw.csv", baseFileName);
            File file = new File(fileName);
            String csvString = CDL.toString(elements);
            FileUtils.writeStringToFile(file, csvString, Charset.defaultCharset());
        }
        catch (IOException ioException) {
            String exceptionMessage = String.format("Error extracting raw player data to csv: %s", ioException.getMessage());
            System.out.println(exceptionMessage);
        }
    }
}
