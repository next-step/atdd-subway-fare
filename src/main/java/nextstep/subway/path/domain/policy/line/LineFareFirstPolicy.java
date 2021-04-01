package nextstep.subway.path.domain.policy.line;

import static nextstep.subway.path.domain.Fare.*;

public class LineFareFirstPolicy implements LineFarePolicy {

    public static final int TEN_KM_DISTANCE = 10;
    public static final int TEN_KM_DELIMITER = 5;

    @Override
    public int calculateFare(int distance) {
        return DEFAULT_FARE + calculate10KmOverAnd50KmUnder(distance);
    }

    public int calculate10KmOverAnd50KmUnder(int distance) {
        int divisionTenDistance = (distance - TEN_KM_DISTANCE - NUMBER_ONE) / TEN_KM_DELIMITER;
        return (int) ((Math.ceil(divisionTenDistance) + NUMBER_ONE) * INCREASE_FARE);
    }
}
