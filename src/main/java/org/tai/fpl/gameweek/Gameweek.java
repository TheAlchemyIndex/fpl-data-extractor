package org.tai.fpl.gameweek;

import org.tai.fpl.parsers.JsonParser;
import org.tai.fpl.connectors.UrlConnector;
import org.tai.fpl.providers.util.constants.GameweekColumns;
import org.tai.fpl.providers.util.constants.JsonKeys;
import org.tai.fpl.providers.util.constants.PlayerColumns;
import org.tai.fpl.providers.util.constants.PlayerPositions;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class Gameweek {
    private static final String TARGET_URL = "https://fantasy.premierleague.com/api/element-summary/";
    private final int gameweekNumber;
    private final JSONArray players;
    private final Map<Integer, String> teams;

    public Gameweek(int gameweekNumber, JSONArray players, Map<Integer, String> teams) {
        this.gameweekNumber = gameweekNumber;
        this.players = players;
        this.teams = teams;
    }

    public JSONArray getCurrentGameweekData() throws IOException {
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

        String name = String.format("%s %s", player.getString(PlayerColumns.FIRST_NAME),
                player.getString(PlayerColumns.SECOND_NAME));
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
        switch (positionId) {
            case 1:
                return PlayerPositions.KEEPER;
            case 2:
                return PlayerPositions.DEFENDER;
            case 3:
                return PlayerPositions.MIDFIELDER;
            case 4:
                return PlayerPositions.FORWARD;
            default:
                return "N/A";
        }
    }

    private String formatTeam(int teamId) {
        return this.teams.get(teamId);
    }

    private JSONArray getPlayerGameweekHistory(Integer playerId) throws IOException {
        String url = String.format("%s%s/", TARGET_URL, playerId);
        UrlConnector urlConnector = new UrlConnector(new URL(url));
        JsonParser jsonParser = new JsonParser(urlConnector.getResponseString());
        JSONObject playerData = jsonParser.parseJsonObject();
        return playerData.getJSONArray((JsonKeys.HISTORY));
    }
}