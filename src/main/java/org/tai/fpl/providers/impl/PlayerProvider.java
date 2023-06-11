package org.tai.fpl.providers.impl;

import org.json.JSONArray;
import org.json.JSONObject;
import org.tai.fpl.providers.Provider;
import org.tai.fpl.providers.util.constants.PlayerColumns;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PlayerProvider implements Provider {
    private final JSONArray playerArray;
    private static final List<String> PLAYER_HEADERS = new ArrayList<>(Arrays.asList(PlayerColumns.FIRST_NAME,
            PlayerColumns.SECOND_NAME, PlayerColumns.WEB_NAME, PlayerColumns.ID, PlayerColumns.ELEMENT_TYPE,
            PlayerColumns.TEAM, PlayerColumns.EP_THIS));

    public PlayerProvider(JSONArray playerArray) {
        this.playerArray = playerArray;
    }

    public JSONArray getData() {
        JSONArray playersArray = new JSONArray();

        for (int i = 0; i < this.playerArray.length(); i++) {
            JSONObject element = this.playerArray.getJSONObject(i);
            JSONObject player = new JSONObject();
            for (String header : PLAYER_HEADERS) {
                player.put(header, element.get(header));
            }
            playersArray.put(player);
        }
        return playersArray;
    }
}
