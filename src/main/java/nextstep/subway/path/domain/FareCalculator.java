package nextstep.subway.path.domain;

import nextstep.subway.path.domain.valueobject.Fare;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FareCalculator implements FareCalculation {
    private FareParameter parameter;
    private List< FarePolicy > farePolicies = new ArrayList<>();

    public FareCalculator(FareParameter parameter) {
        this.parameter = parameter;
    }

    @Override
    public Fare calculate() {
        Fare input = Fare.of(0);
        for ( FarePolicy policy : farePolicies) {
            input = policy.calculate(input);
        }
        return input;
    }

    public FareCalculator addAllBaseFarePolicy(FarePolicy... policies) {
        farePolicies.addAll(Arrays.asList(policies));
        return this;
    }
}
