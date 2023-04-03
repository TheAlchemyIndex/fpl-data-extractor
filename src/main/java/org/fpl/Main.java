package org.fpl;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.fpl.connectors.UrlConnector;
import org.fpl.providers.ElementProvider;
import org.fpl.providers.EventProvider;
import org.fpl.providers.TeamProvider;
import org.json.JSONArray;
import org.json.JSONObject;
import java.io.*;
import java.net.URL;

import static org.fpl.Getters.*;
import static org.fpl.Writer.writeData;

public class Main {

    private static final String TARGET_URL = "https://fantasy.premierleague.com/api/bootstrap-static/";
    private static final String SEASON = "2022-23";
    private static final String BASE_FILENAME = String.format("data/%s/", SEASON);

    public static void main(String[] args) throws IOException {

        UrlReader urlReader = new UrlReader(new UrlConnector(new URL(TARGET_URL)));
        JSONObject data = urlReader.parseJSONObject();

        ElementProvider elements = new ElementProvider(data.getJSONArray(("elements")));
        JSONArray players = elements.getPlayers();

        EventProvider events = new EventProvider(data.getJSONArray(("events")));
        int currentGameweekNumber = events.getCurrentGameweek();

        TeamProvider teams = new TeamProvider(data.getJSONArray(("teams")));
        JSONArray currentGameweekData = getCurrentGameweekData(players, teams.getData(), currentGameweekNumber);

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

        writeData(elements.getData(), String.format("%splayers_raw.csv", BASE_FILENAME));
        writeData(teams.getData(), String.format("%steams.csv", BASE_FILENAME));
        writeData(players, String.format("%splayer_idlist.csv", BASE_FILENAME));
        writeData(currentGameweekData, String.format("%sgws/gw%s.csv", BASE_FILENAME, currentGameweekNumber));
        writeData(allGameweeks, String.format("%sgws/merged_gw.csv", BASE_FILENAME));
    }
}