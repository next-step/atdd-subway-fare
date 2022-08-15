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
        return (long) ((Math.ceil((distance - 1) / 5) + 1) * 100);
    }

    private long calculateOverFareFromSecond(int distance) {
        return (long) ((Math.ceil((distance - 1) / 8) + 1) * 100);
    }
}
