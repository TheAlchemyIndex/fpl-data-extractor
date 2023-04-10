package org.tai.fpl.understat;

import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Understat {
    private static final String TARGET_URL = "https://understat.com/league/EPL/2022";
    private static final String TARGET_PLAYER_URL = "https://understat.com/player/";

    private static final String TEAMS_DATA_VAR = "var teamsData";
    private static final String PLAYERS_DATA_VAR = "var playersData";
    private static final String MATCHES_DATA_VAR = "var matchesData";

    public static JSONObject getTeamData() throws IOException {
        return getJsonObject(TARGET_URL, TEAMS_DATA_VAR);
    }

    public static JSONArray getPlayerData() throws IOException {
        return getJsonArray(TARGET_URL, PLAYERS_DATA_VAR);
    }

    public static JSONArray getPlayerMatchesData(int playerId) throws IOException {
        return getJsonArray(String.format("%s%s", TARGET_PLAYER_URL, playerId), MATCHES_DATA_VAR);
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
}
