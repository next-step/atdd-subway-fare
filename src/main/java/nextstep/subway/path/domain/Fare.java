package nextstep.subway.path.domain;

import nextstep.subway.line.domain.Line;

import java.util.ArrayList;
import java.util.List;

public class Fare {
    private static final int BASE_FARE = 1250;

    private List<FarePolicy> farePolicies = new ArrayList<>();

    public Fare(int distance, List<Line> distinctLines) {
        farePolicies.add(new DistanceFarePolicy(distance));
        farePolicies.add(new LineFarePolicy(distinctLines));
        farePolicies.add(new UserFarePolicy());
    }

    public int getTotalFare() {
        int total = BASE_FARE;

        for(FarePolicy farePolicy : farePolicies) {
            total = farePolicy.calculateFareByPolicy(total);
        }

        return total;
    }
}
