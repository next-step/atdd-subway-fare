package nextstep.subway.maps.map.domain;

import nextstep.subway.maps.line.domain.Line;
import nextstep.subway.maps.line.domain.LineStation;
import nextstep.subway.maps.line.domain.Money;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

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
        return this.lineStationEdges.stream()
                .map(LineStationEdge::getLine)
                .map(Line::getExtraFare)
                .max(Money::compareTo)
                .orElse(Money.ZERO);
    }

    public LocalDateTime getArrivalTime(LocalDateTime departureTime) {
        LocalTime stationArrivedTime = departureTime.toLocalTime();
        for (LineStationEdge lineStationEdge : lineStationEdges) {
            LocalTime nextTime = lineStationEdge.calculateNextDepartureTime(stationArrivedTime);
            stationArrivedTime = lineStationEdge.calculateArrivedTime(nextTime);
        }

        return stationArrivedTime.atDate(departureTime.toLocalDate());
    }
}
