package nextstep.subway.domain;

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

    public LocalDateTime take(LocalTime of, List<Integer> durations) {
        return null;
    }
}
