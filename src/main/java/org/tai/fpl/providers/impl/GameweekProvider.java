package org.tai.fpl.providers.impl;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONException;
import org.tai.fpl.util.parsers.JsonParser;
import org.tai.fpl.connectors.UrlConnector;
import org.tai.fpl.providers.Provider;
import org.tai.fpl.providers.util.constants.GameweekColumns;
import org.tai.fpl.providers.util.constants.PlayerColumns;
import org.tai.fpl.providers.util.constants.PlayerPositions;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import static org.tai.fpl.providers.util.formatters.GameweekNameFormatter.formatName;

public class GameweekProvider implements Provider {
    private static final Logger LOGGER = LogManager.getLogger(GameweekProvider.class);
    private final int gameweekNumber;
    private final String gameweekUrl;
    private final JSONArray players;
    private final Map<Integer, String> teams;
    private final String season;

    public GameweekProvider(int gameweekNumber, String gameweekUrl, JSONArray players, Map<Integer, String> teams, String season) {
        this.gameweekNumber = gameweekNumber;
        this.gameweekUrl = gameweekUrl;
        this.players = players;
        this.teams = teams;
        this.season = season;
    }

    public JSONArray getData() {
        JSONArray currentGameweekData = new JSONArray();

        for (int i = 0; i < this.players.length(); i++) {
            JSONObject player = this.players.getJSONObject(i);
            Map<String, Object> playerAttributes = getPlayerAttributes(player);
            Integer playerId = (Integer) playerAttributes.get(PlayerColumns.ID);
            JSONArray playerGameweekHistory = getPlayerGameweekHistory(playerId);

            for (int j = 0; j < playerGameweekHistory.length(); j++) {
                JSONObject gameweek = playerGameweekHistory.getJSONObject(j);
                if (gameweek.getInt(GameweekColumns.ROUND) == this.gameweekNumber) {
                    JSONObject playerGameweekData = getPlayerGameweekData(gameweek, playerAttributes);
                    currentGameweekData.put(playerGameweekData);
                }
            }
        }
        LOGGER.info("Current gameweek data extraction complete.");
        return currentGameweekData;
    }

    private Map<String, Object> getPlayerAttributes(JSONObject player) {
        Map<String, Object> playerAttributes = new HashMap<>();

        String name = formatName(String.format("%s %s", player.getString(PlayerColumns.FIRST_NAME),
                player.getString(PlayerColumns.SECOND_NAME)));
        Integer id = player.getInt(PlayerColumns.ID);
        String position = formatPosition(player.getInt(PlayerColumns.ELEMENT_TYPE));
        String team = formatTeam(player.getInt(PlayerColumns.TEAM));
        Double expectedPoints = player.getDouble(PlayerColumns.EP_THIS);

        playerAttributes.put(PlayerColumns.NAME, name);
        playerAttributes.put(PlayerColumns.ID, id);
        playerAttributes.put(PlayerColumns.POSITION, position);
        playerAttributes.put(PlayerColumns.TEAM, team);
        playerAttributes.put(PlayerColumns.XP, expectedPoints);

        return playerAttributes;
    }

    private String formatPosition(int positionId) {
        return switch (positionId) {
            case 1 -> PlayerPositions.KEEPER;
            case 2 -> PlayerPositions.DEFENDER;
            case 3 -> PlayerPositions.MIDFIELDER;
            case 4 -> PlayerPositions.FORWARD;
            default -> "N/A";
        };
    }

    private String formatTeam(int teamId) {
        return this.teams.get(teamId);
    }

    private JSONArray getPlayerGameweekHistory(Integer playerId) {
        JSONArray playerGameweekHistory;
        String url = String.format("%s%s/", this.gameweekUrl, playerId);

        try {
            UrlConnector urlConnector = new UrlConnector(new URL(url));
            JsonParser jsonParser = new JsonParser(urlConnector.getResponseString());
            JSONObject playerData = jsonParser.parseJsonObject();
            playerGameweekHistory = playerData.getJSONArray(("history"));
            return playerGameweekHistory;
        } catch(MalformedURLException malformedURLException) {
            throw new RuntimeException(String.format("Invalid gameweek url provided {%s}: %s", this.gameweekUrl,
                    malformedURLException.getMessage()));
        } catch(IOException ioException) {
            throw new RuntimeException(String.format("Error connecting to the provided gameweek url {%s}: %s", this.gameweekUrl,
                    ioException.getMessage()));
        } catch(RuntimeException runtimeException) {
            if (runtimeException instanceof JSONException) {
                throw new RuntimeException(String.format("Error parsing JSON data using JsonParser: %s",
                        runtimeException.getMessage()));
            } else {
                throw new RuntimeException(String.format("Error connecting to the provided gameweek url {%s}: %s", this.gameweekUrl,
                        runtimeException.getMessage()));
            }
        }
    }

    private JSONObject getPlayerGameweekData(JSONObject gameweek, Map<String, Object> playerAttributes) {
        JSONObject playerGameweekData = gameweek;

        String opponentTeam = formatTeam(playerGameweekData.getInt(PlayerColumns.OPPONENT_TEAM));
        playerGameweekData.put(PlayerColumns.OPPONENT_TEAM, opponentTeam);
        playerGameweekData.put(PlayerColumns.NAME, playerAttributes.get(PlayerColumns.NAME));
        playerGameweekData.put(PlayerColumns.POSITION, playerAttributes.get(PlayerColumns.POSITION));
        playerGameweekData.put(PlayerColumns.TEAM, playerAttributes.get(PlayerColumns.TEAM));
        playerGameweekData.put(PlayerColumns.XP, playerAttributes.get(PlayerColumns.XP));
        playerGameweekData.put(PlayerColumns.SEASON, this.season);

        return playerGameweekData;
    }
}
