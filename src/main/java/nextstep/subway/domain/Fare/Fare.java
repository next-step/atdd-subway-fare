package nextstep.subway.domain.Fare;

import nextstep.subway.domain.Path;

public class Fare {
    private final int cost;

    public Fare(int cost) {
        this.cost = cost;
    }

    public static Fare fromPath(Path path) {
        int distance = path.extractDistance();
        int fare = FarePolicy.getFare(distance);
        return new Fare(fare);
    }

    public int getCost() {
        return cost;
    }
}
