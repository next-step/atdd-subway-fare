package atdd.path.application.dto;

import atdd.path.domain.Line;
import atdd.path.domain.Station;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

public class MinTimePathResponseView {
    private Long startStationId;
    private Long endStationId;
    private List<Station> stations;
    private Set<Line> lines;
    private double distance;
    private LocalDateTime departAt;
    private LocalDateTime arriveBy;

    public MinTimePathResponseView() {
    }

    public MinTimePathResponseView(Long startStationId, Long endStationId,
                                   List<Station> stations, Set<Line> lines, double distance,
                                   LocalDateTime departAt, LocalDateTime arriveBy) {
        this.startStationId = startStationId;
        this.endStationId = endStationId;
        this.stations = stations;
        this.lines = lines;
        this.distance = distance;
        this.departAt = departAt;
        this.arriveBy = arriveBy;
    }

    public Long getStartStationId() {
        return startStationId;
    }

    public Long getEndStationId() {
        return endStationId;
    }

    public List<Station> getStations() {
        return stations;
    }

    public Set<Line> getLines() {
        return lines;
    }

    public double getDistance() {
        return distance;
    }

    public LocalDateTime getDepartAt() {
        return departAt;
    }

    public LocalDateTime getArriveBy() {
        return arriveBy;
    }
}
