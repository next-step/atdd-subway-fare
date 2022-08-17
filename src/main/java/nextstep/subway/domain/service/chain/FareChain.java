package nextstep.subway.domain.service.chain;


public interface FareChain {

    int calculateBaseOnDistance(int distance);

    void setNext(FareChain chain);

    boolean canSupport(int distance);
}
