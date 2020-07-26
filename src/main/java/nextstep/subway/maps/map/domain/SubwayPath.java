package nextstep.subway.maps.map.domain;

import com.google.common.collect.Lists;

import java.util.List;
import java.util.stream.Collectors;

public class SubwayPath {
    // 요금 기준
    public static int BASE_FARE = 1250;
    public static int ADDITIONAL_FARE = 100;
    public static int FIRST_LIMIT_MAX_FARE = 800;

    // 구간 거리 기준
    public static int FIRST_LIMIT = 10;
    public static int SECOND_LIMIT = 50;
    public static int FIRST_SECTION_RANGE = SECOND_LIMIT - FIRST_LIMIT;

    // 구간 별 km 기준
    public static int FIRST_SECTION_PER_KILO = 5;
    public static int SECOND_SECTION_PER_KILO = 8;

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
        if (distance <= FIRST_SECTION_RANGE) {
            return calculateFareInFirstRange(distance, fare);
        }

        return calcauateFareInSecondRange(distance, fare);
    }

    private int calculateFareInFirstRange(int distance, int fare) {
        fare += getAdditionalFarePer(distance, FIRST_SECTION_PER_KILO);
        return fare;
    }

    private int calcauateFareInSecondRange(int distance, int fare) {
        int leftDistance = distance - FIRST_SECTION_RANGE;
        fare += FIRST_LIMIT_MAX_FARE;
        fare += getAdditionalFarePer(leftDistance, SECOND_SECTION_PER_KILO);
        return fare;
    }

    private int getAdditionalFarePer(int distance, int perKilometers) {
        return (int) ((Math.ceil((distance - 1) / perKilometers) + 1) * ADDITIONAL_FARE);
    }
}
