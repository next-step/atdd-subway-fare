package nextstep.subway.domain.fare;

import nextstep.subway.domain.Sections;

public class BasicFarePolicy implements FarePolicy {

    private static final long BASIC_FARE = 1250;
    private static final int FIFTY = 50;
    private static final int TEN = 10;

    @Override
    public long calculateOverFare(Sections sections) {
        int distance = sections.totalDistance();
        return calculateOverFare(distance);
    }

    public long calculateOverFare(int distance) {
        long fare = 0L;
        if(distance <= TEN) {
            return BASIC_FARE;
        }

        if(distance > FIFTY) {
            fare += calculateOverFareFromSecond(distance - FIFTY);
            distance = FIFTY;
        }

        fare += calculateOverFareFromFirst(distance - TEN);
        return BASIC_FARE + fare;
    }

    private long calculateOverFareFromFirst(int distance) {
        final int standard = 5;
        return calculateOverFare(distance, standard);
    }

    private long calculateOverFareFromSecond(int distance) {
        final int standard = 8;
        return calculateOverFare(distance, standard);
    }

    private long calculateOverFare(int distance, int standard) {
        return (long) ((Math.ceil((distance - 1) / standard) + 1) * 100);
    }
}
