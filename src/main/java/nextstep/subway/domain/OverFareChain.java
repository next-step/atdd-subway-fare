package nextstep.subway.domain;

public interface OverFareChain {
    void setNextChain(OverFareChain overFareChain);
    Fare calculateFare(int distance);
}
