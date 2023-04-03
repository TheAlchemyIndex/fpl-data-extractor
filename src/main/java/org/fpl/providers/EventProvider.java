package org.fpl.providers;

import org.json.JSONArray;
import org.json.JSONObject;

public class EventProvider {
    private final JSONArray eventsArray;

    public EventProvider(JSONArray eventsArray) {
        this.eventsArray = eventsArray;
    }

    public JSONArray getData() {
        return this.eventsArray;
    }

    public int getCurrentGameweek() {
        int gameweek = 0;

        for (int i = 0; i < this.eventsArray.length(); i++) {
            JSONObject event = this.eventsArray.getJSONObject(i);
            if (event.getBoolean("is_current")) {
                gameweek = event.getInt("id");
            };
        }
        return gameweek;
    }
}
