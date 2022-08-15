package nextstep.subway.price.distance;

import nextstep.subway.price.PricePolicy;
import org.springframework.stereotype.Component;

public class DistancePricePolicy {
    private static final int BASIC_PRICE = 1250;
    private static final int BASE_DISTANCE = 10;
    private static final int DIVISION_ONE_DISTANCE = 50;
    private static final int DIVISION_ONE_BASE_DISTANCE = 5;
    private static final int OVER_DIVISION_ONE_BASE_DISTANCE = 8;

    private int totalDistance;

    public DistancePricePolicy(int totalDistance) {
        if (totalDistance <= 0) {
            throw new IllegalArgumentException("지하철 이동거리가 잘못되었습니다.");
        }
        this.totalDistance = totalDistance;
    }

    public int calculatePrice() {

        if (this.totalDistance < BASE_DISTANCE) {
            return BASIC_PRICE;
        }

        return BASIC_PRICE
            + calculateOverFare(getDivisionOneDistance(), DIVISION_ONE_BASE_DISTANCE)
            + calculateOverFare(getDivisionOverOneDistance(), OVER_DIVISION_ONE_BASE_DISTANCE);
    }

    private int getDivisionOneDistance() {
        if (this.totalDistance <= BASE_DISTANCE) {
            return 0;
        }
        return this.totalDistance - BASE_DISTANCE - (getOverDistanceThanDivisionOne());

    }

    private int getOverDistanceThanDivisionOne() {
        if (this.totalDistance - DIVISION_ONE_DISTANCE <= 0) {
            return 0;
        }
        return this.totalDistance - DIVISION_ONE_DISTANCE;
    }

    private int getDivisionOverOneDistance() {
        if (this.totalDistance <= DIVISION_ONE_DISTANCE) {
            return 0;
        }

        return this.totalDistance - DIVISION_ONE_DISTANCE;

    }

    private int calculateOverFare(int distance, int baseOfDistance) {
        if (distance <= 0) {
            return 0;
        }
        return (int)((Math.ceil((distance - 1) / baseOfDistance) + 1) * 100);
    }
}
