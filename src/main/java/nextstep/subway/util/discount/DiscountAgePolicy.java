package nextstep.subway.util.discount;

@FunctionalInterface
public interface DiscountAgePolicy {

    int DEDUCTION_FARE = 350;

    int discount(int fare);
}
