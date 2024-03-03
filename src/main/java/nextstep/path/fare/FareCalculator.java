package nextstep.path.fare;

import nextstep.line.Line;
import nextstep.member.domain.AgeRange;
import nextstep.path.fare.age.AgeFare;
import nextstep.path.fare.distance.*;

import java.util.Set;

public class FareCalculator {

    private DistanceFareChain distanceFareChain;
    private int distance;
    private int maxExtraFare;

    private AgeRange ageRange;

    public FareCalculator(int distance, Set<Line> lines, AgeRange ageRange) {
        this.distance = distance;
        this.ageRange = ageRange;
        this.distanceFareChain = new BaseDistanceFareChain()
                .addNext(new MediumDistanceFareChain())
                .addNext(new LongDistanceFareChain());

        maxExtraFare = findMaxExtraFare(lines);
    }

    public int calculate() {
        AgeFare ageFare = AgeFare.from(ageRange);
        int distanceFare = distanceFareChain.calculate(distance);

        return ageFare.calculate(distanceFare + maxExtraFare);
    }

    private int findMaxExtraFare(Set<Line> lines) {
        return lines.stream()
                .mapToInt(Line::getExtraFare)
                .max()
                .orElse(0);
    }
}
