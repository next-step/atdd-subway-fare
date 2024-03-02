package nextstep.subway.domain;

import nextstep.exception.ApplicationException;
import nextstep.subway.domain.strategy.ShortestPathStrategy;

import java.util.List;

public class Path {

    private ShortestPathStrategy shortestPathStrategy;
    private PathType pathType;

    public Path(ShortestPathStrategy shortestPathStrategy, PathType pathType) {
        this.shortestPathStrategy = shortestPathStrategy;
        this.pathType = pathType;
    }

    public List<Station> findShortestPath(Station source, Station target) {
        validateIsSameBy(source, target);
        return shortestPathStrategy.findShortestPath(source, target);
    }

    private void validateIsSameBy(Station source, Station target) {
        if (source.equals(target)) {
            throw new ApplicationException("출발역과 도착역이 같은 경우 경로를 조회할 수 없습니다.");
        }
    }

    public List<Section> findEdges(Station source, Station target) {
        return shortestPathStrategy.findEdges(source, target);
    }

    public long findShortestValue(Station source, Station target) {
        return shortestPathStrategy.findShortestValue(source, target);
    }

    public boolean isSamePathType(PathType pathType) {
        return this.pathType == pathType;
    }

}
