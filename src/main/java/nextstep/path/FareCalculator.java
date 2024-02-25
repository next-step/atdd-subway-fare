package nextstep.path;

public class FareCalculator {
    public static int calculateOverFare(int distance) {
        int fare = 0;
        if (distance <= 10) {
            return 1250;
        }

        if (distance <= 50) {
            distance -= 10;
            fare += 1250;

            return fare + (int) ((Math.ceil((distance - 1) / 5) + 1) * 100);
        }

        distance -= 10;
        fare += 1250;

        return fare + (int) ((Math.ceil((distance - 1) / 8) + 1) * 100);
    }
}
