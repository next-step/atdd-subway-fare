package nextstep.subway.maps.map.domain;

import com.google.common.collect.Lists;
import nextstep.subway.maps.line.domain.Line;
import nextstep.subway.maps.line.domain.Money;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

public class SubwayPath {
    private List<LineStationEdge> lineStationEdges;

    public SubwayPath(List<LineStationEdge> lineStationEdges) {
        this.lineStationEdges = lineStationEdges;
    }

    public List<LineStationEdge> getLineStationEdges() {
        return lineStationEdges;
    }

    public List<Long> extractStationId() {
        List<Long> stationIds = Lists.newArrayList(lineStationEdges.get(0).getLineStation().getPreStationId());
        stationIds.addAll(lineStationEdges.stream()
                .map(it -> it.getLineStation().getStationId())
                .collect(Collectors.toList()));

        return stationIds;
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
