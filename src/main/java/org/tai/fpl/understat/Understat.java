package org.tai.fpl.understat;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.tai.fpl.util.constants.FileNames;
import org.tai.fpl.writers.FileWriter;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Understat {
    private static final Logger LOGGER = LogManager.getLogger(Understat.class);

    private static final String TARGET_URL = "https://understat.com/league/EPL/2022";
    private static final String TARGET_PLAYER_URL = "https://understat.com/player/";

    private static final String TEAMS_DATA_VAR = "var teamsData";
    private static final String PLAYERS_DATA_VAR = "var playersData";
    private static final String MATCHES_DATA_VAR = "var matchesData";

    private final FileWriter fileWriter;

    public Understat(FileWriter fileWriter) {
        this.fileWriter = fileWriter;
    }

    public void getTeamData() {
        try {
            JSONObject teamsData = getJsonObject(TARGET_URL, TEAMS_DATA_VAR);
            teamsData.keySet().forEach(keyStr ->
            {
                JSONObject teamData = teamsData.getJSONObject(keyStr);
                String teamName = teamData.getString("title");
                JSONArray teamHistory = teamData.getJSONArray("history");
                try {
                    this.fileWriter.writeDataToSeasonPath(teamHistory, String.format("%s%s.csv", FileNames.UNDERSTAT_TEAMS_FILENAME, teamName));
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });
        } catch(IOException ioException) {
            if (ioException instanceof UnsupportedEncodingException) {
                LOGGER.error("Error encoding hex data to Json: " + ioException.getMessage());
            } else {
                LOGGER.error("Error getting player understat data: " + ioException.getMessage());
            }
        }
    }

    public void getPlayerData() {
        try {
            JSONArray playerData = getJsonArray(TARGET_URL, PLAYERS_DATA_VAR);
            for (int i = 0; i < playerData.length(); i++) {
                int playerId = playerData.getJSONObject(i).getInt("id");
                String playerName = playerData.getJSONObject(i).getString("player_name");
                JSONArray playerMatchesData = getJsonArray(String.format("%s%s", TARGET_PLAYER_URL, playerId), MATCHES_DATA_VAR);
                JSONArray playerMatchesDataWithName = addPlayerName(playerMatchesData, playerName);
                this.fileWriter.writeDataToSeasonPath(playerMatchesDataWithName, String.format("%s%s.csv", FileNames.UNDERSTAT_PLAYERS_FILENAME, playerName));
            }
        } catch(IOException ioException) {
            if (ioException instanceof UnsupportedEncodingException) {
                LOGGER.error("Error encoding hex data to Json: " + ioException.getMessage());
            } else {
                LOGGER.error("Error getting player understat data: " + ioException.getMessage());
            }
        }
    }

    private static String extractScriptTag(Elements scriptTags, String targetVar) {
        String scriptTagString = "";
        for (Element script : scriptTags) {
            String scriptHtml = script.html();
            Pattern pattern = Pattern.compile("/\\*(.*?)\\*/", Pattern.DOTALL);
            Matcher matcher = pattern.matcher(scriptHtml);
            scriptHtml = matcher.replaceAll("");
            if (scriptHtml.contains(targetVar)) {
                scriptTagString = scriptHtml;
            }
        }
        return scriptTagString;
    }

    private static String cleanScriptTag(String rawDataString) {
        String cleanedDataString = "";
        Pattern pattern = Pattern.compile("\\(\\'(.*?)\\'\\)");
        Matcher matcher = pattern.matcher(rawDataString);
        while (matcher.find()) {
            cleanedDataString = matcher.group(1);
        }
        return cleanedDataString;
    }

    private static JSONObject hexToJsonObject(String hexString) throws UnsupportedEncodingException {
        String jsonString = hexString.replaceAll("\\\\x", "%"); // Convert the hex string to a regular string
        jsonString = java.net.URLDecoder.decode(jsonString, "UTF-8"); // Decode the URL-encoded characters
        return new JSONObject(jsonString); // Parse the JSON string into a JSONObject
    }

    private static JSONArray hexToJsonArray(String hexString) throws UnsupportedEncodingException {
        String jsonString = hexString.replaceAll("\\\\x", "%"); // Convert the hex string to a regular string
        jsonString = java.net.URLDecoder.decode(jsonString, "UTF-8"); // Decode the URL-encoded characters
        return new JSONArray(jsonString); // Parse the JSON string into a JSONObject
    }

    private static JSONObject getJsonObject(String url, String targetVar) throws IOException {
        Document doc = Jsoup.connect(url).get();
        Elements scriptTags = doc.select("script");

        String rawDataString = extractScriptTag(scriptTags, targetVar);
        String cleanedDataString = cleanScriptTag(rawDataString);
        return hexToJsonObject(cleanedDataString);
    }

    private static JSONArray getJsonArray(String url, String targetVar) throws IOException {
        Document doc = Jsoup.connect(url).get();
        Elements scriptTags = doc.select("script");

        String rawDataString = extractScriptTag(scriptTags, targetVar);
        String cleanedDataString = cleanScriptTag(rawDataString);
        return hexToJsonArray(cleanedDataString);
    }

    private static JSONArray addPlayerName(JSONArray playerData, String playerName) {
        for (int i = 0; i < playerData.length(); i++) {
            JSONObject matchesData = playerData.getJSONObject(i);
            matchesData.put("name", playerName);
        }
        return playerData;
    }
}
