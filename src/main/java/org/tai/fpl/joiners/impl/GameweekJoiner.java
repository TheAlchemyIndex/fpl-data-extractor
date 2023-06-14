package org.tai.fpl.joiners.impl;

import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;
import org.tai.fpl.joiners.Joiner;
import org.tai.fpl.writers.FileWriter;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

public class GameweekJoiner implements Joiner {
    private static final Logger LOGGER = LogManager.getLogger(GameweekJoiner.class);
    private final int currentGameweekNumber;

    public GameweekJoiner(int currentGameweekNumber) {
        this.currentGameweekNumber = currentGameweekNumber;
    }

    public void join(FileWriter fileWriter, String baseFilePath, String subFilePath) {
        CsvMapper csvMapper = new CsvMapper();
        ObjectMapper objectMapper = new ObjectMapper();
        JSONArray allGameweeks = new JSONArray();

        try {
            for (int i = 1; i <= this.currentGameweekNumber; i++) {
                List<Map<String, String>> rows;
                try (MappingIterator<Map<String, String>> mappingIterator = csvMapper
                        .readerWithSchemaFor(Map.class)
                        .with(CsvSchema.emptySchema().withHeader())
                        .readValues(new File(String.format("%sgws/gw%s.csv", baseFilePath, i)))) {
                    rows = mappingIterator.readAll();
                }

                for (Map<String, String> row : rows) {
                    String jsonString = objectMapper.writeValueAsString(row);
                    allGameweeks.put(new JSONObject(jsonString));
                }
                LOGGER.info(String.format("Gameweek {%s}.", i));
                fileWriter.writeDataToSeasonPath(allGameweeks, subFilePath);
            }
        } catch(IOException ioException) {
            throw new RuntimeException(String.format("Error joining previous gameweek files together: {%s}", ioException.getMessage()));
        }
    }
}
