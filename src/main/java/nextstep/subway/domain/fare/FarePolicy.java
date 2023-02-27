package nextstep.subway.domain.fare;

public interface FarePolicy {
    int DEFAULT_FARE = 1250;
    int EXTRA_FARE = 100;

    int getFare(int distance);

    default int getFare(int distance, int min, int start, int rate) {
        if (distance <= min) {
            return 0;
        }

        int extraDistance = distance - min;
        if (start < distance) {
            extraDistance = start - min;
        }

        return calculateFare(extraDistance, rate);
    }

    default int calculateFare(int distance, int extraFareRate) {
        return (int) ((Math.ceil((distance - 1) / extraFareRate) + 1) * EXTRA_FARE);
    }
}
