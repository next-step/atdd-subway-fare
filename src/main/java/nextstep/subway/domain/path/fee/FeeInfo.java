package nextstep.subway.domain.path.fee;

import java.util.List;

public class FeeInfo {
    private final Distance distance;
    private final List<Integer> additionalFees;

    private FeeInfo(final Distance distance, final List<Integer> additionalFees) {
        this.distance = distance;
        this.additionalFees = additionalFees;
    }

    public static FeeInfo of(Distance distance, List<Integer> additionalFees) {
        return new FeeInfo(distance, additionalFees);
    }

    public Distance distance() {
        return distance;
    }

    public List<Integer> additionalFees() {
        return additionalFees;
    }
}
