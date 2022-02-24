package nextstep.subway.domain.fare;

public class Fare {
    public static final int BASIC_FARE = 1250;
    public static final int BASIC_DISTANCE_OVER_FARE = 100;

    private int totalFare;
    private int lineOverFare;
    private int distanceOverFare;
    private int memberDiscountFare;

    public Fare() {
    }

    public void impose(int totalDistance, int memberAge, int maxLineFare) {
        this.lineOverFare = maxLineFare;
        applyPolicy(totalDistance, memberAge);
        calculateTotalFare();
    }

    private void applyPolicy(int totalDistance, int memberAge) {
        applyDistancePolicy(totalDistance);
        applyMemberDiscountPolicy(memberAge);
    }

    private void applyDistancePolicy(int totalDistance) {
        DistancePolicy distancePolicy = DistancePolicy.decide(totalDistance);
        distanceOverFare = distancePolicy.calculateOverFare(totalDistance, BASIC_DISTANCE_OVER_FARE);
    }

    private void applyMemberDiscountPolicy(int memberAge) {
        MemberDiscountPolicy memberPolicy = MemberDiscountPolicy.decide(memberAge);
        memberDiscountFare = memberPolicy.calculateDiscountFare(getBeforeDiscountFare());
    }

    private int getBeforeDiscountFare() {
        return BASIC_FARE + lineOverFare + distanceOverFare;
    }

    private void calculateTotalFare() {
        totalFare = BASIC_FARE + lineOverFare + distanceOverFare - memberDiscountFare;
    }

    public int getTotalFare() {
        return totalFare;
    }

    public int getLineOverFare() {
        return lineOverFare;
    }

    public int getDistanceOverFare() {
        return distanceOverFare;
    }

    public int getMemberDiscountFare() {
        return memberDiscountFare;
    }
}
