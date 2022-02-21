package nextstep.subway.domain;

import com.google.common.collect.Lists;

import java.util.List;

public class SubwayFare {
    private final int distance;
    private final List<SubwayFarePolicy> subwayFarePolicies ;

    public SubwayFare(int distance) {
        this.distance = distance;
        this.subwayFarePolicies = Lists.newArrayList(new DefaultFarePolicy(), new From10To50Policy(), new Over50Policy());
    }

    public int calculate() {
        int currentFare = 0;
        for (SubwayFarePolicy policy : subwayFarePolicies) {
            currentFare = policy.apply(currentFare, distance);
        }

        return currentFare;
    }
}
