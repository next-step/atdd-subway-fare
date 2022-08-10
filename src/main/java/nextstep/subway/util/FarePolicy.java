package nextstep.subway.util;

@FunctionalInterface
public interface FarePolicy {

    int calculateFare(int distance);
}
