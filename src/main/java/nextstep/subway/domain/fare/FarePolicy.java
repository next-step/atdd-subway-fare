package nextstep.subway.domain.fare;

public interface FarePolicy {
    int DEFAULT_FARE = 1250;
    int EXTRA_FARE = 100;

    int getFare(int distance);

    default int calculateFare(int distance, int extraFareRate) {
        return (int) ((Math.ceil((distance - 1) / extraFareRate) + 1) * EXTRA_FARE);
    }
}
