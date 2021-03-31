package nextstep.subway.path.domain;

import nextstep.subway.path.domain.policy.DiscountPolicy;

public class Discount {

    private int fare;

    public Discount(int fare) {
        this.fare = fare;
    }

    public int getFare() {
        return fare;
    }

    public static int excute(int fare, DiscountPolicy... discountPolicy) {
        int discountedFare = fare;
        for (DiscountPolicy discount : discountPolicy) {
            discountedFare = discount.discount(discountedFare);
        }
        return new Discount(discountedFare).getFare();
    }
}
