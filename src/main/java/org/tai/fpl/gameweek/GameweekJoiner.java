package org.tai.fpl.gameweek;

import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;
import org.tai.fpl.Main;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

public class GameweekJoiner {
    private static final Logger LOGGER = LogManager.getLogger(Main.class);
    private final int currentGameweekNumber;

    public GameweekJoiner(int currentGameweekNumber) {
        this.currentGameweekNumber = currentGameweekNumber;
    }

    public JSONArray joinGameweeks(String baseFilePath) {
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
            }
        } catch(IOException ioException) {
            LOGGER.error("Error joining previous gameweek files together: " + ioException.getMessage());
        }
        return allGameweeks;
    }
}