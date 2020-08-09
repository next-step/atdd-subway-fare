package nextstep.subway.maps.map.application;

import org.springframework.stereotype.Component;

import nextstep.subway.maps.line.domain.Money;
import nextstep.subway.maps.map.domain.DiscountPolicy;
import nextstep.subway.maps.map.domain.SubwayPath;

@Component
public class DiscountFareCalculator {

    public static final int BASIC_FARE_VALUE = 1250;
    public static final int OVERFARE_ADDED_PRICE_PER_KM_VALUE = 100;

    public static final Money BASIC_FARE = Money.drawNewMoney(BASIC_FARE_VALUE);
    public static final Money ADDITIONAL_FARE = Money.drawNewMoney(OVERFARE_ADDED_PRICE_PER_KM_VALUE);

    public static final int OVERFARE_DISTANCE_THRESHOLD = 10;
    public static final int SUPER_OVERFARE_DISTANCE_THRESHOLD = 50;

    public static final int OVERFARE_DISTANCE_DIVIDEND = 5;
    public static final int SUPER_OVERFARE_DISTANCE_DIVIDEND = 8;

    public Money calculate(SubwayPath path, DiscountPolicy discountPolicy) {
        return discountPolicy.discount(calculate(path));
    }

    public Money calculate(SubwayPath subwayPath) {
        int distance = subwayPath.calculateDistance();
        Money overFare = subwayPath.calculateMaxLineExtraFare();
        if (distance > SUPER_OVERFARE_DISTANCE_THRESHOLD) {
            int overDistance = distance - SUPER_OVERFARE_DISTANCE_THRESHOLD;
            overFare = overFare.plus(calculateOverFareWithMoney(overDistance, SUPER_OVERFARE_DISTANCE_DIVIDEND));
            distance = distance - overDistance;
        }
        if (distance > OVERFARE_DISTANCE_THRESHOLD) {
            int overDistance = distance - OVERFARE_DISTANCE_THRESHOLD;
            overFare = overFare.plus(calculateOverFareWithMoney(overDistance, OVERFARE_DISTANCE_DIVIDEND));
        }
        return BASIC_FARE.plus(overFare);
    }

    private Money calculateOverFareWithMoney(int overDistance, int unit) {
        return Money.drawNewMoney(calculateOverFare(overDistance, unit));
    }

    private int calculateOverFare(int overDistance, int unit) {
        return (int)((Math.ceil(overDistance - 1) / unit) + 1) * ADDITIONAL_FARE.extractValue();
    }
}
