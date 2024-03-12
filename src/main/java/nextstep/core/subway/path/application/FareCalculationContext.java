package nextstep.core.subway.path.application;

import nextstep.core.auth.domain.constants.AgeGroup;
import nextstep.core.subway.path.domain.PathFinderResult;

import java.util.List;

public class FareCalculationContext {

    private final int distance;

    private final List<Integer> additionalFares;

    private final AgeGroup ageGroup;

    public FareCalculationContext(int distance, List<Integer> additionalFares, AgeGroup ageGroup) {
        this.distance = distance;
        this.additionalFares = additionalFares;
        this.ageGroup = ageGroup;
    }

    public int getDistance() {
        return distance;
    }

    public List<Integer> getAdditionalFares() {
        return additionalFares;
    }

    public AgeGroup getAgeGroup() {
        return ageGroup;
    }
}
