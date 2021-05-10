package nextstep.subway.path.domain;

import nextstep.subway.member.domain.LoginMember;

public class Fare {

    private final static int DEFAULT_FARE = 1_250;

    private int cost;

    private int additionalFee;
    private int totalDistance;

    private LoginMember loginMember;


    public static Fare createInstance(int additionalFee, int totalDistance, LoginMember loginMember) {
       return new Fare(DEFAULT_FARE, additionalFee, totalDistance, loginMember);
    }

    private Fare(int cost, int additionalFee, int totalDistance, LoginMember loginMember) {
        this.cost = cost;
        this.additionalFee = additionalFee;
        this.totalDistance = totalDistance;
        this.loginMember= loginMember;
    }

    public int calculateCost() {
        calculateCostByDistance();
        applyAdditionalFee();

        discountByUser();
        return this.cost;
    }

    private void calculateCostByDistance() {
        FarePolicy distancePolicy = new FareDistancePolicy(this.totalDistance);
        this.cost = distancePolicy.calculate(this.cost);
    }

    private void applyAdditionalFee() {
        this.cost += this.additionalFee;
    }

    public void discountByUser() {
        FarePolicy discountPolicy = new FareDiscountPolicy(loginMember);
        this.cost = discountPolicy.calculate(this.cost);
    }
}
