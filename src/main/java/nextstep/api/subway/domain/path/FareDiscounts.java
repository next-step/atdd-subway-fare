package nextstep.api.subway.domain.path;

import java.util.stream.Stream;

public enum FareDiscounts {
    TEENAGER(13, 19, 350, 20),
    CHILDREN(6, 13, 350, 50),
    ;

    private final int baseAge;
    private final int limitAge;
    private final long deduction;
    private final long discountPercentage;

    FareDiscounts(final int baseAge, final int limitAge, final long deduction, final long discountPercentage) {
        this.baseAge = baseAge;
        this.limitAge = limitAge;
        this.deduction = deduction;
        this.discountPercentage = discountPercentage;
    }

    public static long discountFareByAge(final long fare, final long age) {
        return Stream.of(values())
                .filter(it -> it.contains(age))
                .findAny()
                .map(it -> it.discount(fare))
                .orElse(fare);
    }

    private boolean contains(final long age) {
        return (baseAge <= age) && (age < limitAge);
    }

    private long discount(final long fare) {
        if (fare <= deduction) {
            return fare;
        }
        return deduction + (fare - deduction) * (100 - discountPercentage) / 100;
    }
}
