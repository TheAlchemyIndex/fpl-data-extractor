package org.tai.fpl.extractors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.tai.fpl.connectors.UrlConnector;
import org.tai.fpl.util.parsers.JsonParser;
import org.tai.fpl.util.constants.FileNames;
import org.tai.fpl.writers.FileWriter;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;

public class FixtureExtractor {
    private static final Logger LOGGER = LogManager.getLogger(FixtureExtractor.class);

    private final String url;
    private final String season;
    private final FileWriter fileWriter;
    private final Map<Integer, String> teams;

    public FixtureExtractor(String url, String season, FileWriter fileWriter, Map<Integer, String> teams) {
        this.url = url;
        this.season = season;
        this.fileWriter = fileWriter;
        this.teams = teams;
    }

    public void getFixtures() {
        try {
            JSONArray fixtureData = parseFixtureData();

            JSONArray modifiedFixtures = new JSONArray();
            for (int i = 0; i < fixtureData.length(); i++) {
                JSONObject fixture = fixtureData.getJSONObject(i);
                String homeTeam = formatTeam(fixture.getInt("team_h"));
                String awayTeam = formatTeam(fixture.getInt("team_a"));
                fixture.remove("team_h");
                fixture.remove("team_a");
                fixture.put("home_team", homeTeam);
                fixture.put("away_team", awayTeam);
                modifiedFixtures.put(fixture);
            }

            writeFixtureData(modifiedFixtures);
            LOGGER.info("Data extraction from {} complete.", this.url);
        } catch (MalformedURLException malformedURLException) {
            LOGGER.error("Invalid target URL provided: {}", malformedURLException.getMessage());
        } catch (IOException ioException) {
            LOGGER.error("Error connecting to the provided target URL: {}", ioException.getMessage());
        } catch (JSONException jsonException) {
            LOGGER.error("Error parsing JSON data: {}", jsonException.getMessage());
        }
    }

    private JSONArray parseFixtureData() throws IOException {
        UrlConnector urlConnector = new UrlConnector(new URL(this.url));
        JsonParser jsonParser = new JsonParser(urlConnector.getResponseString());
        return jsonParser.parseJsonArray();
    }

    private void writeFixtureData(JSONArray fixtureData) {
        String filename = String.format("%s/%s", season, FileNames.FIXTURES_FILENAME);
        fileWriter.write(fixtureData, filename);
    }

    private String formatTeam(int teamId) {
        return teams.get(teamId);
    }
}
