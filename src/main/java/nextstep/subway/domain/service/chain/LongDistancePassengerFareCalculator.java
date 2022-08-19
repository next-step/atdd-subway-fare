package nextstep.subway.domain.service.chain;

import org.springframework.stereotype.Component;

@Component
public class LongDistancePassengerFareCalculator implements FareChain {
    @Override
    public int calculateBaseOnDistance(int distance) {
        return (int) (Math.ceil((distance - 51) / 8) + 1) * 100;
    }

    @Override
    public void setNext(FareChain chain) {
    }

    @Override
    public boolean canSupport(int distance) {
        return true;
    }
}
