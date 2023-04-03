package org.fpl;

import org.apache.commons.io.FileUtils;
import org.json.CDL;
import org.json.JSONArray;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;

public class Writer {

    public static void writeData(JSONArray elements, String baseFileName) {
        try {
            String fileName = baseFileName;
            File file = new File(fileName);
            String csvString = CDL.toString(elements);
            FileUtils.writeStringToFile(file, csvString, Charset.defaultCharset());
        }
        catch (IOException ioException) {
            String exceptionMessage = String.format("Error extracting raw player data to file: %s", ioException.getMessage());
            System.out.println(exceptionMessage);
        }
    }
}
