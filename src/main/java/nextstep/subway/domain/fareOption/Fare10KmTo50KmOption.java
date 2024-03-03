package nextstep.subway.domain.fareOption;

public class Fare10KmTo50KmOption extends FareCalculateOption {
    private static final int DISTANCE_OVER = 10;
    private static final int DISTANCE_UNDER = 50;
    private static final int CHARGING_UNIT_DISTANCE = 5;
    private static final int FARE = 100;

    public Fare10KmTo50KmOption() {
        super(DISTANCE_OVER, DISTANCE_UNDER, CHARGING_UNIT_DISTANCE, FARE);
    }
}
