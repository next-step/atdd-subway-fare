package nextstep.subway.domain.map;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

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
        final LocalDateTime takableTime = takableTimeByStartEnd(takeTime);
        return takableTimestampByInterval(takableTime);
    }

    private LocalDateTime takableTimestampByInterval(LocalDateTime takeTime) {
        LocalTime ongoingTime = startTime;
        while(ongoingTime.isBefore(takeTime.toLocalTime())) {
            ongoingTime = ongoingTime.plusHours(intervalTime.getHour())
                                     .plusMinutes(intervalTime.getMinute());
        }
        return LocalDateTime.of(takeTime.toLocalDate(), ongoingTime);
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

    public LocalTime getStartTime() {
        return startTime;
    }

    public LocalTime getEndTime() {
        return endTime;
    }

    public LocalTime getIntervalTime() {
        return intervalTime;
    }
}
