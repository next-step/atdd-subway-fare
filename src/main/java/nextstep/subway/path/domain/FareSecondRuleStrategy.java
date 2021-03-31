package nextstep.subway.path.domain;

import static nextstep.subway.path.domain.Fare.*;

public class FareSecondRuleStrategy implements FareRuleStrategy {

    public static final int FIFTY_KM_DISTANCE = 50;
    public static final int FIFTY_KM_DELIMITER = 8;

    private final FareFirstRuleStrategy fareFirstRuleStrategy;

    public FareSecondRuleStrategy(FareFirstRuleStrategy fareFirstRuleStrategy) {
        this.fareFirstRuleStrategy = fareFirstRuleStrategy;
    }

    @Override
    public int calculateFare(int distance) {
        int secondRuleDistance =  distance - FIFTY_KM_DISTANCE;
        int firstRuleDistance = distance - secondRuleDistance;

        int fareFirstRuleResult = fareFirstRuleStrategy.calculateFare(firstRuleDistance);

        return fareFirstRuleResult + calculate50KmOver(secondRuleDistance);
    }

    public int calculate50KmOver(int distance) {
        int divisionFiftyDistance = (distance - NUMBER_ONE) / FIFTY_KM_DELIMITER;
        return (int) ((Math.ceil(divisionFiftyDistance) + NUMBER_ONE) * INCREASE_FARE);
    }
}
