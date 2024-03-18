package nextstep.subway.domain;

public class DistancePolicy implements FarePolicy{
    private final int distance;

    public DistancePolicy(int distance) {
        this.distance = distance;
    }

    @Override
    public int getAdditionalFee() {
        return getFeeOver50Km(distance) + getFeeOver10Km(distance);
    }

    @Override
    public int getDiscountFee() {
        return 0;
    }

    @Override
    public double getDiscountPercent() {
        return 0;
    }

    private int getFeeOver50Km(int distance) {
        int extraDistance = distance - 50;

        if(extraDistance <= 0) {
            return 0;
        }

        return (int) ((Math.ceil((extraDistance - 1) / 8) + 1) * 100);
    }

    private int getFeeOver10Km(int distance) {
        if(distance >= 50) {
            return 800;
        }

        int extraDistance = distance - 10;

        if(extraDistance <= 0) {
            return 0;
        }

        return (int) ((Math.ceil((extraDistance - 1) / 5) + 1) * 100);
    }
}
