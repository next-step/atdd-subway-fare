package nextstep.subway.domain;

import lombok.Getter;

import java.util.List;

@Getter
public class Fare {
    public static final int DEFAULT_PRICE = 1250;
    private final int fare;

    private Fare(int fare) {
        this.fare = fare;
    }

    public static Fare calculate(Sections sections) {
        List<FarePolicy> farePolicies = List.of(
                new DistanceFarePolicy(sections.totalDistance()),
                new LineFarePolicy(sections.totalLine()));
        int fare = farePolicies.stream()
                .mapToInt(FarePolicy::calculate)
                .sum();
        return new Fare(fare);
    }
}
