package org.tae.fpl;

import org.apache.commons.csv.CSVRecord;
import org.json.JSONObject;

public class Getters {

    /* TEMPORARY */

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
