package nextstep.subway.domain;

public interface FareCalculator {
    int calculateFare(int distance);

    default int calculateOverFare(int distance, int chargingUnitDistance, int fare) {
        if (distance < 1) {
            return 0;
        }

        return (int) Math.ceil((double) distance / chargingUnitDistance) * fare;
    }
}
