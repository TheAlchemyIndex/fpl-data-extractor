package org.tae.fpl.providers.impl;

import org.tae.fpl.providers.AbstractProvider;
import org.json.JSONArray;
import org.json.JSONObject;

public class EventProvider extends AbstractProvider {

    public EventProvider(JSONArray dataArray) {
        super(dataArray);
    }

    @Override
    public JSONArray getData() {
        return this.dataArray;
    }

    public int getCurrentGameweek() {
        int gameweek = 0;

        for (int i = 0; i < this.dataArray.length(); i++) {
            JSONObject event = this.dataArray.getJSONObject(i);
            if (event.getBoolean("is_current")) {
                gameweek = event.getInt("id");
            };
        }
        return gameweek;
    }
}
