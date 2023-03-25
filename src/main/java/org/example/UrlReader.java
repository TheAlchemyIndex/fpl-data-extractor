package org.example;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class UrlReader {

    public static String readData(UrlConnector urlConnector) throws IOException {

        if (urlConnector.getResponseCode() != 200) {
            throw new RuntimeException("HttpResponseCode: " + urlConnector.getResponseCode());
        } else {
            String inputLine;
            BufferedReader bufferedReader =
                    new BufferedReader(new InputStreamReader(urlConnector.getConnection().getInputStream()));
            StringBuilder responseString = new StringBuilder();

            while ((inputLine = bufferedReader.readLine()) != null) {
                responseString.append(inputLine);
            }
            bufferedReader.close();

            return String.valueOf(responseString);
        }
    }
}
