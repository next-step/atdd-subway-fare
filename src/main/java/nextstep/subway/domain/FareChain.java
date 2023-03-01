package nextstep.subway.domain;

import java.util.List;

public class FareChain {

    private final List<Fare> fares;

    public FareChain(List<Fare> fares) {
        this.fares = fares;
    }

    public int calculateFare(int distance) {
        return fares.stream()
            .mapToInt(it -> it.calculateOverFare(distance))
            .sum();
    }
}
