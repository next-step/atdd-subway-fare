package nextstep.subway.maps.map.application;

import nextstep.subway.maps.map.domain.DistanceProportionalFarePolicy;
import nextstep.subway.maps.map.domain.FareContext;
import nextstep.subway.maps.map.domain.FarePolicy;
import org.springframework.lang.Nullable;

import java.util.ArrayList;
import java.util.List;

public class FareCalculator {
    public static final int DEFAULT_FARE = 1250;
    private static final int OVER_DISTANCE_FARE_UNIT = 100;

    private final List<FarePolicy> farePolicies = new ArrayList<>();

    public FareCalculator() {
        this.farePolicies.add(new DistanceProportionalFarePolicy());
    }

    public int calculate(int distance) {
        int fare = DEFAULT_FARE;
        fare += calculateOverFare(distance, 10, 50, 5);
        fare += calculateOverFare(distance, 50, null, 8);

        return fare;
    }

    private int calculateOverFare(Integer distance, Integer min, @Nullable Integer max, Integer unitDistance) {
        if (distance <= min) {
            return 0;
        }

        if (max != null && distance > max) {
            return calculateOverFare(max - min, unitDistance);
        }

        int overDistance = distance - min;
        return calculateOverFare(overDistance, unitDistance);
    }

    private int calculateOverFare(int overDistance, int unitDistance) {
        return (int) ((Math.ceil((overDistance - 1) / unitDistance) + 1) * OVER_DISTANCE_FARE_UNIT);
    }

    public int calculate(FareContext fareContext) {
        for (FarePolicy farePolicy : this.farePolicies) {
            farePolicy.calculate(fareContext);
        }

        return fareContext.getFare();
    }
}
