package nextstep.subway.path.domain.fare;

import nextstep.subway.path.domain.fare.age.AgePolicy;
import nextstep.subway.path.domain.fare.distance.DistancePolicy;

public class Fare {

    private int fare;

    public Fare(int distance, int age, int extra) {
        DistancePolicy distancePolicy = new DistancePolicy(distance);
        AgePolicy agePolicy = new AgePolicy(age);

        final int fare = distancePolicy.calculate();
        this.fare = agePolicy.calculate(fare) + extra;
    }

    public int getFare() {
        return fare;
    }
}
