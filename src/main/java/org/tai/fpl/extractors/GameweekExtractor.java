package org.tai.fpl.extractors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.tai.fpl.gameweek.Gameweek;
import org.tai.fpl.providers.impl.ElementProvider;
import org.tai.fpl.providers.impl.EventProvider;
import org.tai.fpl.providers.impl.PlayerProvider;
import org.tai.fpl.providers.util.constants.JsonKeys;
import org.tai.fpl.util.constants.FileNames;
import org.tai.fpl.writers.FileWriter;

import java.io.IOException;
import java.util.Map;

public class GameweekExtractor {
    private static final Logger LOGGER = LogManager.getLogger(GameweekExtractor.class);
    private final JSONObject jsonData;
    private final Map<Integer, String> teams;
    private final String season;

    public GameweekExtractor(JSONObject jsonData, Map<Integer, String> teams, String season) throws IllegalArgumentException {
        if (jsonData == null) {
            throw new IllegalArgumentException("GameweekExtractor initialised with a null value.");
        }
        this.jsonData = jsonData;
        this.teams = teams;
        this.season = season;
    }

    public void getGameweekData(FileWriter fileWriter) {
        if (fileWriter == null) {
            throw new IllegalArgumentException("null value passed for fileWriter parameter.");
        }

        try {
            ElementProvider elementProvider = new ElementProvider(this.jsonData.getJSONArray((JsonKeys.ELEMENTS)));
            PlayerProvider playerProvider = new PlayerProvider(elementProvider.getData());
            JSONArray players = playerProvider.getData();

            int currentGameweekNumber = getCurrentGameweekNumber();

            Gameweek gameweekProvider = new Gameweek(currentGameweekNumber, players, this.teams, this.season);
            JSONArray currentGameweekData = gameweekProvider.getCurrentGameweekData();

            fileWriter.writeDataToSeasonPath(elementProvider.getData(), FileNames.PLAYERS_RAW_FILENAME);
            fileWriter.writeDataToSeasonPath(players, FileNames.PLAYER_ID_FILENAME);
            fileWriter.writeDataToSeasonPath(currentGameweekData, String.format("%s%s.csv", FileNames.GAMEWEEK_FILENAME, currentGameweekNumber));
        } catch (IllegalArgumentException | JSONException illegalArgumentException) {
            if (illegalArgumentException instanceof JSONException) {
                LOGGER.error("Error parsing JSON data using Provider classes: " + illegalArgumentException.getMessage());
            } else {
                LOGGER.error("IllegalArgumentException: " + illegalArgumentException.getMessage());
            }
        } catch (IOException ioException) {
                LOGGER.error(String.format("Error writing data to file: %s", ioException.getMessage()));
        }
    }

    public int getCurrentGameweekNumber() {
        EventProvider eventProvider = new EventProvider(this.jsonData.getJSONArray((JsonKeys.EVENTS)));
        return eventProvider.getCurrentGameweek();
    }
}
