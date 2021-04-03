package nextstep.subway.path.domain;

import nextstep.subway.line.domain.Line;
import nextstep.subway.member.domain.LoginMember;

import java.util.List;

public class FareCalculationStrategyFactory {

    public static final int DEFAULT_FARE_DISTANCE = 10;
    public static final int ADD_100_FARE_DISTANCE = 50;

    public static FareCalculationStrategy of(PathResult pathResult, LoginMember member) {
        int distance = pathResult.getTotalDistance();
        int fareByDistance = getFareByDistance(distance);

        List<Line> lines = pathResult.getSections().getLinesIncluded();
        int addedFareByLines = getFareAddedByLines(lines);

        int baseFare = fareByDistance + addedFareByLines;
        return getFinalFareByAge(baseFare, member);
    }

    private static int getFareByDistance(int distance) {
        if (distance <= DEFAULT_FARE_DISTANCE) {
            return new DistanceUnder10FareStrategy(distance).calculate();
        }
        if (distance <= ADD_100_FARE_DISTANCE) {
            return new DistanceOver10Under50FareStrategy(distance).calculate();
        }
        return new DistanceOver50FareStrategy(distance).calculate();
    }

    private static int getFareAddedByLines(List<Line> lines) {
        return lines.stream()
                    .mapToInt(Line::getAdditionalFare)
                    .max()
                    .orElse(0);
    }

    private static FareCalculationStrategy getFinalFareByAge(int baseFare, LoginMember member) {
        if (member == null) {
            return new AgeDefaultFareStrategy(baseFare);
        }
        if (member.getAge() >= 6 && member.getAge() < 13) {
            return new AgeChildFareStrategy(baseFare);
        }
        if (member.getAge() >= 13 && member.getAge() < 19) {
            return new AgeAdolescentFareStrategy(baseFare);
        }
        return new AgeDefaultFareStrategy(baseFare);
    }

}
