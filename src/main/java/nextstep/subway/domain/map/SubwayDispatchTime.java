package nextstep.subway.domain.map;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

public class SubwayDispatchTime {
    private final LocalTime startTime;
    private final LocalTime endTime;
    private final LocalTime intervalTime;

    public SubwayDispatchTime(LocalTime startTime, LocalTime endTime, LocalTime intervalTime) {
        this.startTime = startTime;
        this.endTime = endTime;
        this.intervalTime = intervalTime;
    }

    public LocalDateTime findArrivalTime(LocalDateTime takeTime, List<Integer> durations) {
        LocalDateTime ongoingDateTime = takableTime(takeTime);
        for (int eachDuration : durations) {
            ongoingDateTime = takeTrain(ongoingDateTime, eachDuration);
        }
        return ongoingDateTime;
    }

    private LocalDateTime takableTime(LocalDateTime takeTime) {
        final LocalDateTime takableTime = takableTimestampByInterval(takeTime);
        return takableTimeByStartEnd(takableTime);
    }

    private LocalDateTime takableTimestampByInterval(LocalDateTime takeTime) {
        long takeTimestamp = localTimeToTimestamp(takeTime.toLocalTime());
        long intervalTimsStamp = localTimeToTimestamp(intervalTime);
        long waitingTimestamp = takeTimestamp % intervalTimsStamp;
        if (waitingTimestamp == 0) {
            return takeTime;
        }
        return takeTime.plusSeconds(waitingTimestamp / 1000);
    }

    private long localTimeToTimestamp(LocalTime time) {
        return TimeUnit.MILLISECONDS.convert(
            time.getHour() * 60 + time.getMinute(), TimeUnit.MINUTES
        );
    }

    private LocalDateTime takableTimeByStartEnd(LocalDateTime takableTime) {
        LocalDate takeDay = takableTime.toLocalDate();
        LocalDateTime todayStartTime = LocalDateTime.of(takeDay, startTime);
        if (takableTime.isBefore(todayStartTime)) {
            return todayStartTime;
        }
        LocalDateTime todayEndTime = LocalDateTime.of(takeDay, endTime);
        if (takableTime.isAfter(todayEndTime)) {
            LocalDate nextDay = takeDay.plusDays(1);
            return LocalDateTime.of(nextDay, startTime);
        }
        return takableTime;
    }

    private LocalDateTime takeTrain(LocalDateTime ongoingDateTime, int duration) {
        LocalDateTime arrivalDateTime = ongoingDateTime.plusMinutes(duration);
        if (arrivalDateTime.toLocalTime().isAfter(endTime)) {
            LocalDate nextDay = arrivalDateTime.toLocalDate().plusDays(1);
            return LocalDateTime.of(nextDay, startTime)
                                .plusMinutes(duration);
        }
        return arrivalDateTime;
    }
}
