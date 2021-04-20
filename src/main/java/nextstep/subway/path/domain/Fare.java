package nextstep.subway.path.domain;

import nextstep.subway.path.domain.enumeration.DiscountType;
import nextstep.subway.path.domain.enumeration.FareDistanceType;

public class Fare {

    private final static int DEFAULT_FARE = 1_250;
    private int cost;
    private int totalDistance;
    private FareDistanceType distanceType;
    private DiscountType discountType;
    private int additionalFee;

    public static Fare createInstance(int totalDistance, int additionalFee, int age) {
       return new Fare(DEFAULT_FARE, totalDistance, FareDistanceType.typeFromDistance(totalDistance), DiscountType.typeFromAge(age), additionalFee);
    }

    private Fare(int cost, int totalDistance, FareDistanceType distanceType, DiscountType discountType, int additionalFee) {
        this.cost = cost;
        this.totalDistance = totalDistance;
        this.distanceType = distanceType;
        this.discountType = discountType;
        this.additionalFee = additionalFee;
    }

    public Fare calculateCost() {
        this.cost = DEFAULT_FARE;

        //거리에 따른 요금 계산
        if (totalDistance > 0) {
            this.cost += this.distanceType.calucate(this.totalDistance);
        }

        //추가요금
        this.cost += additionalFee;

        System.out.println(this.discountType);
        //로그인 계정에 따른 요금 계산
        this.cost = this.cost - this.discountType.discount(this.cost);

        return this;
    }

    public int getCost() {
        return this.cost;
    }
}
