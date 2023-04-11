package org.tai.fpl;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.json.JSONArray;
import org.json.JSONObject;
import org.tai.fpl.config.FplConfig;
import org.tai.fpl.extractors.DataExtractor;
import org.tai.fpl.extractors.GameweekExtractor;
import org.tai.fpl.writers.FileWriter;

import java.io.*;

import static org.tai.fpl.Getters.createJSONObject;
import static org.tai.fpl.understat.Understat.*;

public class Main {
    private static final String TARGET_URL = "https://fantasy.premierleague.com/api/bootstrap-static/";

    public static void main(String[] args) {
        FplConfig config = new FplConfig();
        final String season = config.getFullSeason();
        final String baseFilePath = config.getBaseFilePath();

        FileWriter fileWriter = new FileWriter(baseFilePath);

        DataExtractor dataExtractor = new DataExtractor(TARGET_URL);
        JSONObject data = dataExtractor.getJsonFromUrl();

        GameweekExtractor gameweekExtractor = new GameweekExtractor(data);
        gameweekExtractor.getGameweekData(fileWriter);
        int currentGameweekNumber = gameweekExtractor.getCurrentGameweekNumber();

        /* Not efficient, will change later */
        JSONArray allGameweeks = new JSONArray();
        for (int i = 1; i <= currentGameweekNumber; i++) {
            try {
                BufferedReader br = new BufferedReader(new FileReader(String.format("%sgws/gw%s.csv",
                        baseFilePath, i)));
                CSVParser parser = CSVFormat.DEFAULT.withDelimiter(',').withHeader().parse(br);
                for(CSVRecord record : parser) {
                    JSONObject gameweek = createJSONObject(record);
                    allGameweeks.put(gameweek);
                }
            } catch (Exception e) {
                System.out.println(e);
            }
        }
        fileWriter.writeData(allGameweeks, "gws/merged_gw.csv");

        getTeamData(season, baseFilePath);
        getPlayerData(season, baseFilePath);
    }
}
