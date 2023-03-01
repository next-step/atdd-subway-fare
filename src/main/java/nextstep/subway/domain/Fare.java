package nextstep.subway.domain;

import java.util.List;

public class Fare {

    private final List<FarePolicy> farePolicies;

    public Fare(List<FarePolicy> farePolicies) {
        this.farePolicies = farePolicies;
    }

    public int calcFare() {
        return farePolicies.stream().mapToInt(FarePolicy::calcFare).sum();
    }

}
