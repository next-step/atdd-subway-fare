package nextstep.subway.maps.map.domain;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

import com.google.common.collect.Lists;
import nextstep.subway.maps.line.domain.Line;
import nextstep.subway.maps.line.domain.Money;

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
        return lineStationEdges.stream()
            .map(LineStationEdge::getLine)
            .map(Line::getExtraFare)
            .max(Money::compareTo)
            .orElse(Money.NO_VALUE());
    }

    public LocalDateTime getArrivalTime(LocalDateTime departureTime) {
        LocalTime stationArrivalTime = departureTime.toLocalTime();
        for (LineStationEdge lineStationEdge : lineStationEdges) {
            LocalTime nextTime = lineStationEdge.calculateNextDepartureTime(stationArrivalTime);
            stationArrivalTime = lineStationEdge.calculateArrivedTime(nextTime);
        }
        return stationArrivalTime.atDate(departureTime.toLocalDate());
    }
}
