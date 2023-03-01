package nextstep.subway.domain.fare;

import nextstep.subway.domain.Line;
import nextstep.subway.domain.Section;
import nextstep.subway.domain.Sections;

import java.util.stream.Stream;

public class Fare {
    public static final int DEFAULT_FARE = 1_250;

    private final int cost;

    public Fare(Sections sections) {
        this(sections, DiscountPolicy.GENERAL);
    }

    public Fare(Sections sections, DiscountPolicy discountPolicy) {
        this(sections.sumByCondition(Section::getDistance),
                sections.allLines()
                        .stream()
                        .mapToInt(Line::getAdditionalFare)
                        .max()
                        .orElse(0),
                discountPolicy
        );
    }

    public Fare(int distance, int additionalFare) {
        this(distance, additionalFare, DiscountPolicy.GENERAL);
    }

    public Fare(int distance, int additionalFare, DiscountPolicy discountPolicy) {
        int fare = DEFAULT_FARE + additionalFare + calculateOverFare(distance);
        this.cost = discountFare(fare, discountPolicy);
    }

    private int calculateOverFare(int distance) {
        return Stream.of(FarePolicy.values())
                .filter(policy -> policy.supported(distance))
                .mapToInt(policy -> policy.additionalFare(distance))
                .sum();
    }

    private int discountFare(int cost, DiscountPolicy discountPolicy) {
        return discountPolicy.apply(cost);
    }

    public int cost() {
        return cost;
    }
}
