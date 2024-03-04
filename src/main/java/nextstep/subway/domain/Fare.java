package nextstep.subway.domain;

import nextstep.subway.domain.chain.FareHandlerFactory;
import nextstep.subway.domain.farepolicy.AgeDiscount;
import nextstep.subway.domain.farepolicy.FarePolicy;
import nextstep.subway.domain.farepolicy.LineExtraFare;

import java.util.List;

public class Fare {

    private final List<FarePolicy> farePolicies;

    public Fare(Lines lines, FareAgeGroup fareAgeGroup) {
        this.farePolicies = List.of(
                new LineExtraFare(lines),
                new AgeDiscount(fareAgeGroup)
        );
    }

    public long calculateTotalFare(FareAgeGroup fareAgeGroup, long distance) {
        if (fareAgeGroup.isLineExtraFareFree()) {
            return 0;
        }
        long basicFare = new FareHandlerFactory().calculateFare(distance);
        for (FarePolicy policy : farePolicies) {
            basicFare = policy.calculateFare(basicFare);
        }
        return basicFare;
    }

}
