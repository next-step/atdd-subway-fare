package nextstep.subway.domain.fare;

import nextstep.member.domain.Member;
import nextstep.subway.domain.Path;

public class SubwayFare {
    private static final int BASIC_FARE = 1250;

    private static FareCalculationRule buildFareCalculationChain() {
        FareCalculationRule ageDiscountRule = new AgeDiscountRule();
        FareCalculationRule distanceDiscountRule = new DistanceFareRule();
        FareCalculationRule lineFareRule = new LineFareRule();

        distanceDiscountRule.setNextRule(lineFareRule);
        lineFareRule.setNextRule(ageDiscountRule);

        return distanceDiscountRule;
    }

    private SubwayFare() {
    }

    public static int calculateFare(Path path, Member member) {
        FareCalculationRule fareCalculationChain = buildFareCalculationChain();
        return fareCalculationChain.calculateFare(path, member.getAge(), BASIC_FARE);
    }
}
