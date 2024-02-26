package nextstep.subway.domain.chain;

import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Order(1)
@Component
public class SecondPalicyFareHandler implements FareHandler {

    private FareHandler nextHandler;

    @Override
    public void setNextHandler(FareHandler fareHandler) {
        this.nextHandler = fareHandler;
    }

    @Override
    public long calculate(long duration) {
        return 0;
    }
}
