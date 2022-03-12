package nextstep.subway.domain.fare;

import nextstep.subway.domain.Path;

import java.util.Arrays;
import java.util.List;

public class FareCalculator {

    private final List<FarePolicy> farePolicies;

    public FareCalculator(final Path path, final int age) {
        farePolicies = createFarePolicies(path, age);
    }

    private List<FarePolicy> createFarePolicies(final Path path, final int age) {
        return Arrays.asList(
                DistancePolicy.from(path.extractDistance()),
                LinePolicy.from(path.extractExpensiveExtraCharge()),
                AgePolicy.from(age));
    }

    public Fare getFare() {
        Fare fare = Fare.standard();
        for(FarePolicy policy : farePolicies) {
            policy.calculate(fare);
        }
        return fare;
    }
}
