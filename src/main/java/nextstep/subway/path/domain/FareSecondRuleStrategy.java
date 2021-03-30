package nextstep.subway.path.domain;

import static nextstep.subway.path.domain.Fare.*;
import static nextstep.subway.path.domain.FareFirstRuleStrategy.calculate10KmOverAnd50KmUnder;

public class FareSecondRuleStrategy implements FareRuleStrategy {

    public static final int FIFTY_KM_DISTANCE = 50;
    public static final int FIFTY_KM_DELIMITER = 8;

    @Override
    public int calculateFare(int distance) {
        int secondRuleDistance =  distance - FIFTY_KM_DISTANCE;
        int firstRuleDistance = distance - secondRuleDistance;
        return DEFAULT_FARE + calculate10KmOverAnd50KmUnder(firstRuleDistance) + calculate50KmOver(secondRuleDistance);
    }

    public static int calculate50KmOver(int distance) {
        int divisionFiftyDistance = (distance - NUMBER_ONE) / FIFTY_KM_DELIMITER;
        return (int) ((Math.ceil(divisionFiftyDistance) + NUMBER_ONE) * INCREASE_FARE);
    }
}
