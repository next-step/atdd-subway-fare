package nextstep.subway.fare;

public class FareUtil {
    private static final int DEFAULT_FARE = 1250;

    private FareUtil() {
        throw new IllegalArgumentException("Util 클래스는 생성자를 생성할 수 없습니다.");
    }

    public static int getFare(int distance) {
        return DEFAULT_FARE + Over10FarePolicy.setOver50FarePolicy().calculate(distance);
    }
}
