package nextstep.subway.domain.service.chain;

import org.springframework.stereotype.Component;

@Component
public class MediumDistancePassengerFareCalculator implements FareChain {
    private static final int MAX_DISTANCE = 50;

    private FareChain nextChain;

    @Override
    public int calculateBaseOnDistance(int distance) {
        if (!canSupport(distance)) {
           return operate(distance) + nextChain.calculateBaseOnDistance(distance);
        }
        return operate(distance);
    }

    @Override
    public void setNext(FareChain chain) {
        this.nextChain = chain;
    }

    @Override
    public boolean canSupport(int distance) {
        return distance <= MAX_DISTANCE;
    }

    private int operate(int distance) {
        return (int) Math.min(800, (Math.ceil((distance - 11) / 5) + 1) * 100);
    }
}
