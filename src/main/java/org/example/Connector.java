package org.example;

public interface Connector<T> {
    T getConnection();
    int getResponseCode();
}
