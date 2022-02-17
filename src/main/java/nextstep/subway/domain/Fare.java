package nextstep.subway.domain;

import java.util.List;

public class Fare {
    private final int distance;
    private final int age;
    private final boolean isLoggedIn;
    private final List<Line> lines;

    public Fare(final int distance, int age, boolean isLoggedIn, final List<Line> lines) {
        this.distance = distance;
        this.age = age;
        this.isLoggedIn = isLoggedIn;
        this.lines = lines;
    }

    public int calculateFare() {
        int distanceFare = DistanceFarePolicy.calculate(distance);
        int lineFare = new LineFarePolicy(lines).calculate();

        return DiscountPolicy.discount(isLoggedIn, age, distanceFare + lineFare);
    }
}
