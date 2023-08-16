package nextstep.subway.domain;

import lombok.Getter;
import nextstep.member.domain.Member;

import java.util.List;
import java.util.Optional;

@Getter
public class Fare {
    public static final int DEFAULT_PRICE = 1250;
    private final int fare;

    public Fare(int fare) {
        this.fare = fare;
    }

    public static Fare calculate(Sections sections) {
        int fare = additionalFare(sections);
        return new Fare(fare);
    }

    public static Fare calculate(Sections sections, Member member) {
        int fare = additionalFare(sections);

        Optional<AgeDiscountSection> section = AgeDiscountSection.find(member.getAge());
        if (section.isPresent()) {
            fare = section.get().calculate(fare);
        }
        return new Fare(fare);
    }

    private static int additionalFare(Sections sections) {
        List<FarePolicy> farePolicies = List.of(
                new DistanceFarePolicy(sections.totalDistance()),
                new LineFarePolicy(sections.totalLine()));
        return farePolicies.stream()
                .mapToInt(FarePolicy::calculate)
                .sum();
    }

}
