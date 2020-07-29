package nextstep.subway.maps.map.application;

import nextstep.subway.maps.line.domain.Money;
import nextstep.subway.maps.map.domain.SubwayPath;
import org.springframework.stereotype.Component;

@Component
public class FareCalculator {

    public static final Money BASIC_FARE = Money.wons(1250);
    public static final Money ADDITIONAL_FARE = Money.wons(100);
    public static final int OVER_FARE_DISTANCE = 10;
    private static final int SUPER_OVER_FARE_DISTANCE = 50;
    public static final int SUPER_OVER_FARE_CHARGE_UNIT = 8;
    public static final int OVER_FARE_CHARGE_UNIT = 5;

    public Money calculate(SubwayPath path) {
        int distance = path.calculateDistance();

        Money overFare = path.calculateMaxLineExtraFare();
        if (distance > SUPER_OVER_FARE_DISTANCE) {
            int overDistance = distance - SUPER_OVER_FARE_DISTANCE;
            overFare = overFare.plus(calculateOverFare(overDistance, SUPER_OVER_FARE_CHARGE_UNIT));
            distance -= overDistance;
        }
        if (distance > OVER_FARE_DISTANCE) {
            int overDistance = distance - OVER_FARE_DISTANCE;
            overFare = overFare.plus(calculateOverFare(overDistance, OVER_FARE_CHARGE_UNIT));
        }
        return BASIC_FARE.plus(overFare);
    }

    private Money calculateOverFare(int overDistance, int unit) {
        return Money.wons(calculateOverFareInteger(overDistance, unit));
    }

    private int calculateOverFareInteger(int overDistance, int unit) {
        return (int) ((Math.ceil((overDistance - 1) / unit) + 1) * ADDITIONAL_FARE.amount());
    }


}
