package org.tai.fpl.providers;

import org.json.JSONArray;

public abstract class AbstractProvider {
    protected final JSONArray dataArray;

    protected AbstractProvider(JSONArray dataArray) {
        this.dataArray = dataArray;
    }

    public abstract JSONArray getData();
}
