package nextstep.subway.path.domain.fare.distance;

public interface DistanceFarePolicy {
    boolean satisfiesCondition(int totalDistance);

    int calculateFare(int totalDistance);
}
