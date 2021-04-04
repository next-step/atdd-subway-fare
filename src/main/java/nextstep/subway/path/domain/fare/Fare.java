package nextstep.subway.path.domain.fare;

import nextstep.subway.path.domain.fare.distance.Distance10FareChain;
import nextstep.subway.path.domain.fare.distance.Distance50FareChain;
import nextstep.subway.path.domain.fare.distance.DistanceDefaultFareChain;
import nextstep.subway.path.domain.fare.distance.DistanceFareChain;

import java.util.Arrays;
import java.util.List;

public class Fare {

    private static final int DEFAULT_FARE = 1250;

    private int fare;

    public Fare(int distance) {
        if(distance < 0) {
            throw new RuntimeException("요금은 양수입니다.");
        }
        calculate(distance);
    }

    private void calculate(int distance) {
        List<DistanceFareChain> distanceChains = Arrays.asList(
                new DistanceDefaultFareChain(),
                new Distance10FareChain(),
                new Distance50FareChain());

        this.fare = distanceChains.stream()
                .mapToInt(chain -> chain.calculate(distance))
                .sum();
    }

    public int getFare() {
        return fare;
    }
}
