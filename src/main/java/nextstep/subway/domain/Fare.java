package nextstep.subway.domain;

import static nextstep.subway.domain.FareType.STANDARD_FARE;
import static nextstep.subway.domain.FareType.findCriteria;
import static nextstep.subway.domain.FareType.isStandard;

public class Fare {

    private int cost;
    private final int distance;

    public Fare(final int distance) {
        this.distance = distance;
    }

    public int findCost() {
        calculate(distance);
        return cost;
    }

    private void calculate(final int distance) {
        if(isStandard(distance)) {
            this.cost = STANDARD_FARE;
            return;
        }
        this.cost = STANDARD_FARE + calculateOverFare(distance, findCriteria(distance));
    }

    private int calculateOverFare(final int distance, final int criteria) {
        return (int) ((Math.ceil(((double) distance - 11) / criteria) + 1) * 100);
    }
}
