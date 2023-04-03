package org.fpl;

import org.apache.commons.csv.CSVRecord;
import org.fpl.connectors.UrlConnector;
import org.json.JSONArray;
import org.json.JSONObject;
import java.io.IOException;
import java.net.URL;
import java.util.*;

import static org.fpl.Formatters.formatPosition;
import static org.fpl.Formatters.formatTeam;

public class Getters {

    private static final String TARGET_URL = "https://fantasy.premierleague.com/api/element-summary/";

    public static Map<String, String> getFixtures(JSONArray fixtures, String teamType) {
        Map<String, String> fixturesMap = new HashMap<>();

        for (int i = 0; i < fixtures.length(); i++) {
            JSONObject fixture = fixtures.getJSONObject(i);
            fixturesMap.put(fixture.get("id").toString(), fixture.get(teamType).toString());
        }
        return fixturesMap;
    }

    public static Map<Integer, String> getTeams(JSONArray teams) {
        Map<Integer, String> teamsMap = new HashMap<>();

        for (int i = 0; i < teams.length(); i++) {
            JSONObject team = teams.getJSONObject(i);
            teamsMap.put(team.getInt("id"), team.get("name").toString());
        }
        return teamsMap;
    }

    public static JSONArray getCurrentGameweekData(JSONArray players, JSONArray teams, int currentGameweekNumber) throws IOException {
        JSONArray currentGameweekData = new JSONArray();

        for (int i = 0; i < players.length(); i++) {
            JSONObject player = players.getJSONObject(i);
            Map<String, Object> playerIdentifiers = getPlayerIdentifiers(player, teams);
            Integer playerId = (Integer) playerIdentifiers.get("id");
            JSONArray playerGameweekHistory = getPlayerGameweekHistory(playerId);

            JSONObject playerGameweekData;
            for (int j = 0; j < playerGameweekHistory.length(); j++) {
                JSONObject gameweek = playerGameweekHistory.getJSONObject(j);
                if (gameweek.getInt("round") == currentGameweekNumber) {
                    playerGameweekData = gameweek;
                    playerGameweekData.put("name", playerIdentifiers.get("name"));
                    playerGameweekData.put("position", playerIdentifiers.get("position"));
                    playerGameweekData.put("team", playerIdentifiers.get("team"));
                    playerGameweekData.put("xP", playerIdentifiers.get("xP"));
                    currentGameweekData.put(playerGameweekData);
                }
            }
        }
        return currentGameweekData;
    }

    public static Map<String, Object> getPlayerIdentifiers(JSONObject player, JSONArray teams) {
        Map<String, Object> playerIdentifiers = new HashMap<>();

        String name = String.format("%s %s", player.getString("first_name"), player.getString("second_name"));
        Integer id = player.getInt("id");
        String position = formatPosition(player.getInt("element_type"));
        String team = formatTeam(player.getInt("team"), teams);
        Double expectedPoints = player.getDouble("ep_this");

        playerIdentifiers.put("name", name);
        playerIdentifiers.put("id", id);
        playerIdentifiers.put("position", position);
        playerIdentifiers.put("team", team);
        playerIdentifiers.put("xP", expectedPoints);

        return playerIdentifiers;
    }

    public static JSONArray getPlayerGameweekHistory(Integer playerId) throws IOException {
        String url = String.format("%s%s/", TARGET_URL, playerId);
        UrlReader urlReader = new UrlReader(new UrlConnector(new URL(url)));
        JSONObject playerData = urlReader.parseJSONObject();
        return playerData.getJSONArray(("history"));
    }

    public static JSONObject createJSONObject(CSVRecord record) {
        JSONObject playerRecord = new JSONObject();

        playerRecord.put("name", record.get("name"));
        playerRecord.put("position", record.get("position"));
        playerRecord.put("team", record.get("team"));
        playerRecord.put("xP", record.get("xP"));
        playerRecord.put("assists", record.get("assists"));
        playerRecord.put("bonus", record.get("bonus"));
        playerRecord.put("bps", record.get("bps"));
        playerRecord.put("clean_sheets", record.get("clean_sheets"));
        playerRecord.put("creativity", record.get("creativity"));
        playerRecord.put("element", record.get("element"));
        playerRecord.put("expected_assists", record.get("expected_assists"));
        playerRecord.put("expected_goal_involvements", record.get("expected_goal_involvements"));
        playerRecord.put("expected_goals", record.get("expected_goals"));
        playerRecord.put("expected_goals_conceded", record.get("expected_goals_conceded"));
        playerRecord.put("fixture", record.get("fixture"));
        playerRecord.put("goals_conceded", record.get("goals_conceded"));
        playerRecord.put("goals_scored", record.get("goals_scored"));
        playerRecord.put("ict_index", record.get("ict_index"));
        playerRecord.put("influence", record.get("influence"));
        playerRecord.put("kickoff_time", record.get("kickoff_time"));
        playerRecord.put("minutes", record.get("minutes"));
        playerRecord.put("opponent_team", record.get("opponent_team"));
        playerRecord.put("own_goals", record.get("own_goals"));
        playerRecord.put("penalties_missed", record.get("penalties_missed"));
        playerRecord.put("penalties_saved", record.get("penalties_saved"));
        playerRecord.put("red_cards", record.get("red_cards"));
        playerRecord.put("round", record.get("round"));
        playerRecord.put("saves", record.get("saves"));
        playerRecord.put("selected", record.get("selected"));
        playerRecord.put("starts", record.get("starts"));
        playerRecord.put("team_a_score", record.get("team_a_score"));
        playerRecord.put("team_h_score", record.get("team_h_score"));
        playerRecord.put("threat", record.get("threat"));
        playerRecord.put("total_points", record.get("total_points"));
        playerRecord.put("transfers_balance", record.get("transfers_balance"));
        playerRecord.put("transfers_in", record.get("transfers_in"));
        playerRecord.put("transfers_out", record.get("transfers_out"));
        playerRecord.put("value", record.get("value"));
        playerRecord.put("was_home", record.get("was_home"));
        playerRecord.put("yellow_cards", record.get("yellow_cards"));

        return playerRecord;
    }
}
