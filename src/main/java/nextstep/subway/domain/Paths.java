package nextstep.subway.domain;

import java.util.ArrayList;
import java.util.List;

public class Paths {
    private final List<Path> paths;

    public Paths(List<Path> paths) {
        this.paths = new ArrayList<>(paths);
    }

    public List<Station> findShortestPath(Station source, Station target, PathType pathType) {
        Path path = findPathBy(pathType);
        return path.findShortestPath(source, target);
    }

    public long findShortestValue(Station source, Station target, PathType pathType) {
        Path path = findPathBy(pathType);
        return path.findShortestValue(source, target);
    }

    public List<Section> findEdges(Station source, Station target, PathType pathType) {
        Path path = findPathBy(pathType);
        return path.findShortestEdges(source, target);
    }

    private Path findPathBy(PathType pathType) {
        return paths.stream()
                .filter(path -> path.isSamePathType(pathType))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("해당하는 경로가 존재하지 않습니다."));
    }

}
