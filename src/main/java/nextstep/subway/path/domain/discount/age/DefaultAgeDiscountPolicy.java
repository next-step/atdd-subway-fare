package nextstep.subway.path.domain.discount.age;

public class DefaultAgeDiscountPolicy extends AgeDiscountPolicy {
    @Override
    public int discount(int totalFare) {
        return totalFare;
    }
}
