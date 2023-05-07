package org.tai.fpl.providers.impl;

import org.tai.fpl.providers.AbstractProvider;
import org.tai.fpl.providers.util.constants.PlayerColumns;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ElementProvider extends AbstractProvider {

    private static final List<String> PLAYER_HEADERS = new ArrayList<>(Arrays.asList(PlayerColumns.FIRST_NAME,
            PlayerColumns.SECOND_NAME, PlayerColumns.WEB_NAME, PlayerColumns.ID, PlayerColumns.ELEMENT_TYPE,
            PlayerColumns.TEAM, PlayerColumns.EP_THIS));

    public ElementProvider(JSONArray dataArray) {
        super(dataArray);
    }

    public JSONArray getPlayers() {
        JSONArray playersArray = new JSONArray();

        for (int i = 0; i < this.getData().length(); i++) {
            JSONObject element = this.getData().getJSONObject(i);
            JSONObject player = new JSONObject();
            for (String header : PLAYER_HEADERS) {
                player.put(header, element.get(header));
            }
            playersArray.put(player);
        }
        return playersArray;
    }
}
