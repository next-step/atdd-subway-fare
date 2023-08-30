package nextstep.subway.path.domain.policy.fare;

import nextstep.subway.path.domain.Path;

public class LineFarePolicy extends AdditionalFarePolicy {
    public LineFarePolicy(FarePolicy next) {
        super(next);
    }

    @Override
    protected int afterCalculated(Path path, int fare) {
        int additionalFare = path.getAdditionalFares().stream()
                .mapToInt(f -> f)
                .max()
                .orElse(0);

        return fare + additionalFare;
    }
}
