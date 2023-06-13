package org.tai.fpl.config;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class FplConfig {
    private static final Logger LOGGER = LogManager.getLogger(FplConfig.class);
    private final String mainSeason;
    private final int startingSeasonStart;
    private final int startingSeasonEnd;
    private final int finalSeasonEnd;
    private final String baseFilePath;
    private final String mainUrl;
    private final String gameweekUrl;

    public FplConfig(String configFilePath) {
        try {
            FileInputStream propsInput = new FileInputStream(configFilePath);
            Properties prop = new Properties();
            prop.load(propsInput);

            this.mainSeason = prop.getProperty("MAIN_SEASON");
            this.startingSeasonStart = Integer.parseInt(prop.getProperty("STARTING_SEASON_START"));
            this.startingSeasonEnd = Integer.parseInt(prop.getProperty("STARTING_SEASON_END"));
            this.finalSeasonEnd = Integer.parseInt(prop.getProperty("FINAL_SEASON_END"));
            this.baseFilePath = prop.getProperty("BASE_FILEPATH");
            this.mainUrl = prop.getProperty("MAIN_URL");
            this.gameweekUrl = prop.getProperty("GAMEWEEK_URL");
            LOGGER.info("Config file successfully loaded.");
        } catch(IOException fileNotFoundException) {
            throw new RuntimeException(String.format("Error loading config file: {%s}", fileNotFoundException.getMessage()));
        }
    }

    public String getMainSeason() {
        return mainSeason;
    }

    public int getStartingSeasonStart() {
        return startingSeasonStart;
    }

    public int getStartingSeasonEnd() {
        return startingSeasonEnd;
    }

    public int getFinalSeasonEnd() {
        return finalSeasonEnd;
    }

    public String getBaseFilePath() {
        return baseFilePath;
    }

    public String getMainUrl() {
        return mainUrl;
    }

    public String getGameweekUrl() {
        return gameweekUrl;
    }

    public String getSeasonFilePath() {
        return String.format("%s%s/", this.baseFilePath, this.mainSeason);
    }
}
