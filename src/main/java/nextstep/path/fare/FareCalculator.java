package nextstep.path.fare;

import nextstep.line.Line;
import nextstep.member.domain.AgeRange;
import nextstep.path.fare.age.AgeFare;
import nextstep.path.fare.distance.BaseDistanceFare;
import nextstep.path.fare.distance.DistanceFare;
import nextstep.path.fare.distance.LongDistanceFare;
import nextstep.path.fare.distance.MediumDistanceFare;

import java.util.Set;

public class FareCalculator {
    private DistanceFare distanceFare;
    private int distance;
    private int maxExtraFare;

    private AgeRange ageRange;

    public FareCalculator(int distance, Set<Line> lines, AgeRange ageRange) {
        this.distance = distance;
        this.ageRange = ageRange;

        if (distance <= 10) {
            distanceFare = new BaseDistanceFare();
        } else if (distance <= 50) {
            distanceFare = new MediumDistanceFare();
        } else {
            distanceFare = new LongDistanceFare();
        }

        maxExtraFare = findMaxExtraFare(lines);
    }

    public int calculate() {
        AgeFare ageFare = AgeFare.from(ageRange);
        return ageFare.calculate(distanceFare.calculate(distance) + maxExtraFare);
    }

    private int findMaxExtraFare(Set<Line> lines) {
        return lines.stream()
                .mapToInt(Line::getExtraFare)
                .max()
                .orElse(0);
    }
}
