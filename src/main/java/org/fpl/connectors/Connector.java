package org.fpl.connectors;

public interface Connector<T> {
    T getConnection();
    int getResponseCode();
}
