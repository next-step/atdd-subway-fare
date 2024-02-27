package nextstep.path.fare;

import nextstep.line.Line;

import java.util.List;
import java.util.Set;

public class FareCalculator {
    private Fare fare;
    private int distance;

    private int maxExtraFare;

    public FareCalculator(int distance, Set<Line> lines) {
        this.distance = distance;

        if (distance <= 10) {
            fare = new BaseFare();
        } else if (distance <= 50) {
            fare = new MediumFare();
        } else {
            fare = new LongFare();
        }

        maxExtraFare = findMaxExtraFare(lines);
    }

    public int calculate() {
        return fare.calculate(distance) + maxExtraFare;
    }

    private int findMaxExtraFare(Set<Line> lines) {
        return lines.stream()
                .mapToInt(Line::getExtraFare)
                .max()
                .orElse(0);
    }
}
