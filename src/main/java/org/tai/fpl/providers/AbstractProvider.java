package org.tai.fpl.providers;

import org.json.JSONArray;

public abstract class AbstractProvider {
    private final JSONArray dataArray;

    protected AbstractProvider(JSONArray dataArray) {
        if (dataArray == null) {
            throw new IllegalArgumentException(String.format("%s can not be initialised with null. " +
                    "Please initialise with a valid JSONArray.", this.getClass().getSimpleName()));
        }
        this.dataArray = dataArray;
    }

    public JSONArray getData() {
        return dataArray;
    }
}
