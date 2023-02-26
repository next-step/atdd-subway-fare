package nextstep.subway.domain;

public class Fare {

    public static final int DEFAULT_COST = 1_250;

    private final int cost;

    public Fare(int distance) {
        if (distance > 10) {
            this.cost = DEFAULT_COST + calculateOverFare(distance);
            return;
        }

        this.cost = DEFAULT_COST;
    }

    private int calculateOverFare(int distance) {
        int additionalDistance = 0;
        int additionalCost = 0;
        if (distance > 50) {
            additionalDistance = distance - 50;
            additionalCost = (int) ((Math.ceil((additionalDistance - 1) / 8) + 1) * 100);
        }

        return additionalCost + (int) ((Math.ceil((distance - additionalDistance - 10 - 1) / 5) + 1) * 100);
    }

    public int cost() {
        return cost;
    }
}
