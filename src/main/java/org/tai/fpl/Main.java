package org.tai.fpl;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.json.JSONArray;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONException;
import org.json.JSONObject;
import org.tai.fpl.gameweek.Gameweek;
import org.tai.fpl.providers.impl.ElementProvider;
import org.tai.fpl.providers.impl.EventProvider;
import org.tai.fpl.providers.impl.TeamProvider;
import org.tai.fpl.providers.util.constants.JsonKeys;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

import static org.tai.fpl.Getters.createJSONObject;
import static org.tai.fpl.understat.Understat.*;
import static org.tai.fpl.writers.FileWriter.writeData;

public class Main {
    private static final Logger LOGGER = LogManager.getLogger(Main.class);

    private static final String TARGET_URL = "https://fantasy.premierleague.com/api/bootstrap-static/";
    private static final String SEASON = "2022-23";

    private static final String BASE_FILENAME = String.format("data/%s/", SEASON);
    private static final String PLAYERS_RAW_FILENAME = String.format("%splayers_raw.csv", BASE_FILENAME);
    private static final String TEAMS_FILENAME = String.format("%steams.csv", BASE_FILENAME);
    private static final String PLAYER_ID_FILENAME = String.format("%splayer_idlist.csv", BASE_FILENAME);
    private static final String GAMEWEEK_FILENAME = String.format("%sgws/gw", BASE_FILENAME);
    private static final String UNDERSTAT_TEAMS_FILENAME = String.format("%sunderstat/teams/", BASE_FILENAME);
    private static final String UNDERSTAT_PLAYERS_FILENAME = String.format("%sunderstat/players/", BASE_FILENAME);

    /* Will clean up main method later */
    public static void main(String[] args) {
        DataExtractor dataExtractor = new DataExtractor(TARGET_URL);
        JSONObject data = dataExtractor.getJsonFromUrl();

        JSONArray players = new JSONArray();
        int currentGameweekNumber = 0;
        Map<Integer, String> teams = new HashMap<>();
        try {
            ElementProvider elementProvider = new ElementProvider(data.getJSONArray((JsonKeys.ELEMENTS)));
            players = elementProvider.getPlayers();

            EventProvider eventProvider = new EventProvider(data.getJSONArray((JsonKeys.EVENTS)));
            currentGameweekNumber = eventProvider.getCurrentGameweek();

            TeamProvider teamProvider = new TeamProvider(data.getJSONArray((JsonKeys.TEAMS)));
            teams = teamProvider.getTeams();

            writeData(elementProvider.getData(), PLAYERS_RAW_FILENAME);
            writeData(teamProvider.getData(), TEAMS_FILENAME);
            writeData(players, PLAYER_ID_FILENAME);
        } catch(IllegalArgumentException | JSONException illegalArgumentException) {
            if (illegalArgumentException instanceof JSONException) {
                LOGGER.error("Error parsing JSON data using Provider classes: " + illegalArgumentException.getMessage());
            } else {
                LOGGER.error("IllegalArgumentException: " + illegalArgumentException.getMessage());
            }
        }

        Gameweek gameweekProvider = new Gameweek(currentGameweekNumber, players, teams);
        JSONArray currentGameweekData = gameweekProvider.getCurrentGameweekData();
        writeData(currentGameweekData, String.format("%s%s.csv", GAMEWEEK_FILENAME, currentGameweekNumber));

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
        writeData(allGameweeks, String.format("%sgws/merged_gw.csv", BASE_FILENAME));

        getTeamData(UNDERSTAT_TEAMS_FILENAME);
        getPlayerData(UNDERSTAT_PLAYERS_FILENAME);
    }
}
