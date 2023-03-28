package nextstep.subway.domain.path;

import java.util.List;

import nextstep.subway.domain.Line;

public class PathFinderFactory {

    public static PathFinder getPathFinder(List<Line> lines, PathSearch type) {
        switch (type) {
            case DISTANCE:
                return new DistancePathFinder(lines);
            case DURATION:
                return new DurationPathFinder(lines);
        }
        throw new IllegalArgumentException("유효하지 않은 PathSearch 타입입니다.");
    }

}
