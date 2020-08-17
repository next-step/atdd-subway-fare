package nextstep.subway.maps.map.domain;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import nextstep.subway.maps.line.domain.Line;
import nextstep.subway.maps.line.domain.LineStation;
import nextstep.subway.maps.line.domain.Money;

public class SubwayPath {
    private List<LineStationEdge> lineStationEdges;
    private PathDirection pathDirection;

    public SubwayPath(List<LineStationEdge> lineStationEdges, Long sourceId) {
        this.lineStationEdges = lineStationEdges;
        LineStation lineStation = lineStationEdges.get(0).getLineStation();
        this.pathDirection = PathDirection.findBySourceId(lineStation, sourceId);
    }

    public List<LineStationEdge> getLineStationEdges() {
        return lineStationEdges;
    }

    public List<Long> extractStationId() {
        return pathDirection.extractStationIds(lineStationEdges);
    }

    public int calculateDuration() {
        return lineStationEdges.stream().mapToInt(it -> it.getLineStation().getDuration()).sum();
    }

    public int calculateDistance() {
        return lineStationEdges.stream().mapToInt(it -> it.getLineStation().getDistance()).sum();
    }

    public Money calculateMaxLineExtraFare() {
        return lineStationEdges.stream()
            .map(LineStationEdge::getLine)
            .map(Line::getExtraFare)
            .max(Money::compareTo)
            .orElse(Money.NO_VALUE());
    }

    public LocalDateTime getArrivalTime(LocalDateTime departureTime) {
        LocalTime stationArrivalTime = departureTime.toLocalTime();
        for (LineStationEdge lineStationEdge : lineStationEdges) {
            LocalTime nextTime = lineStationEdge.calculateNextDepartureTime(stationArrivalTime, pathDirection);
            stationArrivalTime = lineStationEdge.calculateArrivedTime(nextTime);
        }
        return stationArrivalTime.atDate(departureTime.toLocalDate());
    }
}
