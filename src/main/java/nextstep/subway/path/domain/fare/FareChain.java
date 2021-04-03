package nextstep.subway.path.domain.fare;

public interface FareChain {

    void setFareChain(FareChain nextChain);

    int calculate(int distance, int fare);
}
