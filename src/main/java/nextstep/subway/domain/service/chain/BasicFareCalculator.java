package nextstep.subway.domain.service.chain;


import org.springframework.stereotype.Component;

@Component
public class BasicFareCalculator implements FareChain {
    private static final int MAX_DISTANCE = 10;
    private static final int BASE_FARE = 1250;

    private FareChain nextChain;

    @Override
    public int calculateBaseOnDistance(int distance) {
        if(!canSupport(distance)) {
            return BASE_FARE + nextChain.calculateBaseOnDistance(distance);
        }

        return BASE_FARE;
    }

    @Override
    public void setNext(FareChain chain) {
        this.nextChain = chain;
    }

    @Override
    public boolean canSupport(int distance) {
        return distance <= MAX_DISTANCE;
    }
}
