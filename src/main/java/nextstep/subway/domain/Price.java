package nextstep.subway.domain;

import lombok.Getter;

@Getter
public class Price {
    public static final int DEFAULT_PRICE = 1250;
    private final int price;

    private Price(int price) {
        this.price = price;
    }

    public static Price calculate(int distance) {
        int price = DEFAULT_PRICE;
        int total = distance;

        for (PathPolicy policy : PathPolicy.values()) {
            if(policy.inRange(total)) {
                price += policy.calculateOverFare(total);
                total = policy.remainDistance(total);
            }
        }

        return new Price(price);
    }
}
