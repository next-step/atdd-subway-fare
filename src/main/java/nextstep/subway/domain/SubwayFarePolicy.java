package nextstep.subway.domain;

public interface SubwayFarePolicy {
    int apply(int currentFare, int distance);
}
