package org.tai.fpl;

import org.json.JSONObject;
import org.tai.fpl.config.FplConfig;
import org.tai.fpl.extractors.DataExtractor;
import org.tai.fpl.extractors.GameweekExtractor;
import org.tai.fpl.gameweek.GameweekJoiner;
import org.tai.fpl.writers.FileWriter;

import java.io.*;

import static org.tai.fpl.understat.Understat.*;

public class Main {
    private static final String TARGET_URL = "https://fantasy.premierleague.com/api/bootstrap-static/";

    public static void main(String[] args) throws IOException {
        FplConfig config = new FplConfig();
        final String season = config.getFullSeason();
        final String baseFilePath = config.getBaseFilePath();

        FileWriter fileWriter = new FileWriter(baseFilePath);

        DataExtractor dataExtractor = new DataExtractor(TARGET_URL);
        JSONObject data = dataExtractor.getJsonFromUrl();

        GameweekExtractor gameweekExtractor = new GameweekExtractor(data);
        gameweekExtractor.getGameweekData(fileWriter);
        int currentGameweekNumber = gameweekExtractor.getCurrentGameweekNumber();

        GameweekJoiner merger = new GameweekJoiner(currentGameweekNumber);
        fileWriter.writeData(merger.joinGameweeks(baseFilePath), "gws/merged_gw.csv");

        getTeamData(season, baseFilePath);
        getPlayerData(season, baseFilePath);
    }
}
