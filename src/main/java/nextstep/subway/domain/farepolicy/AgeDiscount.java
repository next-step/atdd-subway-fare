package nextstep.subway.domain.farepolicy;

import nextstep.subway.domain.FareAgeGroup;

public class AgeDiscount implements FarePolicy {

    private final FareAgeGroup fareAgeGroup;

    public AgeDiscount(FareAgeGroup fareAgeGroup) {
        this.fareAgeGroup = fareAgeGroup;
    }

    @Override
    public long calculateFare(long basicFare) {
        return basicFare - fareAgeGroup.calculateDiscountFare(basicFare);
    }

}
