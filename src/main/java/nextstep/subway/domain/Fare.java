package nextstep.subway.domain;

import java.util.List;

public class Fare {
    private final int distance;
    private final int age;
    private final List<String> lineNames;

    public Fare(final int distance, final int age, final List<String> lineNames) {
        this.distance = distance;
        this.age = age;
        this.lineNames = lineNames;
    }

    public int calculateFare() {
        int distanceFare = DistanceFarePolicy.calculate(distance);
        int lineFare = LineFarePolicy.calculate(lineNames);

        return DiscountPolicy.discount(age, distanceFare + lineFare);
    }
}
