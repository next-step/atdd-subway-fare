package nextstep.subway.domain;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

public class SubwayDispatchTime {
    private final LocalTime startTime;
    private final LocalTime endTime;

    public SubwayDispatchTime(LocalTime startTime, LocalTime endTime) {
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public LocalDateTime takeOnce(LocalTime takeTime, List<Integer> durations) {
        LocalDateTime ongoingDateTime = takeDateTime(takeTime);
        for (int eachDuration : durations) {
            ongoingDateTime = takeOnce(ongoingDateTime, eachDuration);
        }
        return ongoingDateTime;
    }

    private LocalDateTime takeDateTime(LocalTime takeTime) {
        if (takeTime.isBefore(startTime)) {
            return today(startTime);
        }
        if (takeTime.isAfter(endTime)) {
            return nextDay(startTime);
        }
        return today(takeTime);
    }

    private LocalDateTime takeOnce(LocalDateTime ongoingDateTime, int duration) {
        LocalDateTime arrivalDateTime = ongoingDateTime.plusMinutes(duration);
        if (arrivalDateTime.toLocalTime().isAfter(endTime)) {
            return nextDay(startTime).plusMinutes(duration);
        }
        return arrivalDateTime;
    }

    private LocalDateTime nextDay(LocalTime localTime) {
        return today(localTime).plusDays(1);
    }

    private LocalDateTime today(LocalTime localTime) {
        return LocalDateTime.of(LocalDate.now(), localTime);
    }

}
