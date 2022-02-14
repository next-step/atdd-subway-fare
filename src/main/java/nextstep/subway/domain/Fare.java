package nextstep.subway.domain;

import java.util.Arrays;
import java.util.List;
import java.util.function.Function;

public class Fare {
    private final Path path;

    private Fare(final Path path) {
        this.path = path;
    }

    public Fare(final Station upStation, final Station downStation, final List<Line> lines) {
        this(new SubwayMap(lines).findPath(upStation, downStation, PathType.DISTANCE));
    }

    public int calculateFare() {
        int distance = path.extractDistance();

        return Arrays.stream(FaresPerSection.values()).mapToInt(it -> it.calculate(distance)).sum();
    }

    public enum FaresPerSection {
        BASE_SECTION(value -> 1_250),
        FIRST_OVER_SECTION(distance -> {
            if (distance < 10) {
                return 0;
            }
            int excessDistance = Math.min(distance, 50) - 10;
            return calculateOverFare(excessDistance, 5, 100);
        }),
        SECOND_OVER_SECTION(distance -> {
            if (distance < 50) {
                return 0;
            }
            int excessDistance = distance - 50;
            return calculateOverFare(excessDistance, 8, 100);
        }),
        ;

        private final Function<Integer, Integer> expression;

        FaresPerSection(final Function<Integer, Integer> expression) {
            this.expression = expression;
        }

        public int calculate(int distance) {
            return expression.apply(distance);
        }
    }

    private static int calculateOverFare(int distance, int unitDistance, int extraFare) {
        return (int) ((Math.ceil((distance - 1) / unitDistance) + 1) * extraFare);
    }
}
