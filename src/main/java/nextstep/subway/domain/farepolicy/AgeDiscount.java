package nextstep.subway.domain.farepolicy;

import nextstep.subway.domain.FareAgeGroup;

public class AgeDiscount implements FarePolicy {

    private final FareAgeGroup fareAgeGroup;
    private final long basicFare;

    public AgeDiscount(FareAgeGroup fareAgeGroup, long basicFare) {
        this.fareAgeGroup = fareAgeGroup;
        this.basicFare = basicFare;
    }

    @Override
    public boolean isSupported() {
        return true;
    }

    @Override
    public long getFare() {
        return -fareAgeGroup.calculateDiscountFare(basicFare);
    }
}
