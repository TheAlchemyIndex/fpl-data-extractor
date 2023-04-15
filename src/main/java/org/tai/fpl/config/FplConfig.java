package org.tai.fpl.config;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class FplConfig {
    private static final Logger LOGGER = LogManager.getLogger(FplConfig.class);
    private String fullSeason;
    private String baseFilePath;

    public FplConfig() {
        try {
            String configFilePath = "src/main/resources/config.properties";
            FileInputStream propsInput = new FileInputStream(configFilePath);
            Properties prop = new Properties();
            prop.load(propsInput);

            this.fullSeason = prop.getProperty("FULL_SEASON");
            this.baseFilePath = prop.getProperty("BASE_FILEPATH");
        } catch(IOException fileNotFoundException) {
            LOGGER.error("Error loading config file: " + fileNotFoundException.getMessage());
        }
    }

    public String getFullSeason() {
        return fullSeason;
    }

    public String getBaseFilePath() {
        return baseFilePath;
    }
}
