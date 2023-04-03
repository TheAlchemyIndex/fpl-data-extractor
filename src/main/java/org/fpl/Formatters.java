package org.fpl;

import org.json.JSONArray;

import java.util.Map;

import static org.fpl.Getters.getTeams;

public class Formatters {

    public static String formatPosition(int positionId) {
        switch (positionId) {
            case 1:
                return "GK";
            case 2:
                return "DEF";
            case 3:
                return "MID";
            case 4:
                return "FWD";
            default:
                return "N/A";
        }
    }

    public static String formatTeam(int teamId, JSONArray teams) {
        Map<Integer, String> parsedTeams = getTeams(teams);
        return parsedTeams.get(teamId);
    }
}
