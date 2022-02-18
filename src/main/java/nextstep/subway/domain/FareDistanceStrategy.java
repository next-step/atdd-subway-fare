package nextstep.subway.domain;

import java.math.BigDecimal;

public class FareDistanceStrategy implements FareCalculator {

    private static BigDecimal BASIC_FARE = BigDecimal.valueOf(1_250);

    @Override
    public Fare calculate(Path path) {
        return calculate(path.extractDistance());
    }

    private Fare calculate(int distance) {
        BigDecimal fare = BASIC_FARE;
        if (distance <= AdditionalFare.OVER_10KM.startChargingDistance) {
            return Fare.of(fare);
        }

        fare = fare.add(calculateUnder50Km(distance));
        fare = fare.add(calculateOver50Km(distance));

        return Fare.of(fare);
    }

    private BigDecimal calculateUnder50Km(int distance) {
        int fareDistance = distance;
        if (distance > AdditionalFare.OVER_50KM.startChargingDistance) {
            fareDistance = distance - (distance - AdditionalFare.OVER_50KM.startChargingDistance);
        }
        fareDistance -= AdditionalFare.OVER_10KM.startChargingDistance;
        return overFare(fareDistance, AdditionalFare.OVER_10KM);
    }

    private BigDecimal calculateOver50Km(int distance) {
        if (distance <= AdditionalFare.OVER_50KM.startChargingDistance) {
            return BigDecimal.ZERO;
        }
        int fareDistance = distance - AdditionalFare.OVER_50KM.startChargingDistance;
        return overFare(fareDistance, AdditionalFare.OVER_50KM);
    }

    private BigDecimal overFare(int distance, AdditionalFare additionalFare) {
        int addCount = (int) (Math.ceil((distance - 1) / additionalFare.rechargeUnitDistance) + 1);
        return additionalFare.additionalFare.multiply(BigDecimal.valueOf(addCount));
    }

    private enum AdditionalFare {

        OVER_10KM(10, 5, BigDecimal.valueOf(100)),
        OVER_50KM(50, 8, BigDecimal.valueOf(100));

        private int startChargingDistance;
        private int rechargeUnitDistance;
        private BigDecimal additionalFare;

        AdditionalFare(int startChargingDistance, int rechargeUnitDistance, BigDecimal additionalFare) {
            this.startChargingDistance = startChargingDistance;
            this.rechargeUnitDistance = rechargeUnitDistance;
            this.additionalFare = additionalFare;
        }

    }

}
