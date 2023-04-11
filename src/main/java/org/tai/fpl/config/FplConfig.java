package org.tai.fpl.config;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class FplConfig {
    private String fullSeason;
    private String baseFilePath;

    public FplConfig() {
        try {
            String configFilePath = "src/main/resources/config.properties";
            FileInputStream propsInput = new FileInputStream(configFilePath);
            Properties prop = new Properties();
            prop.load(propsInput);

            this.fullSeason = prop.getProperty("FULL_SEASON");
            this.baseFilePath = String.format("%s%s/", prop.getProperty("BASE_FILEPATH"), this.fullSeason);
        } catch(IOException fileNotFoundException) {
            System.out.println(fileNotFoundException.getMessage());
        }
    }

    public String getFullSeason() {
        return fullSeason;
    }

    public String getBaseFilePath() {
        return baseFilePath;
    }
}
