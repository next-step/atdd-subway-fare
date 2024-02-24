package nextstep.path;

import nextstep.exception.InvalidInputException;

public enum SearchType {
    DISTANCE {
        @Override
        public PathInfo findPath(PathFinder pathFinder, String sourceId, String targetId) {
            return pathFinder.findShortestPath(sourceId, targetId);
        }
    },
    DURATION {
        @Override
        public PathInfo findPath(PathFinder pathFinder, String sourceId, String targetId) {
            return pathFinder.findFastestPath(sourceId, targetId);
        }
    };

    public abstract PathInfo findPath(PathFinder pathFinder, String sourceId, String targetId);

    public static SearchType from(String type) throws InvalidInputException {
        for (SearchType searchType : values()) {
            if (searchType.name().equalsIgnoreCase(type)) {
                return searchType;
            }
        }

        throw new InvalidInputException("올바른 타입을 입력해주세요.");
    }
}