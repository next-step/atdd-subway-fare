package nextstep.subway.path.application;

import org.springframework.stereotype.Component;

@Component("adultFareCalculator")
public class AdultFareCalculator implements FareCalculator {

    public static int ADULT_DEFAULT_FARE = 1250;

    public AdultFareCalculator() {
    }

    public int calculateFare(int distance) {
        int fare = ADULT_DEFAULT_FARE;
        if (distance > 10) {
            fare += calculateOverFare(distance);
        }
        return fare;
    }

    private int calculateOverFare(int distance) {
        int unit = distance < 50 ? 5 : 8;
        return (int)((Math.ceil((distance - 1) / unit) + 1) * 100);
    }

}
