package nextstep.subway.domain;

import lombok.Getter;

@Getter
public class Price {
    private final int price;

    private Price(int price) {
        this.price = price;
    }

    public static Price calculate(int distance) {
        int price = 1250;
        if (distance <= 10) {
            return new Price(price);
        }
        if (distance <= 50) {
            return new Price(price + getOverFareFirst(distance));
        }
        return new Price(price + getOverFareFirst(50) + getOverFareSecond(distance));
    }


    private static int getOverFareFirst(int distance) {
        return (int) Math.ceil((double) (distance - 10) / 5) * 100;
    }

    private static int getOverFareSecond(double distance) {
        return (int) Math.ceil((double) (distance - 50) / 8) * 100;
    }
}
