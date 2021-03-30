package nextstep.subway.path.domain;

public interface FarePolicy {
    int calculateFareByPolicy(int subtotal);
}
