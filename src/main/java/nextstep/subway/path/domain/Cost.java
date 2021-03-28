package nextstep.subway.path.domain;

import nextstep.subway.exceptions.InvalidCostException;

public class Cost {
    private long cost;

    public Cost(long cost) {
        isValidCost(cost);
        this.cost = cost;
    }

    public static Cost from(int val) {
        return new Cost(val);
    }

    private void isValidCost(long cost) {
        if (cost < 0) {
            throw new InvalidCostException();
        }
    }

    public long getCost() {
        return cost;
    }
}
