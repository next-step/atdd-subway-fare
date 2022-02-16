package nextstep.subway.domain;

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

    public LocalDateTime takableTime(LocalTime takeTime) {
        long takableTimestamp = takableTimestamp(firstTakeDateTime(takeTime));
        LocalDateTime takableDateTime = LocalDateTime.ofInstant(
            Instant.ofEpochMilli(takableTimestamp), TimeZone.getDefault().toZoneId()
        );
        return takableDateTime;
    }

    private LocalDateTime firstTakeDateTime(LocalTime takeTime) {
        if (takeTime.isBefore(startTime)) {
            return today(startTime);
        }
        if (takeTime.isAfter(endTime)) {
            return nextDay(startTime);
        }
        return today(takeTime);
    }

    private long takableTimestamp(LocalDateTime firstTakeDateTime) {
        long takeTimestamp = Timestamp.valueOf(firstTakeDateTime).getTime();
        long intervalTimeStamp = intervalTimestamp();

        long takableTimestamp = takeTimestamp / intervalTimeStamp * intervalTimeStamp;
        if (isTrainNotReady(takeTimestamp, intervalTimeStamp)) {
            takableTimestamp += intervalTimeStamp;
        }
        return takableTimestamp;
    }

    private long intervalTimestamp() {
        return TimeUnit.MILLISECONDS.convert(
            intervalTime.getHour() + intervalTime.getMinute() * 60, TimeUnit.SECONDS
        );
    }

    private boolean isTrainNotReady(long takeTimestamp, long intervalTimeStamp) {
        return takeTimestamp % intervalTimeStamp > 0;
    }

    public LocalDateTime arrivalTime(LocalDateTime takeTime, List<Integer> durations) {
        LocalDateTime ongoingDateTime = takeTime;
        for (int eachDuration : durations) {
            ongoingDateTime = takeTrain(ongoingDateTime, eachDuration);
        }
        return ongoingDateTime;
    }

    private LocalDateTime takeTrain(LocalDateTime ongoingDateTime, int duration) {
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
