package org.tai.fpl.fixtures;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.tai.fpl.connectors.UrlConnector;
import org.tai.fpl.parsers.JsonParser;
import org.tai.fpl.providers.util.constants.GameweekColumns;
import org.tai.fpl.understat.Understat;
import org.tai.fpl.util.constants.FileNames;
import org.tai.fpl.writers.FileWriter;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;

public class FixtureExtractor {

    private static final Logger LOGGER = LogManager.getLogger(Understat.class);
    private static final String TARGET_URL = "https://fantasy.premierleague.com/api/fixtures/";

    private final FileWriter fileWriter;
    private final Map<Integer, String> teams;

    public FixtureExtractor(FileWriter fileWriter, Map<Integer, String> teams) {
        this.fileWriter = fileWriter;
        this.teams = teams;
    }

    public void getFixtures() {
        try {
            UrlConnector urlConnector = new UrlConnector(new URL(TARGET_URL));
            JsonParser jsonParser = new JsonParser(urlConnector.getResponseString());
            JSONArray fixtureData = jsonParser.parseJsonArray();

            for (int i = 0; i < fixtureData.length(); i++) {
                JSONObject fixture = fixtureData.getJSONObject(i);
                String homeTeam = formatTeam(fixture.getInt("team_h"));
                String awayTeam = formatTeam(fixture.getInt("team_a"));
                fixture.remove("team_h");
                fixture.remove("team_a");
                fixture.put("home_team", homeTeam);
                fixture.put("away_team", awayTeam);
                fixtureData.put(i, fixture);
            }

            this.fileWriter.writeDataToSeasonPath(fixtureData, FileNames.FIXTURES_FILENAME);
        } catch(MalformedURLException malformedURLException) {
            LOGGER.error("Invalid target url provided: " + malformedURLException.getMessage());
        } catch(IOException ioException) {
            LOGGER.error("Error connecting to the provided target url: " + ioException.getMessage());
        } catch(RuntimeException runtimeException) {
            if (runtimeException instanceof JSONException) {
                LOGGER.error("Error parsing JSON data using JsonParser: " + runtimeException.getMessage());
            } else {
                LOGGER.error("Error connecting to the provided target url: " + runtimeException.getMessage());
            }
        }
    }

    private String formatTeam(int teamId) {
        return this.teams.get(teamId);
    }
}
