package nextstep.subway.fare;

public abstract class DistanceFarePolicy {
    private static final double OVER_FARE = 100;
    private final DistanceFarePolicy next;

    public DistanceFarePolicy(DistanceFarePolicy next) {
        this.next = next;
    }

    public static Over10FarePolicy setOver50FarePolicy() {
        return new Over10FarePolicy(new Over50FarePolicy(null));
    }

    public final int calculate(int distance) {
        if (isOverFare(distance)) {
            return calculateFare(distance);
        }
        if (next != null) {
            return next.calculate(distance);
        }
        return 0;
    }

    public final int calculateOverFare(int distance, int distancePer) {
        return (int) ((Math.ceil((distance - 11) / distancePer) + 1) * OVER_FARE);
    }

    protected abstract int calculateFare(int distance);

    protected abstract boolean isOverFare(int distance);

}
