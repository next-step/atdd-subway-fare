package nextstep.subway.domain.fare;

public class DistancePolicy implements FarePolicy{
    private final int distance;

    public DistancePolicy(int distance) {
        this.distance = distance;
    }

    @Override
    public int applyAdditionalFare(int fare) {
        return fare + getFareOver50Km(distance) + getFareOver10Km(distance);
    }

    @Override
    public int applyDiscountFare(int fare) {
        return fare;
    }

    @Override
    public double applyDiscountPercent(int fare) {
        return fare;
    }

    private int getFareOver50Km(int distance) {
        int extraDistance = distance - 50;

        if(extraDistance <= 0) {
            return 0;
        }

        return (int) ((Math.ceil((extraDistance - 1) / 8) + 1) * 100);
    }

    private int getFareOver10Km(int distance) {
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
