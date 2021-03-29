package nextstep.subway.path.domain;

public class Fare {

    private static final int DEFAULT_FARE = 1250;
    private static final int TEN_KM_DISTANCE = 10;
    private static final int FIFTY_KM_DISTANCE = 50;
    private static final int INCREASE_FARE = 100;
    private static final int TEN_KM_DELIMITER = 5;
    private static final int FIFTY_KM_DELIMITER = 8;
    private static final int NUMBER_ONE = 1;

    private final int fare;

    public Fare(int distance) {
        this.fare = DEFAULT_FARE + calculateFare(distance);
    }

    public int calculateFare(int distance) {
        if (distance > TEN_KM_DISTANCE && distance <= FIFTY_KM_DISTANCE) {
            return calculate10KmOverAnd50KmUnder(distance);
        }
        if (distance > FIFTY_KM_DISTANCE) {
            int thirdRuleDistance = distance - FIFTY_KM_DISTANCE;
            int secondRuleDistance = distance - thirdRuleDistance;
            return calculate10KmOverAnd50KmUnder(secondRuleDistance) + calculate50KmOver(thirdRuleDistance);
        }
        return 0;
    }

    private int calculate10KmOverAnd50KmUnder(int distance) {
        return (int) ((Math.ceil((distance - TEN_KM_DISTANCE - NUMBER_ONE) / TEN_KM_DELIMITER) + NUMBER_ONE) * INCREASE_FARE);
    }

    private int calculate50KmOver(int distance) {
        return (int) ((Math.ceil((distance - NUMBER_ONE) / FIFTY_KM_DELIMITER) + NUMBER_ONE) * INCREASE_FARE);
    }

    public int getFare() {
        return fare;
    }
}
