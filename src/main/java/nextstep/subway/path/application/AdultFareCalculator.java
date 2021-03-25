package nextstep.subway.path.application;

import org.springframework.stereotype.Component;

@Component("adultFareCalculator")
public class AdultFareCalculator implements FareCalculator {

    public static int ADULT_DEFAULT_FARE = 1250;

    public AdultFareCalculator() {
    }

    public int calculateFare(int distance) {
        int fare = ADULT_DEFAULT_FARE;
        if (distance > OverFareCalculator.FIRST.getSection()) {
            fare += OverFareCalculator.FIRST.calculate(Math.min(distance, 50));
        }
        if (distance > OverFareCalculator.SECOND.getSection()) {
            fare += OverFareCalculator.SECOND.calculate(distance);
        }
        return fare;
    }

    private enum OverFareCalculator {
        FIRST(10, 5),
        SECOND(50, 8);

        private final int section;
        private final int unit;

        OverFareCalculator(int section, int unit) {
            this.section = section;
            this.unit = unit;
        }

        public int getSection() {
            return section;
        }

        public int getUnit() {
            return unit;
        }

        public int calculate(int distance) {
            return (int)((Math.ceil((distance - section - 1) / unit) + 1) * 100);
        }
    }
}
