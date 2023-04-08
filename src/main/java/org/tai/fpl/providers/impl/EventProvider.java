package org.tai.fpl.providers.impl;

import org.tai.fpl.providers.AbstractProvider;
import org.json.JSONArray;
import org.json.JSONObject;

public class EventProvider extends AbstractProvider {

    public EventProvider(JSONArray dataArray) {
        super(dataArray);
    }

    public int getCurrentGameweek() {
        int gameweek = 0;

        for (int i = 0; i < this.getData().length(); i++) {
            JSONObject event = this.getData().getJSONObject(i);
            if (event.getBoolean("is_current")) {
                gameweek = event.getInt("id");
            };
        }
        return gameweek;
    }
}
