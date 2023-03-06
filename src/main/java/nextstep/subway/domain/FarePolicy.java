package nextstep.subway.domain;

import java.util.Arrays;
import java.util.function.Predicate;
import java.util.function.UnaryOperator;

public enum FarePolicy {
    NOT_MORE_THAN_10KM(
            distance -> distance <= Constants.TEN_KM,
            distance -> 0
    ),
    MORE_THAN_10KM_AND_NOT_MORE_THAN_50KM(
            distance -> Constants.TEN_KM < distance && distance <= Constants.FIFTY_KM,
            distance -> calculateExtraFare(Constants.TEN_KM, distance, Constants.PER_FIVE_KM)
    ),
    MORE_THAN_50KM(
            distance -> Constants.FIFTY_KM < distance,
            distance -> calculateExtraFare(Constants.TEN_KM, Constants.FIFTY_KM, Constants.PER_FIVE_KM)
                    + calculateExtraFare(Constants.FIFTY_KM, distance, Constants.PER_EIGHT_KM)
    );

    private final Predicate<Integer> predicate;
    private final UnaryOperator<Integer> extraFareOperator;

    FarePolicy(Predicate<Integer> predicate, UnaryOperator<Integer> extraFareOperator) {
        this.predicate = predicate;
        this.extraFareOperator = extraFareOperator;
    }

    public static int calculate(int distance) {
        return Constants.DEFAULT_FARE + getExtraFare(distance);
    }

    private static Integer getExtraFare(int distance) {
        Integer extraFare = Arrays.stream(values())
                .filter(it -> it.predicate.test(distance))
                .findFirst()
                .orElse(NOT_MORE_THAN_10KM)
                .extraFareOperator.apply(distance);
        return extraFare;
    }

    /**
     * 추가 운임에 대한 거리에 따른 계산 메서드
     *
     * @param start 시작 위치
     * @param end   도착 위치
     * @param unit  추가 운임의 단위거리
     * @return 거리에 따른 추가운임
     */
    private static int calculateExtraFare(int start, int end, int unit) {
        return (int) ((Math.ceil((end - start - 1) / unit) + 1) * 100);
    }

    private static class Constants {

        public static final int DEFAULT_FARE = 1250;

        public static final int TEN_KM = 10;
        public static final int FIFTY_KM = 50;

        public static final int PER_FIVE_KM = 5;
        public static final int PER_EIGHT_KM = 8;
    }
}
