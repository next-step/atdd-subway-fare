package nextstep.subway.maps.map.domain;

public interface FarePolicy {
    void calculate(FareContext fareContext);
}
