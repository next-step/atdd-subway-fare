package nextstep.subway.domain.chain;

import org.springframework.core.annotation.Order;

@Order(2)
public class ThirdPalicyFareHandler implements FareHandler {

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
