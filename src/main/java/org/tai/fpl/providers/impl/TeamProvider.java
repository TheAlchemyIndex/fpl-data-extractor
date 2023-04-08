package org.tai.fpl.providers.impl;

import org.tai.fpl.providers.AbstractProvider;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class TeamProvider extends AbstractProvider {

    public TeamProvider(JSONArray dataArray) {
        super(dataArray);
    }

    @Override
    public JSONArray getData() {
        return this.dataArray;
    }

    public Map<Integer, String> getTeams() {
        Map<Integer, String> teamsMap = new HashMap<>();

        for (int i = 0; i < this.dataArray.length(); i++) {
            JSONObject team = this.dataArray.getJSONObject(i);
            teamsMap.put(team.getInt("id"), team.get("name").toString());
        }
        return teamsMap;
    }
}
