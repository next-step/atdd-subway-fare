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

    public static int getFare(int distance, List<Line> lines, List<Section> sectionList, int age) {
        int defaultFare = FareCalculator.calculateByDistance(distance);
        int extraFare = FareCalculator.getExtraFare(lines, sectionList);
        float discountRate = FareCalculator.getAgeBasedDiscountPolicyDiscountRate(age);
        return (int) ((defaultFare + extraFare) * (1 - discountRate));
    }

    public static int calculateByDistance(int distance) {
        if (distance > DISTANCE_THRESHOLD_KM) {
            int extraDistance = distance - DISTANCE_THRESHOLD_KM;
            return calculateByDistance1(extraDistance,DISTANCE_INTERVAL_8_KM) + BASE_FARE_UP_TO_50_KM;
        }
        return calculateByDistance1(distance, DISTANCE_INTERVAL_5_KM);
    }

    private static int calculateByDistance1(int extraDistance,int distanceInterval) {
        return ((extraDistance / distanceInterval) + calculateAdditionalCharge(extraDistance,
                distanceInterval))
                * FARE_PER_INTERVAL;
    }

    private static int calculateAdditionalCharge(int distance, int distanceIntervalKm) {
        return (distance % distanceIntervalKm) > 0 ? 1 : 0;
    }

    public static int getExtraFare(List<Line> lines, List<Section> sections) {
        return lines.stream()
                .filter(line -> line.getSections().stream()
                        .anyMatch(sections::contains)
                )
                .map(Line::getExtraFare)
                .max(Integer::compareTo)
                .orElse(0);
    }

    public static float getAgeBasedDiscountPolicyDiscountRate(int age) {
        return AgeBasedDiscountPolicy.getPolicyByAge(age).getDiscountRate();
    }
}
