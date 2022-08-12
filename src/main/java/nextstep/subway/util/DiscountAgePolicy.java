package nextstep.subway.util;

public interface DiscountAgePolicy {

    int DEDUCTION_FARE = 350;

    int discount(int fare);
}
