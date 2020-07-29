package nextstep.subway.maps.boarding.domain;

import org.springframework.stereotype.Component;

@Component
public class DistanceProportionFareCalculationPolicy implements FareCalculationPolicy {

    private static final int BASIC_FARE = 1250;
    private static final int FIRST_CHARGING_DISTANCE = 10;
    private static final int SECOND_CHARGING_DISTANCE = 50;
    private static final int FIRST_CHARGING_UNIT = 5;
    private static final int SECOND_CHARGING_UNIT = 8;

    @Override
    public int calculateFare(FareCalculationContext context) {
        final int distance = context.getBoardingDistance();
        final int secondCharging = distance - SECOND_CHARGING_DISTANCE;
        final int firstCharging = secondCharging > 0 ? distance - secondCharging - FIRST_CHARGING_DISTANCE : distance - FIRST_CHARGING_DISTANCE;
        final int result = BASIC_FARE
                + calculateOverFare(firstCharging, FIRST_CHARGING_UNIT)
                + calculateOverFare(secondCharging, SECOND_CHARGING_UNIT);
        context.setCalculationResult(result);
        return result;
    }

    private int calculateOverFare(int distance, int chargingUnit) {
        if (0 >= distance) {
            return 0;
        }
        return (int) ((Math.ceil((distance - 1) / chargingUnit) + 1) * 100);
    }
}
