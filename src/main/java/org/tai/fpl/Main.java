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
import org.tai.fpl.util.constants.FileNames;
import org.tai.fpl.writers.FileWriter;

import java.util.Map;

public class Main {

    public static void main(String[] args) {
        FplConfig config = new FplConfig("src/main/resources/config.properties");
        final String mainSeason = config.getMainSeason();
        final int startingSeasonStart = config.getStartingSeasonStart();
        final int startingSeasonEnd = config.getStartingSeasonEnd();
        final int finalSeasonEnd = config.getFinalSeasonEnd();
        final String baseFilePath = config.getBaseFilePath();
        final String mainUrl = config.getMainUrl();
        final String gameweekUrl = config.getGameweekUrl();
        final String seasonFilePath = config.getSeasonFilePath();

        FileWriter fileWriter = new FileWriter(baseFilePath, mainSeason);

        FplDataExtractor fplDataExtractor = new FplDataExtractor(mainUrl);
        JSONObject fplData = fplDataExtractor.getData();

        TeamProvider teamProvider = new TeamProvider(fplData.getJSONArray(("teams")));
        Map<Integer, String> teams = teamProvider.getTeams();
        fileWriter.writeDataToSeasonPath(teamProvider.getData(), FileNames.TEAMS_FILENAME);

        GameweekExtractor gameweekExtractor = new GameweekExtractor(fplData, gameweekUrl, teams, mainSeason, fileWriter);
        gameweekExtractor.getGameweekData();
        int currentGameweekNumber = gameweekExtractor.getCurrentGameweekNumber();

        GameweekJoiner gameweekJoiner = new GameweekJoiner(currentGameweekNumber);
        gameweekJoiner.joinGameweeks(fileWriter, seasonFilePath, "gws/merged_gw.csv");

        SeasonJoiner seasonJoiner = new SeasonJoiner(startingSeasonStart, startingSeasonEnd, 2023);
        seasonJoiner.joinSeasons(fileWriter, baseFilePath, String.format("%s-%s seasons.csv", startingSeasonStart, finalSeasonEnd));

        FixtureExtractor fixturesExtractor = new FixtureExtractor(fileWriter, teams);
        fixturesExtractor.getFixtures();

        /* Joiner classes are all simular, will fix and remove duplication later */
        FixtureJoiner fixtureJoiner = new FixtureJoiner(startingSeasonStart, startingSeasonEnd, 2023);
        fixtureJoiner.joinFixtureData(fileWriter, baseFilePath, String.format("Fixtures - %s-%s seasons.csv", startingSeasonStart, finalSeasonEnd));
    }
}
