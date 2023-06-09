package org.tai.fpl.connectors;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class UrlConnector {
    private final HttpURLConnection connection;
    private final int responseCode;

    public UrlConnector(URL url) throws IOException {
        this.connection = (HttpURLConnection) url.openConnection();
        this.connection.setRequestMethod("GET");
        this.responseCode = connection.getResponseCode();
    }

    public String getResponseString() throws IOException {
        if (this.responseCode != 200) {
            throw new IOException(String.format("HttpResponseCode: {%s} - {%s}", this.responseCode, this.connection.getURL()));
        } else {
            return readStringFromUrl();
        }
    }

    private String readStringFromUrl() throws IOException {
        String inputLine;
        BufferedReader bufferedReader =
                new BufferedReader(new InputStreamReader(this.connection.getInputStream()));
        StringBuilder responseString = new StringBuilder();

        while ((inputLine = bufferedReader.readLine()) != null) {
            responseString.append(inputLine);
        }
        bufferedReader.close();

        return String.valueOf(responseString);
    }
}
