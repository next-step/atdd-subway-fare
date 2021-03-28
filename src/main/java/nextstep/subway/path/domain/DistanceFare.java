package nextstep.subway.path.domain;

public class DistanceFare {
    public static final int BASE_FARE = 1250;

    public int calculate(int distance){
        return BASE_FARE + calculate10KmOverFare(distance) + calculate50KmOverFare(distance);
    }

    private int calculate10KmOverFare(int distance) {
        if (distance < 10) {
            return 0;
        }
        return (int) ((Math.ceil((distance - 10) / 5) + 1) * 100);
    }

    private int calculate50KmOverFare(int distance) {
        if (distance < 50) {
            return 0;
        }
        return (int) ((Math.ceil((distance - 50) / 8) + 1) * 100);
    }
}
