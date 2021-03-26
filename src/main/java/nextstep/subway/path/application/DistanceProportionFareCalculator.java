package nextstep.subway.path.application;

import nextstep.subway.line.domain.LineFare;

public class DistanceProportionFareCalculator implements FareCalculator {

    private final LineFare lineFare;
    private final int lineAdditionalFare;


    public DistanceProportionFareCalculator(LineFare lineFare, int lineAdditionalFare) {
        this.lineFare = lineFare;
        this.lineAdditionalFare = lineAdditionalFare;
    }

    public DistanceProportionFareCalculator(LineFare lineFare) {
        this(lineFare, 0);
    }

    public int calculateFare(int distance) {
        int fare = lineFare.getFare();
        if (distance > OverFareCalculator.FIRST.getSection()) {
            fare += OverFareCalculator.FIRST.calculate(Math.min(distance, 50));
        }
        if (distance > OverFareCalculator.SECOND.getSection()) {
            fare += OverFareCalculator.SECOND.calculate(distance);
        }
        return fare + lineFare.getDiscountRateOf(fare) + lineAdditionalFare;
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
