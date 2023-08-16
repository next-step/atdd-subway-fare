package nextstep.subway.domain;

import java.util.Arrays;
import java.util.Optional;
import java.util.function.Predicate;

public enum AgeDiscountSection {
    CHILD((age) -> age >= 6 && age < 13, 350, 0.5),
    TEENAGER((age) -> age >= 13 && age < 19, 350, 0.2);

    private final Predicate<Integer> range;
    private final int defaultFare;
    private final double percent;

    AgeDiscountSection(Predicate<Integer> range, int defaultFare, double percent) {
        this.range = range;
        this.defaultFare = defaultFare;
        this.percent = percent;
    }

    public static Optional<AgeDiscountSection> find(int age) {
        return Arrays.stream(values())
                .filter(ageDiscountSection -> ageDiscountSection.range.test(age))
                .findFirst();
    }

    public int calculate(int fare) {
        return (int) Math.ceil((fare - defaultFare) * (1 - percent));
    }
}