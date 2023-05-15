package org.tai.fpl;

import org.json.JSONObject;
import org.tai.fpl.config.FplConfig;
import org.tai.fpl.extractors.DataExtractor;
import org.tai.fpl.extractors.GameweekExtractor;
import org.tai.fpl.joiners.GameweekJoiner;
import org.tai.fpl.joiners.SeasonJoiner;
import org.tai.fpl.joiners.UnderstatJoiner;
import org.tai.fpl.understat.Understat;
import org.tai.fpl.writers.FileWriter;

public class Main {
    private static final String TARGET_URL = "https://fantasy.premierleague.com/api/bootstrap-static/";

    public static void main(String[] args) {
        FplConfig config = new FplConfig("src/main/resources/config.properties");
        final String season = config.getSeason();
        final String baseFilePath = config.getBaseFilePath();
        final String seasonFilePath = String.format("%s%s/", baseFilePath, season);

        FileWriter fileWriter = new FileWriter(baseFilePath, season);

        DataExtractor dataExtractor = new DataExtractor(TARGET_URL);
        JSONObject data = dataExtractor.getJsonFromUrl();

        GameweekExtractor gameweekExtractor = new GameweekExtractor(data, season);
        gameweekExtractor.getGameweekData(fileWriter);
        int currentGameweekNumber = gameweekExtractor.getCurrentGameweekNumber();

        GameweekJoiner gameweekJoiner = new GameweekJoiner(currentGameweekNumber);
        gameweekJoiner.joinGameweeks(fileWriter, seasonFilePath, "gws/merged_gw.csv");

        SeasonJoiner seasonJoiner = new SeasonJoiner(2019, 2020, 2023);
        seasonJoiner.joinSeasons(fileWriter, baseFilePath, String.format("%s-%s seasons.csv", 2019, 23));

        Understat understat = new Understat(fileWriter, season);
        understat.getTeamData();
        understat.getPlayerData();

        UnderstatJoiner understatJoiner = new UnderstatJoiner(2019, 2020, 2023);
        understatJoiner.joinPlayerData(fileWriter, baseFilePath, String.format("Understat - %s-%s seasons.csv", 2019, 23));
    }
}
