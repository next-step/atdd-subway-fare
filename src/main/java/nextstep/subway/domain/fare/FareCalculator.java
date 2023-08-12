package nextstep.subway.domain.fare;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

public class FareCalculator {

    private static final int BASE_FARE = 1250;

    private final List<FarePolicy> farePolicies;

    public FareCalculator(int distance, int fareByLine, Optional<Integer> age) {

        FarePolicy farePolicy1 = new DistanceFarePolicy(distance);
        FarePolicy farePolicy2 = new LineFarePolicy(fareByLine);
        FarePolicy farePolicy3 = new AgeFarePolicy(age);

        farePolicy1.setPolicyOrder(1);
        farePolicy2.setPolicyOrder(2);
        farePolicy3.setPolicyOrder(3);

        this.farePolicies = Arrays.asList(farePolicy1, farePolicy2, farePolicy3);
        this.farePolicies.sort(Comparator.comparingInt(FarePolicy::getPolicyOrder));
    }

    public int fare() {
        int fare = BASE_FARE;

        for (FarePolicy farePolicy : farePolicies) {
            fare = farePolicy.fare(fare);
        }

        return fare;
    }
}
