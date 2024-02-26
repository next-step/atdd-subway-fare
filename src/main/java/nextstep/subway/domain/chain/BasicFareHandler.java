package nextstep.subway.domain.chain;

import org.springframework.stereotype.Component;

@Component
public class BasicFareHandler implements FareHandler {

    private FareHandler nextHandler;

    @Override
    public void setNextHandler(FareHandler fareHandler) {
        this.nextHandler = fareHandler;
    }

    @Override
    public long calculate(long distance) {
        return 0;
    }
}
