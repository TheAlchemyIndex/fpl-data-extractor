package org.fpl.providers;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class TeamProvider {
    private final JSONArray teamsArray;

    public TeamProvider(JSONArray teamsArray) {
        this.teamsArray = teamsArray;
    }

    public JSONArray getData() {
        return this.teamsArray;
    }

    public Map<Integer, String> getTeams() {
        Map<Integer, String> teamsMap = new HashMap<>();

        for (int i = 0; i < this.teamsArray.length(); i++) {
            JSONObject team = this.teamsArray.getJSONObject(i);
            teamsMap.put(team.getInt("id"), team.get("name").toString());
        }
        return teamsMap;
    }
}
