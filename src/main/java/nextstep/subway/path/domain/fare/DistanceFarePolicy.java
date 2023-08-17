package nextstep.subway.path.domain.fare;

public interface DistanceFarePolicy {
    boolean satisfiesCondition(int totalDistance);

    int calculateFare(int totalDistance);
}
