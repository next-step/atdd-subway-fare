package nextstep.subway.utils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class StringDateTimeConverter {

    private static final int HOUR_END_INDEX = 2;
    private static final int MINUTE_START_INDEX = 2;

    public static LocalDateTime convertLineTimeToDateTime(LocalDateTime findTime, String time) {
        return LocalDateTime.of(findTime.getYear(), findTime.getMonth(), findTime.getDayOfMonth()
            , Integer.parseInt(time.substring(0,HOUR_END_INDEX))
            , Integer.parseInt(time.substring(MINUTE_START_INDEX)));
    }

    public static LocalDateTime convertStringToDateTime(String dateString) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmm");
        return LocalDateTime.parse(dateString, formatter);
    }

    public static String convertDateTimeToString(LocalDateTime localDateTime) {
        return localDateTime.format(DateTimeFormatter.ofPattern("yyyyMMddHHmm"));
    }

}
