package nextstep.path.domain;

import nextstep.path.DistanceFare;

import java.util.List;

public class Fare {
    int fare = 0;
    public void calculateFare(int distance, List<DistanceFare> distanceFares) {
        for (DistanceFare distanceFare : distanceFares) {
            fare += distanceFare.calculateFare(distance);
        }
    }

    public int getFare() {
        return fare;
    }
}
