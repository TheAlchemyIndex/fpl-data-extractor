package org.tai.fpl.connectors;

import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

import static org.junit.Assert.assertThrows;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;

public class UrlConnectorTest {
        
    @Test
    public void testGetResponseString_Success() throws IOException {
        String expectedResponse = "Test Response";
        byte[] expectedResponseBytes = expectedResponse.getBytes();

        try (ByteArrayInputStream inputStream = new ByteArrayInputStream(expectedResponseBytes)) {
            URL mockUrl = mock(URL.class);
            HttpURLConnection mockHttpUrlConnection = mock(HttpURLConnection.class);
            when(mockHttpUrlConnection.getResponseCode()).thenReturn(200);
            when(mockUrl.openConnection()).thenReturn(mockHttpUrlConnection);
            when(mockHttpUrlConnection.getInputStream()).thenReturn(inputStream);

            UrlConnector urlConnector = new UrlConnector(mockUrl);
            String responseString = urlConnector.getResponseString();

            assertEquals(expectedResponse, responseString);

            verify(mockHttpUrlConnection).setRequestMethod("GET");
            verify(mockHttpUrlConnection).getResponseCode();
            verify(mockHttpUrlConnection).getInputStream();
            verify(mockUrl).openConnection();
        }
    }

    @Test
    public void testGetResponseString_Exception() throws IOException {
        URL mockUrl = mock(URL.class);
        HttpURLConnection mockHttpUrlConnection = mock(HttpURLConnection.class);
        when(mockHttpUrlConnection.getResponseCode()).thenReturn(404);
        when(mockUrl.openConnection()).thenReturn(mockHttpUrlConnection);

        UrlConnector urlConnector = new UrlConnector(mockUrl);

        assertThrows(IOException.class, urlConnector::getResponseString);

        verify(mockHttpUrlConnection).setRequestMethod("GET");
        verify(mockHttpUrlConnection).getResponseCode();
        verify(mockUrl).openConnection();
    }
}
