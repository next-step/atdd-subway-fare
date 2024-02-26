package nextstep.subway.domain.chain;

public interface FareHandler {
    void setNextHandler(FareHandler handler);
    long calculate(long distance);
}
