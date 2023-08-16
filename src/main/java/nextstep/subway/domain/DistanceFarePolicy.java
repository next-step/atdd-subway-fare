package nextstep.subway.domain;

public class DistanceFarePolicy implements FarePolicy {
    public static final int DEFAULT_PRICE = 1250;
    private int total;

    public DistanceFarePolicy(int total) {
        this.total = total;
    }

    @Override
    public int calculate() {
        int price = DEFAULT_PRICE;

        for (DistanceFareSection policy : DistanceFareSection.values()) {
            if(policy.inRange(total)) {
                price += policy.calculateOverFare(total);
                total = policy.remainDistance(total);
            }
        }

        return price;
    }
}
