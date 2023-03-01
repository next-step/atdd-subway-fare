package nextstep.subway.domain;

import java.util.ArrayList;
import java.util.List;

public class Fare {

    private static final int DEFAULT_FARE = 1250;

    private final List<FarePolicy> farePolicies = new ArrayList<>();

    public Fare() {

    }

    public int calcFare() {
        int fare = DEFAULT_FARE;

        for (FarePolicy farePolicy : farePolicies) {
            fare += farePolicy.calcFare(fare);
        }

        return fare;
    }

    public void addPolicy(FarePolicy farePolicy) {
        farePolicies.add(farePolicy);
    }

}
