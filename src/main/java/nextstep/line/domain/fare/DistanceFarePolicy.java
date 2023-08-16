package nextstep.line.domain.fare;

public interface DistanceFarePolicy {

    int DEFAULT_FARE = 1250;

    boolean isIncluded(int distance);

    int fare(int distance);

    default int extraCharges(int distance, int distanceInterval) {
        return (int) ((Math.ceil((distance - 1) / distanceInterval) + 1) * 100);
    }

}
