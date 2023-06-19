package org.tai.fpl.extractors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.tai.fpl.providers.impl.GameweekProvider;
import org.tai.fpl.providers.impl.PlayerProvider;
import org.tai.fpl.util.constants.FileNames;
import org.tai.fpl.writers.FileWriter;

import java.util.Map;

public class GameweekExtractor {
    private static final Logger LOGGER = LogManager.getLogger(GameweekExtractor.class);
    private final JSONObject fplData;
    private final String gameweekUrl;
    private final Map<Integer, String> teams;
    private final String season;
    private final FileWriter fileWriter;

    public GameweekExtractor(JSONObject fplData, String gameweekUrl, Map<Integer, String> teams, String season,
                             FileWriter fileWriter) {
        this.fplData = fplData;
        this.gameweekUrl = gameweekUrl;
        this.teams = teams;
        this.season = season;
        this.fileWriter = fileWriter;
    }

    public void getGameweekData() {
        try {
            JSONArray elements = this.fplData.getJSONArray("elements");
            PlayerProvider playerProvider = new PlayerProvider(elements);
            JSONArray players = playerProvider.getData();
            int currentGameweekNumber = getCurrentGameweekNumber();
            LOGGER.info("Current gameweek number: {} - {} season.", currentGameweekNumber, this.season);

            GameweekProvider gameweekProvider = new GameweekProvider(currentGameweekNumber, gameweekUrl, players,
                    teams, season);
            JSONArray currentGameweekData = gameweekProvider.getData();

            writeDataToFile(elements, FileNames.PLAYERS_RAW_FILENAME);
            writeDataToFile(players, FileNames.PLAYER_ID_FILENAME);
            writeDataToFile(currentGameweekData, String.format(FileNames.GAMEWEEK_FILENAME, currentGameweekNumber));
        } catch (JSONException | IllegalArgumentException e) {
            throw new RuntimeException("Error parsing JSON data using Provider classes: " + e.getMessage(), e);
        }
    }

    private void writeDataToFile(JSONArray data, String filename) {
        String filepath = String.format("%s/%s", this.season, filename);
        fileWriter.write(data, filepath);
    }

    public int getCurrentGameweekNumber() {
        JSONArray events = this.fplData.getJSONArray("events");
        int currentGameweekNumber = 0;

        for (int i = 0; i < events.length(); i++) {
            JSONObject event = events.getJSONObject(i);
            if (event.getBoolean("is_current")) {
                currentGameweekNumber = event.getInt("id");
                break;
            }
        }
        return currentGameweekNumber;
    }
}
