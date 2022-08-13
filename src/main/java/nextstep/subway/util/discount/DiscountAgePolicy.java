package nextstep.subway.util.discount;

public interface DiscountAgePolicy {

    int DEDUCTION_FARE = 350;

    int discount(int fare);

    boolean support(int age);
}
