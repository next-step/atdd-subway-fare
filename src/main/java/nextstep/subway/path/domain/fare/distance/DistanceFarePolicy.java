package nextstep.subway.path.domain.fare.distance;

public interface DistanceFarePolicy {
    boolean isSatisfiedBy(int totalDistance);

    int calculateFare(int totalDistance);
}
