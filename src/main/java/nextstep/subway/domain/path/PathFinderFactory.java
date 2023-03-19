package nextstep.subway.domain.path;

import nextstep.subway.domain.Line;

import java.util.List;

public class PathFinderFactory {

    public static PathFinder drawMap(final List<Line> lines, final PathType type) {
        switch (type) {
            case DISTANCE:
                return new DistancePathFinder(lines);
            case DURATION:
                return new DurationPathFinder(lines);
        }

        throw new IllegalArgumentException("경로 조회 타입이 유효하지 않습니다.");
    }
}
