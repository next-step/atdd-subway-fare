package nextstep.subway.maps.boarding.domain;

public interface FareCalculationPolicy {
    int calculateFare(FareCalculationContext context);
}
