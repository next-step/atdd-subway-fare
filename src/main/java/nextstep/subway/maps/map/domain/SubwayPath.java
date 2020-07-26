package nextstep.subway.maps.map.domain;

import com.google.common.collect.Lists;

import java.util.List;
import java.util.stream.Collectors;

public class SubwayPath {
    public static int BASE_FARE = 1250;
    public static int FIRST_LIMIT = 10;
    public static int SECOND_LIMIT = 50;

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

    public int calculateFare(int distance) {
        if (distance > FIRST_LIMIT) {
            return BASE_FARE + calculateOverFare(distance - FIRST_LIMIT);
        }
        return BASE_FARE;
    }

    public int calculateOverFare(int distance) {
        int fare = 0;
        if (distance <= 40) {
            fare += (int) ((Math.ceil((distance - 1) / 5) + 1) * 100);
            return fare;
        }

        int leftDistance = distance - 40;
        fare += 800;
        fare += (int) ((Math.ceil((leftDistance - 1) / 8) + 1) * 100);
        return fare;
    }
}
