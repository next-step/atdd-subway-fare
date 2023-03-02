package nextstep.subway.domain;

import nextstep.member.domain.LoginMember;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.function.Function;

public enum AgeFarePolicy {
    CHILD(6, 12, AgeFarePolicy::calculateChildDiscountFare),
    YOUTH(13, 18, AgeFarePolicy::calculateYouthDiscountFare),
    ADULT(19, Integer.MAX_VALUE, fare -> fare);

    private static final BigDecimal FARE_YOUTH_CHILD_DEDUCTION = new BigDecimal("350");
    private static final BigDecimal PERCENT_DISCOUNT_FIFTY_CHILD = new BigDecimal("0.5");
    private static final BigDecimal PERCENT_DISCOUNT_TWENTY_YOUTH = new BigDecimal("0.2");
    private static final Fare DEDUCTION = new Fare(FARE_YOUTH_CHILD_DEDUCTION);
    private static final Fare CHILD_DISCOUNT_PERCENT = new Fare(PERCENT_DISCOUNT_FIFTY_CHILD);
    private static final Fare YOUTH_DISCOUNT_PERCENT = new Fare(PERCENT_DISCOUNT_TWENTY_YOUTH);

    private final int from;
    private final int to;
    private final Function<Fare, Fare> fareFunction;

    AgeFarePolicy(final int from, final int to, final Function<Fare, Fare> fareFunction) {
        this.from = from;
        this.to = to;
        this.fareFunction = fareFunction;
    }

    public static Fare of(final LoginMember member, final Fare fare) {
        if (member.isGuest()) {
            return fare;
        }
        return findAgeFarePolicy(member.getAge()).fareFunction.apply(fare);
    }

    private static Fare calculateChildDiscountFare(final Fare fare) {
        final Fare deductionFare = fare.minus(DEDUCTION);
        return fare.minus(deductionFare.multiply(CHILD_DISCOUNT_PERCENT));
    }

    private static Fare calculateYouthDiscountFare(final Fare fare) {
        final Fare deductionFare = fare.minus(DEDUCTION);
        return fare.minus(deductionFare.multiply(YOUTH_DISCOUNT_PERCENT));
    }

    private static AgeFarePolicy findAgeFarePolicy(final int age) {
        return Arrays.stream(values())
                .filter(ageFarePolicy -> ageFarePolicy.contain(age))
                .findFirst()
                .orElse(ADULT);
    }

    private boolean contain(final int age) {
        return this.from <= age && this.to >= age;
    }
}
