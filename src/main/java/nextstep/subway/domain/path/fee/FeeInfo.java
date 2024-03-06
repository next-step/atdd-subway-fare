package nextstep.subway.domain.path.fee;

import java.util.List;

public class FeeInfo {
    private final Distance distance;
    private final List<Integer> additionalFees;
    private final AgeType ageType;

    private FeeInfo(final Distance distance, final List<Integer> additionalFees, final AgeType ageType) {
        this.distance = distance;
        this.additionalFees = additionalFees;
        this.ageType = ageType;
    }

    public static FeeInfo of(Distance distance, List<Integer> additionalFees, AgeType ageType) {
        return new FeeInfo(distance, additionalFees, ageType);
    }

    public Distance distance() {
        return distance;
    }

    public List<Integer> additionalFees() {
        return additionalFees;
    }

    public AgeType ageType() {
        return this.ageType;
    }
}
