package nextstep.subway.domain.chain;

import org.springframework.stereotype.Component;

@Component
public class BasicFareHandler implements FareHandler {
    public static final long BASIC_DISTANCE = 10;
    public static final long BASIC_FARE = 1250;

    private FareHandler nextHandler;

    @Override
    public FareHandler setNextHandler(FareHandler fareHandler) {
        this.nextHandler = fareHandler;
        return this;
    }

    @Override
    public long calculate(long distance) {
        if (distance <= BASIC_DISTANCE) {
            return BASIC_FARE;
        }
        return BASIC_FARE + nextCalculate(nextHandler, distance);
    }

}
