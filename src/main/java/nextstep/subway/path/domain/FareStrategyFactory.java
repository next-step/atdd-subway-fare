package nextstep.subway.path.domain;

import static nextstep.subway.path.domain.FareFirstRuleStrategy.TEN_KM_DISTANCE;
import static nextstep.subway.path.domain.FareSecondRuleStrategy.FIFTY_KM_DISTANCE;

public class FareStrategyFactory {

    public static FareRuleStrategy from(int distance) {
        if (distance > TEN_KM_DISTANCE && distance <= FIFTY_KM_DISTANCE) {
            return new FareFirstRuleStrategy();
        }
        if (distance > FIFTY_KM_DISTANCE) {
            return new FareSecondRuleStrategy(new FareFirstRuleStrategy());
        }
        return new FareDefaultRuleStrategy();
    }
}
