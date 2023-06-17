package org.tai.fpl;

import org.json.JSONObject;
import org.tai.fpl.config.FplConfig;
import org.tai.fpl.extractors.FplDataExtractor;
import org.tai.fpl.extractors.GameweekExtractor;
import org.tai.fpl.extractors.FixtureExtractor;
import org.tai.fpl.joiners.impl.FixtureJoiner;
import org.tai.fpl.joiners.impl.GameweekJoiner;
import org.tai.fpl.joiners.impl.SeasonJoiner;
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

        FileWriter fileWriter = new FileWriter(baseFilePath);

        FplDataExtractor fplDataExtractor = new FplDataExtractor(mainUrl);
        JSONObject fplData = fplDataExtractor.getData();

        TeamProvider teamProvider = new TeamProvider(fplData.getJSONArray(("teams")));
        Map<Integer, String> teams = teamProvider.getTeams();
        fileWriter.write(teamProvider.getData(), String.format("%s/%s", mainSeason, FileNames.TEAMS_FILENAME));

        GameweekExtractor gameweekExtractor = new GameweekExtractor(fplData, gameweekUrl, teams, mainSeason, fileWriter);
        gameweekExtractor.getGameweekData();
        int currentGameweekNumber = gameweekExtractor.getCurrentGameweekNumber();

        FixtureExtractor fixturesExtractor = new FixtureExtractor(mainSeason, fileWriter, teams);
        fixturesExtractor.getFixtures();

        GameweekJoiner gameweekJoiner = new GameweekJoiner(currentGameweekNumber, mainSeason, fileWriter);
        gameweekJoiner.join();

        SeasonJoiner seasonJoiner = new SeasonJoiner(startingSeasonStart, startingSeasonEnd, finalSeasonEnd,
                baseFilePath, fileWriter);
        seasonJoiner.join();

        FixtureJoiner fixtureJoiner = new FixtureJoiner(startingSeasonStart, startingSeasonEnd, finalSeasonEnd,
                baseFilePath, fileWriter);
        fixtureJoiner.join();
    }
}
