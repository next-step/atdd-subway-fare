package nextstep.subway.domain.farepolicy;

import java.util.function.Supplier;

public class OverFarePolicy implements Policy{

    private final int fareDistance;

    public OverFarePolicy(int fareDistance) {
        this.fareDistance = fareDistance;
    }

    Supplier<Integer> expression;

    @Override
    public int calculate(int fare) {
        expression = () -> (overFare(fareDistance));

        return fare + expression.get();
    }

    private int overFare(int distance) {
        if (distance <= EXTRA_CHARGE_START_DISTANCE) {
            return calculateDefaultOverFare(distance);
        }

        int overFare = calculateDefaultOverFare(EXTRA_CHARGE_START_DISTANCE);

        return overFare + calculateExtraFare(distance - EXTRA_CHARGE_START_DISTANCE);
    }

    private int calculateDefaultOverFare(int distance) {
        return (int) ((Math.ceil(((distance) - 1) / DEFAULT_OVER_CHARGE_DISTANCE) + 1) * 100) - 200;
    }

    private int calculateExtraFare(int extraDistance) {
        return ((extraDistance-1) / EXTRA_CHARGE_DISTANCE + 1) * EXTRA_CHARGE;
    }
}
