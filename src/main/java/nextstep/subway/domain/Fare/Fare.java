package nextstep.subway.domain.Fare;

public class Fare {
    private final int cost;

    public Fare(int cost) {
        this.cost = cost;
    }

    public static int calculate(int distance, int lineExtraFare, int age) {
        int fare = FarePolicy.getFare(distance);
        return FarePolicy.discountFare(fare + lineExtraFare, age);
    }

    public int getCost() {
        return cost;
    }
}
