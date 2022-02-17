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

    public LocalDateTime findArrivalDateTime(LocalTime takeTime, List<Integer> durations) {
        LocalDateTime ongoingDateTime = takableDateTime(takeTime);
        for (int eachDuration : durations) {
            ongoingDateTime = takeTrain(ongoingDateTime, eachDuration);
        }
        return ongoingDateTime;
    }


    private LocalDateTime takableDateTime(LocalTime takeTime) {
        long takableTimestamp = takableTimestamp(firstTakableDateTime(takeTime));
        return LocalDateTime.ofInstant(
            Instant.ofEpochMilli(takableTimestamp), TimeZone.getDefault().toZoneId()
        );
    }

    private long takableTimestamp(LocalDateTime firstTakeDateTime) {
        long takeTimestamp = Timestamp.valueOf(firstTakeDateTime)
                                      .getTime();
        return takableTimestamp(takeTimestamp);
    }

    private long takableTimestamp(long takeTimestamp) {
        long intervalTimeStamp = TimeUnit.MILLISECONDS.convert(
            intervalTime.getHour() + intervalTime.getMinute() * 60, TimeUnit.SECONDS
        );

        long takableTimestamp = takeTimestamp / intervalTimeStamp * intervalTimeStamp;
        if (takeTimestamp % intervalTimeStamp == 0) {
            return takableTimestamp;
        }
        return takableTimestamp + intervalTimeStamp;
    }

    private LocalDateTime firstTakableDateTime(LocalTime takeTime) {
        if (takeTime.isBefore(startTime)) {
            return today(startTime);
        }
        if (takeTime.isAfter(endTime)) {
            return nextDay(startTime);
        }
        return today(takeTime);
    }

    private LocalDateTime today(LocalTime localTime) {
        return LocalDateTime.of(LocalDate.now(), localTime);
    }

    private LocalDateTime nextDay(LocalTime localTime) {
        return today(localTime).plusDays(1);
    }

    private LocalDateTime takeTrain(LocalDateTime ongoingDateTime, int duration) {
        LocalDateTime arrivalDateTime = ongoingDateTime.plusMinutes(duration);
        if (arrivalDateTime.toLocalTime().isAfter(endTime)) {
            return nextDay(startTime).plusMinutes(duration);
        }
        return arrivalDateTime;
    }
}
