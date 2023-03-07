package nextstep.subway.domain.fare;

public class OverFiftyKmFare implements FarePolicy {
    public static final int OVER_50KM = 50;

    @Override
    public int calculateOverFare(int distance) {
        if (distance <= OVER_50KM) {
            return 0;
        }
        
        return (int) ((Math.ceil((distance - OVER_50KM - 1) / 8) + 1) * 100);
    }
}
