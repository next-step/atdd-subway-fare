package nextstep.subway.domain;

import nextstep.subway.domain.farepolicy.AgeDiscount;
import nextstep.subway.domain.farepolicy.FarePolicy;
import nextstep.subway.domain.farepolicy.LineExtraFare;

import java.util.List;

public class Fare {

    private final List<FarePolicy> farePolicies;

    public Fare(Lines lines, FareAgeGroup fareAgeGroup, long basicFare) {
        this.farePolicies = List.of(
                new AgeDiscount(fareAgeGroup, basicFare),
                new LineExtraFare(lines, fareAgeGroup)
        );
    }

    public long calculateTotalFare(final long basicFare) {
        return farePolicies.stream()
                .filter(FarePolicy::isSupported)
                .mapToLong(FarePolicy::getFare)
                .reduce(basicFare, Long::sum);
    }
}
