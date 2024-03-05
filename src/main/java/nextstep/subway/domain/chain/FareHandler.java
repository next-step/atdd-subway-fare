package nextstep.subway.domain.chain;

public interface FareHandler {
    FareHandler setNextHandler(FareHandler handler);
    long calculate(long distance);

    default long nextCalculate(FareHandler nextHandler, long distance) {
        if (nextHandler != null) {
            return nextHandler.calculate(distance);
        }
        return 0L;
    }
}
