package nextstep.subway.path.domain.policy.line;

import static nextstep.subway.path.domain.Fare.*;

public class LineFareSecondPolicy implements LineFarePolicy {

    public static final int FIFTY_KM_DISTANCE = 50;
    public static final int FIFTY_KM_DELIMITER = 8;

    private final LineFareFirstPolicy lineFareFirstPolicy;

    public LineFareSecondPolicy(LineFareFirstPolicy lineFareFirstPolicy) {
        this.lineFareFirstPolicy = lineFareFirstPolicy;
    }

    @Override
    public int calculateLineFare(int distance) {
        int secondRuleDistance =  distance - FIFTY_KM_DISTANCE;
        int firstRuleDistance = distance - secondRuleDistance;

        int fareFirstRuleResult = lineFareFirstPolicy.calculateLineFare(firstRuleDistance);

        return fareFirstRuleResult + calculate50KmOver(secondRuleDistance);
    }

    public int calculate50KmOver(int distance) {
        int divisionFiftyDistance = (distance - NUMBER_ONE) / FIFTY_KM_DELIMITER;
        return (int) ((Math.ceil(divisionFiftyDistance) + NUMBER_ONE) * INCREASE_FARE);
    }
}
