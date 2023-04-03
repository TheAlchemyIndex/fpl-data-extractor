package org.fpl.providers;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ElementProvider {
    private final JSONArray elementsArray;

    private static final List<String> PLAYER_HEADERS = new ArrayList<>(Arrays.asList(PlayerHeaders.FIRST_NAME,
            PlayerHeaders.SECOND_NAME, PlayerHeaders.ID, PlayerHeaders.ELEMENT_TYPE, PlayerHeaders.TEAM,
            PlayerHeaders.EXPECTED_POINTS));

    public ElementProvider(JSONArray elementsArray) {
        this.elementsArray = elementsArray;
    }

    public JSONArray getData() {
        return this.elementsArray;
    }

    public JSONArray getPlayers() {
        JSONArray playersArray = new JSONArray();

        for (int i = 0; i < this.elementsArray.length(); i++) {
            JSONObject element = this.elementsArray.getJSONObject(i);
            JSONObject player = new JSONObject();
            for (String header : PLAYER_HEADERS) {
                player.put(header, element.get(header));
            }
            playersArray.put(player);
        }
        return playersArray;
    }
}
