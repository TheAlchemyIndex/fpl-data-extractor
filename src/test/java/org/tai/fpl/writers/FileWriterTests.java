package org.tai.fpl.writers;

import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.After;
import org.junit.Test;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static org.junit.Assert.assertTrue;

public class FileWriterTests {
    private static final Logger LOGGER = LogManager.getLogger(FileWriterTests.class);

    private static FileWriter FILE_WRITER;
    private static final String BASE_FILEPATH = "src/test/data/";
    private static final String SEASON = "2022-23";
    private static final String FULL_FILEPATH = String.format("%s%s/", BASE_FILEPATH, SEASON);
    private static final String GAMEWEEK_FILENAME = "test_gameweek_filename.csv";
    private static final String SEASON_FILENAME = "test_season_filename.csv";

    private static final String EXPECTED_GAMEWEEK_FILEPATH = "src/test/data/2022-23/test_gameweek_filename.csv";
    private static final String EXPECTED_SEASON_FILEPATH = "src/test/data/test_season_filename.csv";

    private static final JSONObject VALID_JSON_OBJECT_1 = new JSONObject()
            .put("test1", "value1")
            .put("test2", "value2")
            .put("test3", "value3");
    private static final JSONObject VALID_JSON_OBJECT_2 = new JSONObject()
            .put("test1", "value4")
            .put("test2", "value5")
            .put("test3", "value6");

    private static final JSONArray VALID_JSON_ARRAY = new JSONArray()
            .put(VALID_JSON_OBJECT_1)
            .put(VALID_JSON_OBJECT_2);

    @After
    public void deleteFiles() {
        File seasonDirectory = new File(String.format("%s%s", BASE_FILEPATH, SEASON));
        File baseDirectory = new File(BASE_FILEPATH);

        try {
            for (File file: Objects.requireNonNull(seasonDirectory.listFiles())) {
                file.delete();
            }
            seasonDirectory.delete();
        } catch(NullPointerException nullPointerException) {
            LOGGER.info(String.format("No files to delete in %s", seasonDirectory));
        }

        try {
            for (File file: Objects.requireNonNull(baseDirectory.listFiles())) {
                file.delete();
            }
            baseDirectory.delete();
        } catch(NullPointerException nullPointerException) {
            LOGGER.info(String.format("No files to delete in %s", baseDirectory));
        }
    }

    @Test
    public void createGameweekFile() throws IOException {
        FILE_WRITER = new FileWriter(BASE_FILEPATH, SEASON);
        FILE_WRITER.writeDataToSeasonPath(VALID_JSON_ARRAY, GAMEWEEK_FILENAME);

        File gameweekFile = new File(EXPECTED_GAMEWEEK_FILEPATH);
        assertTrue(gameweekFile.exists());
    }

    @Test
    public void createSeasonFile() throws IOException {
        FILE_WRITER = new FileWriter(BASE_FILEPATH, SEASON);
        FILE_WRITER.writeDataToBasePath(VALID_JSON_ARRAY, SEASON_FILENAME);

        File seasonFile = new File(EXPECTED_SEASON_FILEPATH);
        assertTrue(seasonFile.exists());
    }

    @Test
    public void writeGameweekDataToFile() throws IOException {
        FILE_WRITER = new FileWriter(BASE_FILEPATH, SEASON);
        FILE_WRITER.writeDataToSeasonPath(VALID_JSON_ARRAY, GAMEWEEK_FILENAME);

        assertTrue(readDataFromFile(String.format("%s%s", FULL_FILEPATH, GAMEWEEK_FILENAME)).similar(VALID_JSON_ARRAY));
    }

    @Test
    public void writeSeasonDataToFile() throws IOException {
        FILE_WRITER = new FileWriter(BASE_FILEPATH, SEASON);
        FILE_WRITER.writeDataToBasePath(VALID_JSON_ARRAY, SEASON_FILENAME);

        assertTrue(readDataFromFile(String.format("%s%s", BASE_FILEPATH, SEASON_FILENAME)).similar(VALID_JSON_ARRAY));
    }

    @Test(expected = IllegalArgumentException.class)
    public void nullBaseFilepath() {
        FILE_WRITER = new FileWriter(null, SEASON);
    }

    @Test(expected = IllegalArgumentException.class)
    public void nullSeason() {
        FILE_WRITER = new FileWriter(BASE_FILEPATH, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void nullBaseFilepathNullSeason() {
        FILE_WRITER = new FileWriter(null, null);
    }

    private JSONArray readDataFromFile(String filepath) {
        CsvMapper csvMapper = new CsvMapper();
        ObjectMapper objectMapper = new ObjectMapper();
        JSONArray allRows = new JSONArray();

        try {
            List<Map<String, String>> rows;
            try (MappingIterator<Map<String, String>> mappingIterator = csvMapper
                    .readerWithSchemaFor(Map.class)
                    .with(CsvSchema.emptySchema().withHeader())
                    .readValues(new File(filepath))) {
                rows = mappingIterator.readAll();
            }

            for (Map<String, String> row : rows) {
                String jsonString = objectMapper.writeValueAsString(row);
                allRows.put(new JSONObject(jsonString));
            }
        } catch(IOException ioException) {
            LOGGER.error("Error reading data from file: " + ioException.getMessage());
        }
        return allRows;
    }
}
