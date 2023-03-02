package nextstep.subway.domain;

import static nextstep.subway.domain.DistanceFare.*;

public class DistanceFarePolicy extends AbstractFarePolicy {

    private final int maxExtraFare;

    public DistanceFarePolicy(int maxExtraFare) {
        this(null, maxExtraFare);
    }

    public DistanceFarePolicy(FarePolicy nextPolicy, int maxExtraFare) {
        super(nextPolicy);
        this.maxExtraFare = maxExtraFare;
    }

    @Override
    public int calculateFare(int distance) {
        int result = calculate(distance) + maxExtraFare;

        if (hasNext()) {
            return result + nextPolicy.calculateFare(distance);
        }

        return result;
    }

    private int calculate(int distance) {
        if (distance <= DEFAULT_DISTANCE.getValue()) {
            return DEFAULT.getValue();
        }

        if (distance <= DistanceFare.OVER_BETWEEN_TEN_AND_FIFTY.getValue()) {
            return DEFAULT.getValue() + calculateOverFare(
                    distance - DEFAULT_DISTANCE.getValue(),
                    STANDARD_DISTANCE_OVER_BETWEEN_TEN_AND_FIFTY.getValue()
            );
        }

        return DEFAULT.getValue() + calculateOverFare(
                distance - DEFAULT_DISTANCE.getValue(),
                STANDARD_FARE_DISTANCE_OVER_FIFTY.getValue()
        );
    }

    private int calculateOverFare(int distance, int kilometer) {
        return (int) ((Math.ceil(((distance - 1) / (double) kilometer)) + 1) * 100);
    }
}
