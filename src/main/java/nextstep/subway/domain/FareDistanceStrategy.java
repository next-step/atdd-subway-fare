package nextstep.subway.domain;

import java.math.BigDecimal;

public class FareDistanceStrategy implements FareCalculator {

    private static BigDecimal BASIC_FARE = BigDecimal.valueOf(1250);

    @Override
    public BigDecimal calculate(int distance) {
        BigDecimal fare = BASIC_FARE;
        if (distance <= 10) {
            return fare;
        }

        fare = fare.add(calculateUnder50Km(distance));
        fare = fare.add(calculateOver50Km(distance));

        return fare;
    }

    private BigDecimal calculateUnder50Km(int distance) {
        int fareDistance = distance;
        if (distance > AdditionalFare.OVER_50KM.critical) {
            fareDistance = distance - (distance - AdditionalFare.OVER_50KM.critical);
        }
        fareDistance -= AdditionalFare.OVER_10KM.critical;
        return overFare(fareDistance, AdditionalFare.OVER_10KM);
    }

    private BigDecimal calculateOver50Km(int distance) {
        if (distance <= AdditionalFare.OVER_50KM.critical) {
            return BigDecimal.ZERO;
        }
        int fareDistance = distance - AdditionalFare.OVER_50KM.critical;
        return overFare(fareDistance, AdditionalFare.OVER_50KM);
    }

    private BigDecimal overFare(int distance, AdditionalFare additionalFare) {
        int addCount = (int) (Math.ceil((distance - 1) / additionalFare.cycleDistance) + 1);
        return additionalFare.additionalFare.multiply(BigDecimal.valueOf(addCount));
    }

    private enum AdditionalFare {

        OVER_10KM(10, 5, BigDecimal.valueOf(100)),
        OVER_50KM(50, 8, BigDecimal.valueOf(100));

        private int critical;
        private int cycleDistance;
        private BigDecimal additionalFare;

        AdditionalFare(int critical, int cycleDistance, BigDecimal additionalFare) {
            this.critical = critical;
            this.cycleDistance = cycleDistance;
            this.additionalFare = additionalFare;
        }

    }

}
