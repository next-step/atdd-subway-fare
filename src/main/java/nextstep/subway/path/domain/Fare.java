package nextstep.subway.path.domain;

import nextstep.subway.path.domain.enumeration.DiscountType;
import nextstep.subway.path.domain.enumeration.FareDistanceType;

public class Fare {

    private final static int DEFAULT_FARE = 1_250;
    private int cost;
    private int totalDistance;
    private FareDistanceType distanceType;
    private int additionalFee;

    public static Fare createInstance(int totalDistance, int additionalFee) {
       return new Fare(DEFAULT_FARE, totalDistance, FareDistanceType.typeFromDistance(totalDistance), additionalFee);
    }

    private Fare(int cost, int totalDistance, FareDistanceType distanceType, int additionalFee) {
        this.cost = cost;
        this.totalDistance = totalDistance;
        this.distanceType = distanceType;
        this.additionalFee = additionalFee;
    }

    public void calculateCost() {
        calculateCostByDistance();
        applyAdditionalFee();
    }

    private void calculateCostByDistance() {
        this.cost = DEFAULT_FARE;

        //거리에 따른 요금 계산
        if (totalDistance > 0) {
            this.cost += this.distanceType.calucate(this.totalDistance);
        }
    }

    private void applyAdditionalFee() {
        this.cost += this.additionalFee;
    }

    public void discountByAge(int age) {
        DiscountType discountType = DiscountType.typeFromAge(age);
        this.cost -= discountType.discount(this.cost);
    }

    public int getCost() {
        return this.cost;
    }
}
