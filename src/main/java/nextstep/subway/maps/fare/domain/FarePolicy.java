package nextstep.subway.maps.fare.domain;

public interface FarePolicy {
    void calculate(FareContext fareContext);
}
