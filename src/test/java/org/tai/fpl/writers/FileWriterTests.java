package org.tai.fpl.writers;

import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.Test;
import org.tai.fpl.TestHelper;

import java.io.File;

import static org.junit.Assert.assertTrue;

public class FileWriterTests extends TestHelper {
    private static FileWriter FILE_WRITER;
    private static final String EXPECTED_FILEPATH = "src/test/resources/data/test_filename.csv";

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

    @Test
    public void givenValidJSONArrayAndValidFilePath_write_thenCreateCsvFile() {
        FILE_WRITER = new FileWriter(BASE_FILEPATH);
        FILE_WRITER.write(VALID_JSON_ARRAY, FILE_WRITER_FILENAME);

        File gameweekFile = new File(EXPECTED_FILEPATH);
        assertTrue(gameweekFile.exists());
        assertTrue(readDataFromFile(EXPECTED_FILEPATH).similar(VALID_JSON_ARRAY));
    }

    @Test(expected = RuntimeException.class)
    public void givenNullJSONArrayAndValidFilePath_write_thenThrowRuntimeException() {
        FILE_WRITER = new FileWriter(BASE_FILEPATH);
        FILE_WRITER.write(null, FILE_WRITER_FILENAME);
    }

    @Test(expected = RuntimeException.class)
    public void givenValidJSONArrayAndNullFilePath_write_thenThrowRuntimeException() {
        FILE_WRITER = new FileWriter(BASE_FILEPATH);
        FILE_WRITER.write(VALID_JSON_ARRAY, null);
    }

    @Test(expected = RuntimeException.class)
    public void givenNullJSONArrayAndNullFilePath_write_thenThrowRuntimeException() {
        FILE_WRITER = new FileWriter(BASE_FILEPATH);
        FILE_WRITER.write(null, null);
    }
}
