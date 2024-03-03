package nextstep.subway.domain;

public class Fare50KmOverOption extends FareCalculateOption {
    private static final int DISTANCE_OVER = 50;
    private static final int DISTANCE_UNDER = Integer.MAX_VALUE;
    private static final int CHARGING_UNIT_DISTANCE = 8;
    private static final int FARE = 100;

    public Fare50KmOverOption() {
        super(DISTANCE_OVER, DISTANCE_UNDER, CHARGING_UNIT_DISTANCE, FARE);
    }
}
