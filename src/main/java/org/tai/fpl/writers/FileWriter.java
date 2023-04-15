package org.tai.fpl.writers;

import org.apache.commons.io.FileUtils;
import org.json.CDL;
import org.json.JSONArray;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;

public class FileWriter {
    private final String baseFilePath;
    private final String seasonFilePath;

    public FileWriter(String baseFilePath, String season) {
        this.baseFilePath = baseFilePath;
        this.seasonFilePath = String.format("%s%s/", baseFilePath, season);
    }

    public void writeDataGameweeks(JSONArray elements, String secondaryFilePath) {
        try {
            File file = new File(String.format("%s/%s", this.seasonFilePath, secondaryFilePath));
            String csvString = CDL.toString(elements);
            FileUtils.writeStringToFile(file, csvString, Charset.defaultCharset());
        }
        catch (IOException ioException) {
            String exceptionMessage = String.format("Error extracting data to file: %s", ioException.getMessage());
            System.out.println(exceptionMessage);
        }
    }

    public void writeDataSeasons(JSONArray elements, String secondaryFilePath) {
        try {
            File file = new File(String.format("%s/%s", this.baseFilePath, secondaryFilePath));
            String csvString = CDL.toString(elements);
            FileUtils.writeStringToFile(file, csvString, Charset.defaultCharset());
        }
        catch (IOException ioException) {
            String exceptionMessage = String.format("Error extracting data to file: %s", ioException.getMessage());
            System.out.println(exceptionMessage);
        }
    }
}
