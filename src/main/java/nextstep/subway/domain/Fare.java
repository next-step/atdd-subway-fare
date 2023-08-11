package nextstep.subway.domain;

import java.util.ArrayList;
import java.util.List;

public class Fare {
    private static final int DEFAULT_FARE = 1250;
    private final List<FarePolicy> farePolicies = new ArrayList<>();

    private Fare() {
    }

    public static Fare of(FarePolicy farePolicy) {
        Fare fare = new Fare();
        fare.add(farePolicy);
        return fare;
    }

    public void add(FarePolicy farePolicy) {
        this.farePolicies.add(farePolicy);
    }

    public int get() {
        int fare = DEFAULT_FARE;
        for (FarePolicy farePolicy : farePolicies) {
            fare = farePolicy.calculate(fare);
        }
        return fare;
    }
}
