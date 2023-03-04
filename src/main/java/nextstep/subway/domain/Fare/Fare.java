package nextstep.subway.domain.Fare;

public class Fare {
    private final int cost;

    public Fare(int cost) {
        this.cost = cost;
    }

    public static int calculate(int distance) {
        return FarePolicy.getFare(distance);
    }

    public int getCost() {
        return cost;
    }
}
