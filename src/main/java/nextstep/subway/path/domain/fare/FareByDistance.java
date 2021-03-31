package nextstep.subway.path.domain.fare;

import java.util.function.Function;

public class FareByDistance implements Fare {

    private final int distance;
    private final FareByDistancePolicy fareByDistancePolicy;

    public FareByDistance(int distance) {
        this.distance = distance;
        this.fareByDistancePolicy = FareByDistancePolicy.byDistance(distance);
    }

    @Override
    public int calculate() {
        return fareByDistancePolicy.getOperation().apply(distance);
    }
}
