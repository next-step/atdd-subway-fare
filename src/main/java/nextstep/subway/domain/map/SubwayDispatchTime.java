package nextstep.subway.domain.map;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Objects;

/**
 * SubwayDispatchTime 객체는 입력한 출발 시간으로 부터 도착 시간을 구해주는 객체 입니다.
 *
 * 최대한 작은 문제만을 다뤄 단순화 하기 위해 시간 이외의 객체는 알지 못하도록 설계 했습니다.
 * */
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
        LocalDateTime ongoingDateTime = nextTakeTime(takeTime);
        for (int eachDuration : durations) {
            ongoingDateTime = takeTrain(ongoingDateTime, eachDuration);
        }
        return ongoingDateTime;
    }

    private LocalDateTime nextTakeTime(LocalDateTime takeTime) {
        final LocalDateTime takableTime = calculateIfWaiting(takeTime);
        return calculateIfOutOfServiceTime(takableTime);
    }

    private LocalDateTime calculateIfWaiting(LocalDateTime takeTime) {
        LocalTime ongoingTime = startTime;
        while(ongoingTime.isBefore(takeTime.toLocalTime())) {
            ongoingTime = ongoingTime.plusHours(intervalTime.getHour())
                                     .plusMinutes(intervalTime.getMinute());
        }
        return LocalDateTime.of(takeTime.toLocalDate(), ongoingTime);
    }

    private LocalDateTime calculateIfOutOfServiceTime(LocalDateTime takableTime) {
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

    public boolean matchStartTime(LocalTime startTime) {
        return this.startTime.equals(startTime);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        SubwayDispatchTime that = (SubwayDispatchTime)o;
        return Objects.equals(startTime, that.startTime) && Objects.equals(endTime, that.endTime) && Objects.equals(
            intervalTime, that.intervalTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(startTime, endTime, intervalTime);
    }
}
