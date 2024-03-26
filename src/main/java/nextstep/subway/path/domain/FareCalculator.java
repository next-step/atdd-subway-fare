package nextstep.subway.path.domain;

import nextstep.subway.section.domain.Section;
import nextstep.subway.station.domain.Station;

import java.util.HashSet;
import java.util.List;

public class FareCalculator {
    private static final int BASIC_FARE = 1_250;
    private static final int ADDITIONAL_FARE = 100;
    private static final int DISTANCE_UNIT_1 = 10;
    private static final int DISTANCE_UNIT_2 = 50;
    private static final int ADDITIONAL_FARE_UNIT_2 = 5;
    private static final int ADDITIONAL_FARE_OVER_UNIT = 8;


    public static int getDistanceFare(int totalDistance) {
        if (totalDistance <= DISTANCE_UNIT_1) {
            return BASIC_FARE;
        }

        if (totalDistance <= DISTANCE_UNIT_2) {
            return calculateDistanceUnit2(totalDistance);
        }

        return calculateOverDistanceUnit(totalDistance);
    }

    public static int getLineAdditionalFare(List<Station> stations, List<Section> sections) {
        int lineAdditionalFare = 0;
        for (int i = 0; i < stations.size() - 1; i++) {
            Station upStation = stations.get(i);
            Station downStation = stations.get(i + 1);
            int additionalFare = sections.stream()
                    .filter(section -> new HashSet<>(section.stations()).containsAll(List.of(upStation, downStation)))
                    .map(section -> section.getLine().getAdditionalFare())
                    .findFirst()
                    .orElseThrow();
            lineAdditionalFare = Math.max(lineAdditionalFare, additionalFare);
        }
        return lineAdditionalFare;
    }

    private static int calculateDistanceUnit2(int totalDistance) {
        int additionalDistance = totalDistance - DISTANCE_UNIT_1;
        int additionalFare = calculateAdditionalFare(additionalDistance, ADDITIONAL_FARE_UNIT_2);
        return BASIC_FARE + additionalFare;
    }

    private static int calculateOverDistanceUnit(int totalDistance) {
        int additionalDistance = totalDistance - DISTANCE_UNIT_2;
        int additionalFare1 = calculateAdditionalFare((DISTANCE_UNIT_2 - DISTANCE_UNIT_1), ADDITIONAL_FARE_UNIT_2);
        return BASIC_FARE + additionalFare1 + calculateAdditionalFare(additionalDistance, ADDITIONAL_FARE_OVER_UNIT);
    }

    private static int calculateAdditionalFare(int distance, int unit) {
        return (int) ((Math.ceil((double) distance / unit)) * ADDITIONAL_FARE);
    }
}
