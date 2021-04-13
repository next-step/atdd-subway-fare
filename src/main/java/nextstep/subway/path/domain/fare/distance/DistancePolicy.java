package nextstep.subway.path.domain.fare.distance;

import java.util.Arrays;
import java.util.List;

public class DistancePolicy {

    private int distance;
    private List<DistanceFareChain> distanceChains;

    public DistancePolicy(int distance) {
        this.distance = distance;
        this.distanceChains = Arrays.asList(
                new DistanceDefaultFareChain(),
                new Distance10FareChain(),
                new Distance50FareChain());
    }

    public int calculate(){
        return distanceChains.stream()
                .mapToInt(chain -> chain.calculate(distance))
                .sum();
    }
}
