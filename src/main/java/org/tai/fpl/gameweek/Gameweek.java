package org.tai.fpl.gameweek;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONException;
import org.tai.fpl.Main;
import org.tai.fpl.parsers.JsonParser;
import org.tai.fpl.connectors.UrlConnector;
import org.tai.fpl.providers.util.constants.GameweekColumns;
import org.tai.fpl.providers.util.constants.JsonKeys;
import org.tai.fpl.providers.util.constants.PlayerColumns;
import org.tai.fpl.providers.util.constants.PlayerPositions;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

import static org.tai.fpl.gameweek.NameFormatter.formatName;

public class Gameweek {
    private static final Logger LOGGER = LogManager.getLogger(Main.class);
    private static final String TARGET_URL = "https://fantasy.premierleague.com/api/element-summary/";

    private final int gameweekNumber;
    private final JSONArray players;
    private final Map<Integer, String> teams;

    public Gameweek(int gameweekNumber, JSONArray players, Map<Integer, String> teams) {
        this.gameweekNumber = gameweekNumber;
        this.players = players;
        this.teams = teams;
    }

    public JSONArray getCurrentGameweekData() {
        JSONArray currentGameweekData = new JSONArray();
        for (int i = 0; i < this.players.length(); i++) {
            JSONObject player = this.players.getJSONObject(i);
            Map<String, Object> playerIdentifiers = getPlayerAttributes(player);
            Integer playerId = (Integer) playerIdentifiers.get(PlayerColumns.ID);
            JSONArray playerGameweekHistory = getPlayerGameweekHistory(playerId);

            JSONObject playerGameweekData;
            for (int j = 0; j < playerGameweekHistory.length(); j++) {
                JSONObject gameweek = playerGameweekHistory.getJSONObject(j);
                if (gameweek.getInt(GameweekColumns.ROUND) == this.gameweekNumber) {
                    playerGameweekData = gameweek;
                    String opponentTeam = formatTeam(playerGameweekData.getInt("opponent_team"));
                    playerGameweekData.put("opponent_team", opponentTeam);
                    playerGameweekData.put(PlayerColumns.NAME, playerIdentifiers.get(PlayerColumns.NAME));
                    playerGameweekData.put(PlayerColumns.POSITION, playerIdentifiers.get(PlayerColumns.POSITION));
                    playerGameweekData.put(PlayerColumns.TEAM, playerIdentifiers.get(PlayerColumns.TEAM));
                    playerGameweekData.put(PlayerColumns.XP, playerIdentifiers.get(PlayerColumns.XP));
                    currentGameweekData.put(playerGameweekData);
                }
            }
        }
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

    /* Will be used later */
    private Map<String, Object> formatDate(String dateString) {
        Map<String, Object> dateTimeMap = new HashMap<>();
        LocalDateTime localDateTime = LocalDateTime.parse(dateString, DateTimeFormatter.ISO_DATE_TIME);

        dateTimeMap.put("year", localDateTime.getYear());
        dateTimeMap.put("month", localDateTime.getMonthValue());
        dateTimeMap.put("day", localDateTime.getDayOfMonth());
        dateTimeMap.put("hour", localDateTime.getHour());
        dateTimeMap.put("minute", localDateTime.getMinute());
        dateTimeMap.put("second", localDateTime.getSecond());

        return dateTimeMap;
    }

    private JSONArray getPlayerGameweekHistory(Integer playerId) {
        JSONArray playerGameweekHistory = new JSONArray();
        String url = String.format("%s%s/", TARGET_URL, playerId);

        try {
            UrlConnector urlConnector = new UrlConnector(new URL(url));
            JsonParser jsonParser = new JsonParser(urlConnector.getResponseString());
            JSONObject playerData = jsonParser.parseJsonObject();
            playerGameweekHistory = playerData.getJSONArray((JsonKeys.HISTORY));
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
        return playerGameweekHistory;
    }
}
