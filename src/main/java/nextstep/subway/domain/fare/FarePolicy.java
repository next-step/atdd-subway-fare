package nextstep.subway.domain.fare;

public interface FarePolicy {

    long calculateOverFare(int distance);
}
