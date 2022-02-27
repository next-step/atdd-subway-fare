package nextstep.subway.domain.farepolicy;

public class OverFarePolicy implements Policy{
    int DEFAULT_OVER_CHARGE_DISTANCE = 5;
    int EXTRA_CHARGE_START_DISTANCE = 50;
    int EXTRA_CHARGE = 100;
    int EXTRA_CHARGE_DISTANCE = 8;

    private final int fareDistance;

    public OverFarePolicy(int fareDistance) {
        this.fareDistance = fareDistance;
    }

    @Override
    public int calculate(int fare) {
        return fare + overFare(fareDistance);
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
