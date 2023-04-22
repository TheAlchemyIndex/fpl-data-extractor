package org.tai.fpl.joiners;

import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;
import org.tai.fpl.writers.FileWriter;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

public class SeasonJoiner {
    private static final Logger LOGGER = LogManager.getLogger(SeasonJoiner.class);
    private final int startingSeasonStart;
    private final int startingSeasonEnd;
    private final int endingSeasonEnd;

    public SeasonJoiner(int startingSeasonStart, int startingSeasonEnd, int endingSeasonEnd) {
        this.startingSeasonStart = startingSeasonStart;
        this.startingSeasonEnd = startingSeasonEnd;
        this.endingSeasonEnd = endingSeasonEnd;
    }

    public void joinSeasons(FileWriter fileWriter, String baseFilePath, String subFilePath) {
        CsvMapper csvMapper = new CsvMapper();
        ObjectMapper objectMapper = new ObjectMapper();
        JSONArray allSeasons = new JSONArray();

        try {
            for (int i = this.startingSeasonStart, j = this.startingSeasonEnd; j <= this.endingSeasonEnd; i++, j++) {
                List<Map<String, String>> rows;
                try (MappingIterator<Map<String, String>> mappingIterator = csvMapper
                        .readerWithSchemaFor(Map.class)
                        .with(CsvSchema.emptySchema().withHeader())
                        .readValues(new File(String.format("%s%s-%s/gws/merged_gw.csv", baseFilePath, i, j)))) {
                    rows = mappingIterator.readAll();
                }

                for (Map<String, String> row : rows) {
                    String jsonString = objectMapper.writeValueAsString(row);
                    allSeasons.put(new JSONObject(jsonString));
                }
            }
        } catch(IOException ioException) {
            LOGGER.error("Error joining previous season files together: " + ioException.getMessage());
        }
        try {
            fileWriter.writeDataToBasePath(allSeasons, subFilePath);
        } catch (IOException ioException) {
            LOGGER.error("Error writing season data to file: " + ioException.getMessage());
        }
    }
}
