package org.tai.fpl.providers.impl;

import org.tai.fpl.providers.Provider;
import org.json.JSONArray;

public class ElementProvider implements Provider {
    private final JSONArray elementArray;

    public ElementProvider(JSONArray elementArray) {
        this.elementArray = elementArray;
    }

    public JSONArray getData() {
        return elementArray;
    }
}
