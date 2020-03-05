package atdd.path.application.dto;

import atdd.path.domain.Line;

import java.time.LocalDateTime;
import java.util.List;

public class MinTimePathResponseView {
    private Long startStationId;
    private Long endStationId;
    private List<StationResponseView> stations;
    private List<Line> lines;
    private int distance;
    private LocalDateTime departAt;
    private LocalDateTime arriveBy;

    public MinTimePathResponseView() {
    }

    public MinTimePathResponseView(Long startStationId, Long endStationId,
                                   List<StationResponseView> stations, List<Line> lines, int distance,
                                   LocalDateTime departAt, LocalDateTime arriveBy) {
        this.startStationId = startStationId;
        this.endStationId = endStationId;
        this.stations = stations;
        this.lines = lines;
        this.distance = distance;
        this.departAt = departAt;
        this.arriveBy = arriveBy;
    }
}
