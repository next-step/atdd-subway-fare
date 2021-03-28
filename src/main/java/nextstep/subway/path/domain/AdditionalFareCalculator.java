package nextstep.subway.path.domain;

import nextstep.subway.line.domain.Line;
import nextstep.subway.path.exception.IsNotPositiveDistanceException;

import java.util.List;

public class AdditionalFareCalculator {
    public static final int OVER_FARE_UNIT = 100;

    private final int lineAdditionalFare;

    public AdditionalFareCalculator(int lineAdditionalFare) {
        this.lineAdditionalFare = lineAdditionalFare;
    }

    public AdditionalFareCalculator(List<Line> lineOfPath) {
        this(lineOfPath.stream()
                       .map(Line::getAdditionalFare)
                       .reduce(0, Math::max));
    }

    public int calculate(int distance) {
        validatePositiveNumber(distance);
        return lineAdditionalFare + OverFareCalculator.calculate(distance);
    }

    private static void validatePositiveNumber(int distance) {
        if (distance < 1) {
            throw new IsNotPositiveDistanceException();
        }
    }

    private enum OverFareCalculator {
        FIRST(10, 5.0),
        SECOND(50, 8.0);

        private final int overDistance;
        private final double distanceUnit;

        OverFareCalculator(int section, double distanceUnit) {
            this.overDistance = section;
            this.distanceUnit = distanceUnit;
        }

        public static int calculate(int distance) {
            int additionalFare = 0;
            if (distance > FIRST.overDistance) {
                additionalFare += FIRST.apply(Math.min(distance, SECOND.overDistance));
            }
            if (distance > SECOND.overDistance) {
                additionalFare += SECOND.apply(distance);
            }
            return additionalFare;
        }

        private int apply(int distance) {
            return (int)(getAdditionalFareOf(distance) * OVER_FARE_UNIT);
        }

        private double getAdditionalFareOf(int distance) {
            int remainderDistance = distance - overDistance;
            return Math.ceil((remainderDistance - 1) / distanceUnit) + 1;
        }
    }
}
