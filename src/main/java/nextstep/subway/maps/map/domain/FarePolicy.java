package nextstep.subway.maps.map.domain;

public interface FarePolicy {
    int calculate(FareContext fareContext);
}
