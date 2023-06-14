package org.tai.fpl.joiners;

import org.tai.fpl.writers.FileWriter;

public interface Joiner {
    void join(FileWriter fileWriter, String baseFilePath, String subFilePath);
}
