package org.tai.fpl.providers.impl;

import org.json.JSONArray;
import org.json.JSONObject;
import org.tai.fpl.providers.Provider;

public class EventProvider implements Provider {
    private final JSONArray eventArray;

    public EventProvider(JSONArray eventArray) {
        this.eventArray = eventArray;
    }

    public JSONArray getData() {
        return eventArray;
    }

    public int getCurrentGameweek() {
        int gameweek = 0;

        for (int i = 0; i < this.eventArray.length(); i++) {
            JSONObject event = this.eventArray.getJSONObject(i);
            if (event.getBoolean("is_current")) {
                gameweek = event.getInt("id");
            };
        }
        return gameweek;
    }
}
