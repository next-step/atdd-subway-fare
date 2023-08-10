package nextstep.subway.domain;

import java.util.List;

public class FareCalculator {

    private static final int FARE_PER_INTERVAL = 100;
    private static final int DISTANCE_INTERVAL_5_KM = 5;
    private static final int DISTANCE_INTERVAL_8_KM = 8;
    private static final int DISTANCE_THRESHOLD_KM = 50;
    private static final int BASE_FARE_UP_TO_50_KM = 1000;
    public static final int AGE_18 = 18;
    public static final int AGE_12 = 12;
    public static final int AGE_5 = 5;
    public static final int NO_DISCOUNT = 0;
    public static final float DISCOUNT_FOR_AGES_13_TO_18 = 0.2f;
    public static final float DISCOUNT_FOR_AGES_6_TO_12 = 0.5f;
    public static final int FREE = 1;

    private FareCalculator() {
        throw new IllegalStateException();
    }

    public static int getFare(int distance, List<Line> lines, List<Section> sectionList, int age) {
        int defaultFare = FareCalculator.calculateByDistance(distance);
        int extraFare = FareCalculator.getExtraFare(lines, sectionList);
        float discountRate = FareCalculator.getAgeBasedDiscountPolicy(age);
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

    public static float getAgeBasedDiscountPolicy(long age) {
        if (age > AGE_18){
            return NO_DISCOUNT;
        }
        if (age > AGE_12){
            return DISCOUNT_FOR_AGES_13_TO_18;
        }
        if (age > AGE_5) {
            return DISCOUNT_FOR_AGES_6_TO_12;
        }
        return FREE;
    }
}
