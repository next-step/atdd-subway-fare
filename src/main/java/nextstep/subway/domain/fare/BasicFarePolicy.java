package nextstep.subway.domain.fare;

public class BasicFarePolicy implements FarePolicy {

    private static final long BASIC_FARE = 1250;

    @Override
    public long calculateOverFare(int distance) {
        // 51 -> 1250 + 800 + 100 = 2150

        long fare = 0L;
        if(distance <= 10) {
            return BASIC_FARE;
        }

        if(distance > 50) {
            fare += calculateOverFareFrom50KM(distance - 50);
            distance = 50;
        }

        if(distance > 10) {
            fare += calculateOverFareFrom10KM(distance - 10);
        }
        return BASIC_FARE + fare;
    }

    private long calculateOverFareFrom10KM(int distance) {
        return (long) ((Math.ceil((distance - 1) / 5) + 1) * 100);
    }

    private long calculateOverFareFrom50KM(int distance) {
        return (long) ((Math.ceil((distance - 1) / 8) + 1) * 100);
    }
}
