package org.tai.fpl;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.tai.fpl.connectors.UrlConnector;
import org.tai.fpl.parsers.JsonParser;
import org.tai.fpl.providers.impl.ElementProvider;
import org.tai.fpl.providers.impl.EventProvider;
import org.tai.fpl.gameweek.Gameweek;
import org.tai.fpl.providers.impl.TeamProvider;
import org.tai.fpl.providers.util.constants.JsonKeys;
import org.json.JSONArray;
import org.json.JSONObject;
import org.tai.fpl.writers.FileWriter;

import java.io.*;
import java.net.URL;
import java.util.Map;

import static org.tai.fpl.Getters.*;

public class Main {

    private static final String TARGET_URL = "https://fantasy.premierleague.com/api/bootstrap-static/";
    private static final String SEASON = "2022-23";

    private static final String BASE_FILENAME = String.format("data/%s/", SEASON);
    private static final String PLAYERS_RAW_FILENAME = String.format("%splayer_idlist.csv", BASE_FILENAME);
    private static final String TEAMS_FILENAME = String.format("%steams.csv", BASE_FILENAME);
    private static final String PLAYER_ID_FILENAME = String.format("%splayer_idlist.csv", BASE_FILENAME);
    private static final String GAMEWEEK_FILENAME = String.format("%sgws/gw", BASE_FILENAME);

    /* Will fix exception handling later */
    public static void main(String[] args) throws IOException {

        UrlConnector urlConnector = new UrlConnector(new URL(TARGET_URL));
        JsonParser jsonParser = new JsonParser(urlConnector.getResponseString());
        JSONObject data = jsonParser.parseJsonObject();

        ElementProvider elementProvider = new ElementProvider(data.getJSONArray((JsonKeys.ELEMENTS)));
        JSONArray players = elementProvider.getPlayers();

        EventProvider eventProvider = new EventProvider(data.getJSONArray((JsonKeys.EVENTS)));
        int currentGameweekNumber = eventProvider.getCurrentGameweek();

        TeamProvider teamProvider = new TeamProvider(data.getJSONArray((JsonKeys.TEAMS)));
        Map<Integer, String> teams = teamProvider.getTeams();

        Gameweek gameweekProvider = new Gameweek(currentGameweekNumber, players, teams);
        JSONArray currentGameweekData = gameweekProvider.getCurrentGameweekData();

        FileWriter.writeData(elementProvider.getData(), PLAYERS_RAW_FILENAME);
        FileWriter.writeData(teamProvider.getData(), TEAMS_FILENAME);
        FileWriter.writeData(players, PLAYER_ID_FILENAME);
        FileWriter.writeData(currentGameweekData, String.format("%s%s.csv", GAMEWEEK_FILENAME, currentGameweekNumber));

        /* Not efficient, will change later */
        JSONArray allGameweeks = new JSONArray();
        for (int i = 1; i <= currentGameweekNumber; i++) {
            try {
                BufferedReader br = new BufferedReader(new FileReader(String.format("%sgws/gw%s.csv",
                        BASE_FILENAME, i)));
                CSVParser parser = CSVFormat.DEFAULT.withDelimiter(',').withHeader().parse(br);
                for(CSVRecord record : parser) {
                    JSONObject gameweek = createJSONObject(record);
                    allGameweeks.put(gameweek);
                }
            } catch (Exception e) {
                System.out.println(e);
            }
        }
        FileWriter.writeData(allGameweeks, String.format("%sgws/merged_gw.csv", BASE_FILENAME));
    }
}
