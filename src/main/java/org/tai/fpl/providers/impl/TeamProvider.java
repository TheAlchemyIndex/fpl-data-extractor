package org.tai.fpl.providers.impl;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.tai.fpl.providers.Provider;

import java.util.HashMap;
import java.util.Map;

public class TeamProvider implements Provider {
    private final JSONArray teamArray;

    public TeamProvider(JSONArray teamArray) {
        this.teamArray = teamArray;
    }

    public JSONArray getData() {
        return teamArray;
    }

    public Map<Integer, String> getTeams() {
        Map<Integer, String> teamsMap = new HashMap<>();

        try {
            for (int i = 0; i < this.teamArray.length(); i++) {
                JSONObject team = this.teamArray.getJSONObject(i);
                teamsMap.put(team.getInt("id"), team.get("name").toString());
            }
            return teamsMap;
        } catch (JSONException jsonException) {
            throw new RuntimeException(String.format("Incorrect JSON format for teams data: {%s}",
                    jsonException.getMessage()));
        }
    }
}
