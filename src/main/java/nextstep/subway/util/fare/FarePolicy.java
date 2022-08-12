package nextstep.subway.util.fare;

@FunctionalInterface
public interface FarePolicy {

    int calculateFare(int distance);
}
