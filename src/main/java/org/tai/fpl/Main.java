package org.tai.fpl;

import org.json.JSONObject;
import org.tai.fpl.config.FplConfig;
import org.tai.fpl.extractors.DataExtractor;
import org.tai.fpl.extractors.GameweekExtractor;
import org.tai.fpl.joiners.GameweekJoiner;
import org.tai.fpl.joiners.SeasonJoiner;
import org.tai.fpl.writers.FileWriter;

import static org.tai.fpl.understat.Understat.getPlayerData;
import static org.tai.fpl.understat.Understat.getTeamData;

public class Main {
    private static final String TARGET_URL = "https://fantasy.premierleague.com/api/bootstrap-static/";

    public static void main(String[] args) {
        FplConfig config = new FplConfig();
        final String season = config.getFullSeason();
        final String baseFilePath = config.getBaseFilePath();
        final String seasonFilePath = String.format("%s%s/", baseFilePath, season);

        FileWriter fileWriter = new FileWriter(baseFilePath, season);

        DataExtractor dataExtractor = new DataExtractor(TARGET_URL);
        JSONObject data = dataExtractor.getJsonFromUrl();

        GameweekExtractor gameweekExtractor = new GameweekExtractor(data);
        gameweekExtractor.getGameweekData(fileWriter);
        int currentGameweekNumber = gameweekExtractor.getCurrentGameweekNumber();

        GameweekJoiner gameweekJoiner = new GameweekJoiner(currentGameweekNumber);
        fileWriter.writeDataGameweeks(gameweekJoiner.joinGameweeks(seasonFilePath), "gws/merged_gw.csv");

        SeasonJoiner seasonJoiner = new SeasonJoiner(2020, 21, 23);
        fileWriter.writeDataSeasons(seasonJoiner.joinSeasons(baseFilePath), String.format("%s-%s seasons.csv", 2020, 23));

        getTeamData(season, baseFilePath);
        getPlayerData(season, baseFilePath);
    }
}
