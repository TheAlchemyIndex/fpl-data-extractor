<div align="center">
  <h3 align="center">FPL Data Extractor</h3>

  <p align="center">
    A data extraction and pipeline project written in Java, which extracts data from the Fantasy Premier League API 
    and writes it to csv files.
  </p>
</div>

<!-- ABOUT THE PROJECT -->
## About The Project

The Fantasy Premier League API provides plenty of data regarding the FPL game, mainly on a gameweek and season basis.
</br>
This project will need to be run on a weekly basis if you want to collect regular data for each gameweek, as there is 
no way to backfill data for previous gameweeks or seasons (if I am wrong and there is a way to do this, please let me know).
</br>
Data is provided within this project for previous gameweeks and seasons if you are interested (credit goes to 
[vaastav](https://github.com/vaastav), as this project is a Java 
recreation of their Python based [Fantasy-Premier-League](https://github.com/vaastav/Fantasy-Premier-League) project).
</br>
### General Information
#### Urls

[https://fantasy.premierleague.com/api/bootstrap-static/](https://fantasy.premierleague.com/api/bootstrap-static/)
</br>

This endpoint returns general information about the FPL game divided into these sections:

* events: Basic information of every Gameweek such as average score, highest score, top scoring player, most captained, etc.
* game_settings: The game settings and rules.
* phases: Phases of FPL season.
* teams: Basic information of current Premier League clubs.
* total_players: Total FPL players.
* elements: Information of all Premier League players including points, status, value, match stats (goals, assists, etc.), ICT index, etc.
* element_types: Basic information about player’s position (GK, DEF, MID, FWD).
</br></br>

#### Player’s Detailed Data

[https://fantasy.premierleague.com/api/element-summary/{element_id}](https://fantasy.premierleague.com/api/element-summary/)

This endpoint returns a player’s detailed information divided into 3 section:

* fixtures: A list of player’s remaining fixtures of the season.
* history: A list of player’s previous fixtures and its match stats.
* history_past: A list of player’s previous seasons and its seasonal stats.
</br></br>

#### Fixtures

[https://fantasy.premierleague.com/api/fixtures/](https://fantasy.premierleague.com/api/fixtures/)

This endpoint returns a JSON array which contains every fixture of the season. To get fixtures for specific Gameweek, 
you can add a parameter after the endpoint path (ex: fixtures?event=7). You can also request only the upcoming fixtures 
using future parameter (ex: fixtures?future=1). If you set the future value to 0, you will get all fixtures, but if 1 
you will only get the upcoming fixtures.

Here’s an explanation of some of the JSON elements:

* event refers to the event id in events section of the bootstrap-static data.
* team_a and team_h refers to the team id in teams section of the bootstrap-static data. team_a for the away team and 
* team_h for the home team.
* team_h_difficulty and team_a_difficulty is the FDR value calculated by FPL.
* stats contains a list of match facts that affect points of a player. It consists of goals_scored, assists, own_goals, 
* penalties_saved, penalties_missed, yellow_cards, red_cards, saves, bonus, and bps data. The JSON structure can be seen in image below.
* value is the amount and element refers to the element id in elements section of the bootstrap-static data.

(Credit for the above explanation goes to [Frenzel Timothy](https://medium.com/@frenzelts?source=post_page-----acbd5598eb19--------------------------------) - [Fantasy Premier League API Endpoints: A Detailed Guide](https://medium.com/@frenzelts/fantasy-premier-league-api-endpoints-a-detailed-guide-acbd5598eb19)).

<!-- GETTING STARTED -->
## Getting Started

### Prerequisites

* Java - This project has been built and tested using Java 17
* Maven - This project has been built and tested using Maven 3.9.1

<!-- USAGE EXAMPLES -->
## Usage

After ensuring that Java and Maven are installed, clone this repo and run the main method in Main.java.
</br>
The config parameters for this project can be accessed in src/main/resources/config.properties. Set the MAIN_SEASON
variable to the target season of your choice (e.g., 2022-23). Setting the other season variables to the dates that
you wish will enable csv files to be created that contain data for all seasons within the season range that you specify
joined together (defaults are currently set to 2019 - 2023, data for those seasons must already have been extracted to
relevant files for this to work).

<!-- LICENSE -->
## License

Distributed under the MIT License. See `LICENSE` for more information.

<!-- CONTACT -->
## Contact

TheAlchemyIndex - [LinkedIn](https://www.linkedin.com/in/vaughana)

<!-- ACKNOWLEDGMENTS -->
## Acknowledgments

* [Fantasy-Premier-League - vaastav](https://github.com/vaastav/Fantasy-Premier-League)
* [Best-README-Template - othneildrew](https://github.com/othneildrew/Best-README-Template)
* [Fantasy Premier League API Endpoints: A Detailed Guide - Frenzel Timothy](https://medium.com/@frenzelts/fantasy-premier-league-api-endpoints-a-detailed-guide-acbd5598eb19)
