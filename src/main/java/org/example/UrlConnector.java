package org.example;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

public class UrlConnector implements Connector<HttpURLConnection> {
    private final HttpURLConnection connection;
    private final int responseCode;

    public UrlConnector(URL url) throws IOException {
        this.connection = (HttpURLConnection) url.openConnection();
        this.connection.setRequestMethod("GET");
        this.responseCode = connection.getResponseCode();
    }

    public HttpURLConnection getConnection() {
        return this.connection;
    }

    public int getResponseCode() {
        return this.responseCode;
    }
}
