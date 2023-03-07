package nextstep.subway.domain.fare;

public interface FarePolicy {
    int calculateOverFare(int distance);
}
