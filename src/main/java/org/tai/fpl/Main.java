package org.tai.fpl;

import org.json.JSONObject;
import org.tai.fpl.config.FplConfig;
import org.tai.fpl.extractors.FplDataExtractor;
import org.tai.fpl.extractors.GameweekExtractor;
import org.tai.fpl.fixtures.FixtureExtractor;
import org.tai.fpl.joiners.FixtureJoiner;
import org.tai.fpl.joiners.GameweekJoiner;
import org.tai.fpl.joiners.SeasonJoiner;
import org.tai.fpl.providers.impl.TeamProvider;
import org.tai.fpl.providers.util.constants.JsonKeys;
import org.tai.fpl.util.constants.FileNames;
import org.tai.fpl.writers.FileWriter;

import java.io.IOException;
import java.util.Map;

public class Main {
    private static final String TARGET_URL = "https://fantasy.premierleague.com/api/bootstrap-static/";

    public static void main(String[] args) {
        FplConfig config = new FplConfig("src/main/resources/config.properties");
        final String season = config.getSeason();
        final String baseFilePath = config.getBaseFilePath();
        final String seasonFilePath = String.format("%s%s/", baseFilePath, season);

        FileWriter fileWriter = new FileWriter(baseFilePath, season);

        FplDataExtractor fplDataExtractor = new FplDataExtractor(TARGET_URL);
        JSONObject data = fplDataExtractor.getData();

        TeamProvider teamProvider = new TeamProvider(data.getJSONArray((JsonKeys.TEAMS)));
        Map<Integer, String> teams = teamProvider.getTeams();
        try {
            fileWriter.writeDataToSeasonPath(teamProvider.getData(), FileNames.TEAMS_FILENAME);
        } catch (IOException ioException) {
            throw new RuntimeException(String.format("Error writing teams data to file: %s", ioException.getMessage()));
        }

        GameweekExtractor gameweekExtractor = new GameweekExtractor(data, teams, season);
        gameweekExtractor.getGameweekData(fileWriter);
        int currentGameweekNumber = gameweekExtractor.getCurrentGameweekNumber();

        GameweekJoiner gameweekJoiner = new GameweekJoiner(currentGameweekNumber);
        gameweekJoiner.joinGameweeks(fileWriter, seasonFilePath, "gws/merged_gw.csv");

        SeasonJoiner seasonJoiner = new SeasonJoiner(2019, 2020, 2023);
        seasonJoiner.joinSeasons(fileWriter, baseFilePath, String.format("%s-%s seasons.csv", 2019, 23));

        FixtureExtractor fixturesExtractor = new FixtureExtractor(fileWriter, teams);
        fixturesExtractor.getFixtures();

        /* Joiner classes are all simular, will fix and remove duplication later */
        FixtureJoiner fixtureJoiner = new FixtureJoiner(2019, 2020, 2023);
        fixtureJoiner.joinFixtureData(fileWriter, baseFilePath, String.format("Fixtures - %s-%s seasons.csv", 2019, 23));
    }
}
