package nextstep.subway.path.domain;

public class Fare {

    private final static int BASIC_FARE = 1250;
    private final int fare;

    public Fare(int distance, int age, int overFare) {
        this.fare = calculateFare(distance, age, overFare);
    }

    private int calculateFare(int distance, int age, int overFare) {
        DistanceOfFareType distanceOfFareType = DistanceOfFareType.valueOf(distance);
        int calculateDistanceFare = BASIC_FARE + overFare + distanceOfFareType.calculate(distance);

        return AgeOfFareType.calculate(calculateDistanceFare, age);
    }

    public int getFare() {
        return fare;
    }
}
