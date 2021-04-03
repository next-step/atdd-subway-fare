package nextstep.subway.path.domain;

public class Fare {

    private static final int DEFAULT_FARE = 1250;
    private static final int FIRST_FARE_THRESHOLD = 10;
    private static final int FIRST_FARE_OFFSET = 5;
    private static final int FIRST_FARE_LIMIT = 800;
    private static final int SECOND_FARE_THRESHOLD = 50;
    private static final int SECOND_FARE_OFFSET = 8;

    private int fare;

    public Fare(int distance) {
        if(distance < 0) {
            throw new RuntimeException("요금은 양수입니다.");
        }
        calculate(distance);
    }

    private void calculate(int distance) {
        int fare = DEFAULT_FARE;

        if(distance > FIRST_FARE_THRESHOLD) {
            fare += Math.min(calculateOverFare(distance - FIRST_FARE_THRESHOLD, FIRST_FARE_OFFSET), FIRST_FARE_LIMIT);
        }

        if(distance > SECOND_FARE_THRESHOLD) {
            fare += calculateOverFare(distance - SECOND_FARE_THRESHOLD, SECOND_FARE_OFFSET);
        }

        this.fare = fare ;
    }

    private int calculateOverFare(int distance, int offset) {
        return (int) ((Math.ceil((distance - 1) / offset) + 1) * 100);
    }

    public int getFare() {
        return fare;
    }
}
