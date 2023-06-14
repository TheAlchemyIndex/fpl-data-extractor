package org.tai.fpl.writers;

import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.CDL;
import org.json.JSONArray;
import org.tai.fpl.extractors.GameweekExtractor;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;

public class FileWriter {
    private static final Logger LOGGER = LogManager.getLogger(GameweekExtractor.class);
    private final String baseFilePath;
    private final String season;

    public FileWriter(String baseFilePath, String season) {
        this.baseFilePath = baseFilePath;
        this.season = season;
    }

    public void writeDataToBasePath(JSONArray jsonData, String subFilePath) {
        File file = new File(String.format("%s/%s", this.baseFilePath, subFilePath));
        String csvString = CDL.toString(jsonData);
        try {
            FileUtils.writeStringToFile(file, csvString, Charset.defaultCharset());
            LOGGER.info(String.format("Write to {%s} complete.", subFilePath));
        } catch (IOException ioException) {
            throw new RuntimeException(String.format("Error writing data to file in base file path: {%s}", ioException.getMessage()));
        }
    }

    public void writeDataToSeasonPath(JSONArray jsonData, String subFilePath) {
        File file = new File(String.format("%s/%s/%s", this.baseFilePath, this.season, subFilePath));
        String csvString = CDL.toString(jsonData);
        try {
            FileUtils.writeStringToFile(file, csvString, Charset.defaultCharset());
            LOGGER.info(String.format("Write to {%s} complete.", subFilePath));
        } catch (IOException ioException) {
            throw new RuntimeException(String.format("Error writing data to file in season file path: {%s}", ioException.getMessage()));
        }
    }
}
