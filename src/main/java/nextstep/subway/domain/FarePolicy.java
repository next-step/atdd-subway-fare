package nextstep.subway.domain;

public interface FarePolicy {
    /**
     * 기본 운임: 1250원(기준 거리: 10km)
     */
    int BASE_FARE = 1250;

    int calculate(int distance);
}
