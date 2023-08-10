package nextstep.subway.domain;

import java.util.List;

public class FareCalculator {

    private static final int FARE_PER_INTERVAL = 100;
    private static final int DISTANCE_INTERVAL_5_KM = 5;
    private static final int DISTANCE_INTERVAL_8_KM = 8;
    private static final int DISTANCE_THRESHOLD_KM = 50;
    private static final int BASE_FARE_UP_TO_50_KM = 1000;

    private FareCalculator() {
        throw new IllegalStateException();
    }

    public static int calculateByDistance(int distance) {
        if (distance > DISTANCE_THRESHOLD_KM) {
            int extraDistance = distance - DISTANCE_THRESHOLD_KM;
            return ((extraDistance / DISTANCE_INTERVAL_8_KM) + calculateAdditionalCharge(extraDistance,
                    DISTANCE_INTERVAL_8_KM))
                    * FARE_PER_INTERVAL + BASE_FARE_UP_TO_50_KM;
        }
        return ((distance / DISTANCE_INTERVAL_5_KM) + calculateAdditionalCharge(distance, DISTANCE_INTERVAL_5_KM))
                * FARE_PER_INTERVAL;
    }

    private static int calculateAdditionalCharge(int distance, int distanceIntervalKm) {
        return (distance % distanceIntervalKm) > 0 ? 1 : 0;
    }

    public static long getExtraFare(List<Line> lines, List<Section> sections) {
        return lines.stream()
                .filter(line -> line.getSections().stream()
                        .anyMatch(sections::contains)
                )
                .map(Line::getExtraFare)
                .max(Long::compareTo)
                .orElse(0L);
    }


    public static float getAgeBasedDiscountPolicy(long age) {
        if (age > 18){
            return 0;
        }
        if (age > 12){
            return 0.2f;
        }
        if (age > 5) {
            return 0.5f;
        }
        return 1;
    }
}
