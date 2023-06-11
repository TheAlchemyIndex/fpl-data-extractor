package org.tai.fpl.writers;

import org.apache.commons.io.FileUtils;
import org.json.CDL;
import org.json.JSONArray;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;

public class FileWriter {
    private final String baseFilePath;
    private final String season;

    public FileWriter(String baseFilePath, String season) {
        this.baseFilePath = baseFilePath;
        this.season = season;
    }

    public void writeDataToBasePath(JSONArray elements, String subFilePath) {
        File file = new File(String.format("%s/%s", this.baseFilePath, subFilePath));
        String csvString = CDL.toString(elements);
        try {
            FileUtils.writeStringToFile(file, csvString, Charset.defaultCharset());
        } catch (IOException ioException) {
            throw new RuntimeException(String.format("Error writing data to file in base file path: %s", ioException.getMessage()));
        }
    }

    public void writeDataToSeasonPath(JSONArray elements, String subFilePath) {
        File file = new File(String.format("%s/%s/%s", this.baseFilePath, this.season, subFilePath));
        String csvString = CDL.toString(elements);
        try {
            FileUtils.writeStringToFile(file, csvString, Charset.defaultCharset());
        } catch (IOException ioException) {
            throw new RuntimeException(String.format("Error writing data to file in season file path: %s", ioException.getMessage()));
        }
    }
}
