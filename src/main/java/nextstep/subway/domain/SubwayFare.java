package nextstep.subway.domain;

public class SubwayFare {
    public int calculateOverFare(int distance, int chargeDistance) {
        return (int) ((Math.ceil((distance - 1) / chargeDistance) + 1) * 100);
    }

    public int calculateBasicFare() {
        return 1250;
    }

    public int calculateFare(int distance) {
        if (distance < 0) {
            throw new RuntimeException("거리가 올바르지 않습니다.");
        }

        if (distance > 10 && distance <= 50) {
            return calculateBasicFare() + calculateOverFare(distance - 10, 5);
        }

        if (distance > 50) {
            return calculateBasicFare() + calculateOverFare(distance - 10, 5)
                    + calculateOverFare(distance - 50, 8);
        }

        return calculateBasicFare();
    }
}
