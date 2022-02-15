package nextstep.subway.domain.farepolicy.discount;

public class KidsFareDiscountPolicy implements FareDiscountPolicy {

    public KidsFareDiscountPolicy(int age) {

    }

    public int discount(int totalCost) {
        return 0;
    }
}
