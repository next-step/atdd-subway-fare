package nextstep.subway.domain.fare;

public interface FareChain {
    void setNextChain(FareChain fareChain);
    int calculateFare(int distance);
}
