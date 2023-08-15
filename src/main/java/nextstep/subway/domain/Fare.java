package nextstep.subway.domain;

import lombok.Getter;
import nextstep.member.domain.Member;

import java.util.List;
import java.util.Optional;

@Getter
public class Fare {
    public static final int DEFAULT_PRICE = 1250;
    private int fare;

    public Fare(int fare) {
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

    public void discount(Member member) {
        Optional<AgeDiscountSection> section = AgeDiscountSection.find(member.getAge());
        section.ifPresent(ageDiscountSection -> {
            fare = ageDiscountSection.calculate(fare);
        });
    }
}
