package nextstep.subway.path.domain.fare;

public interface DistanceFarePolicy {
    boolean condition(int totalDistance);

    int calculateFare(int totalDistance);
}
