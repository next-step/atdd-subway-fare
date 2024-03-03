package nextstep.path;

import nextstep.exception.InvalidInputException;
import nextstep.station.Station;

public enum SearchType {
    DISTANCE {
        @Override
        public Path findPath(PathFinder pathFinder, Station source, Station target) {
            return pathFinder.findShortestPath(source, target);
        }
    },
    DURATION {
        @Override
        public Path findPath(PathFinder pathFinder, Station source, Station target) {
            return pathFinder.findFastestPath(source, target);
        }
    };

    public abstract Path findPath(PathFinder pathFinder, Station source, Station target);

    public static SearchType from(String type) throws InvalidInputException {
        for (SearchType searchType : values()) {
            if (searchType.name().equalsIgnoreCase(type)) {
                return searchType;
            }
        }

        throw new InvalidInputException("올바른 타입을 입력해주세요.");
    }
}