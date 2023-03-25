package org.example;

import org.json.JSONArray;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

public class GameWeek {
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
    private static final LocalDateTime TODAY = LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES);

    public static int getRecentGameweekId(JSONArray elements) {


        int gameWeekId = 0;
        for (int i = 0; i < elements.length(); i++) {
            LocalDateTime formattedDeadlineDate = formatDeadlineDate(elements.getJSONObject(i)
                    .getString("deadline_time"));

            if (formattedDeadlineDate.isAfter(TODAY)) {
                gameWeekId = elements.getJSONObject(i).getInt("id") - 1;
                break;
            } else {
                gameWeekId = elements.getJSONObject(elements.length() - 1).getInt("id");
            }
        }
        return gameWeekId;
    }

    private static LocalDateTime formatDeadlineDate(String deadLineDate) {
        return LocalDateTime.parse(deadLineDate.substring(0, 16), FORMATTER);
    }
}
