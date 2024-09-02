package nextstep.subway.fare.domain;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import nextstep.subway.line.domain.entity.LineSection;

public class FareCalculator {

    public static long calculateFare(long distance) {
        return FarePolicy.calculateTotalFare(distance);
    }

    public static long calculateFare(long distance, List<Long> additionalFares) {
        return FarePolicy.calculateTotalFare(distance, additionalFares);
    }

    public static long calculateFare(long distance, List<LineSection> sections, Optional<Integer> age) {
        List<Long> additionalFares = extractAdditionalFares(sections);
        long baseFare = FarePolicy.calculateTotalFare(distance, additionalFares);
        return AgeGroup.of(age.orElse(20)).applyDiscount(baseFare);
    }

    private static List<Long> extractAdditionalFares(List<LineSection> sections) {
        return sections.stream()
            .map(LineSection::getLineAdditionalFare)
            .distinct()
            .collect(Collectors.toList());
    }
}
