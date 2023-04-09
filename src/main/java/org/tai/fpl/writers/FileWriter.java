package org.tai.fpl.writers;

import org.apache.commons.io.FileUtils;
import org.json.CDL;
import org.json.JSONArray;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;

public class FileWriter {

    public static void writeData(JSONArray elements, String baseFileName) {
        try {
            File file = new File(baseFileName);
            String csvString = CDL.toString(elements);
            FileUtils.writeStringToFile(file, csvString, Charset.defaultCharset());
        }
        catch (IOException ioException) {
            String exceptionMessage = String.format("Error extracting raw player data to file: %s", ioException.getMessage());
            System.out.println(exceptionMessage);
        }
    }
}
