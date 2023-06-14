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
import org.tai.fpl.util.constants.FileNames;
import org.tai.fpl.writers.FileWriter;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

public class FixtureJoiner implements Joiner {
    private static final Logger LOGGER = LogManager.getLogger(FixtureJoiner.class);
    private final int startingSeasonStart;
    private final int startingSeasonEnd;
    private final int finalSeasonEnd;

    public FixtureJoiner(int startingSeasonStart, int startingSeasonEnd, int finalSeasonEnd) throws IllegalArgumentException {
        this.startingSeasonStart = startingSeasonStart;
        this.startingSeasonEnd = startingSeasonEnd;
        this.finalSeasonEnd = finalSeasonEnd;
    }

    public void join(FileWriter fileWriter, String baseFilePath, String subFilePath) {
        CsvMapper csvMapper = new CsvMapper();
        ObjectMapper objectMapper = new ObjectMapper();
        JSONArray allFixtures = new JSONArray();

        try {
            for (int i = this.startingSeasonStart, j = this.startingSeasonEnd; j <= this.finalSeasonEnd; i++, j++) {
                List<Map<String, String>> rows;
                try (MappingIterator<Map<String, String>> mappingIterator = csvMapper
                        .readerWithSchemaFor(Map.class)
                        .with(CsvSchema.emptySchema().withHeader())
                        .readValues(new File(String.format("%s%s-%s/%s", baseFilePath, i, j, FileNames.FIXTURES_FILENAME)))) {
                    rows = mappingIterator.readAll();
                }

                for (Map<String, String> row : rows) {
                    String jsonString = objectMapper.writeValueAsString(row);
                    allFixtures.put(new JSONObject(jsonString));
                }
                LOGGER.info(String.format("Fixtures - Season {%s-%s}.", i, j));
                fileWriter.writeDataToBasePath(allFixtures, subFilePath);
            }
        } catch (IOException ioException) {
            throw new RuntimeException(String.format("Error joining fixture files together: {%s}", ioException.getMessage()));
        }
    }
}
