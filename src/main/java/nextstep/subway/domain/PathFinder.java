package nextstep.subway.domain;

import nextstep.subway.factory.ShortestPathFactory;
import nextstep.subway.repository.SectionRepository;
import nextstep.subway.strategy.PathType;
import nextstep.subway.strategy.ShortestPathStrategy;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class PathFinder {

    private final SectionRepository sectionRepository;

    public PathFinder(SectionRepository sectionRepository) {
        this.sectionRepository = sectionRepository;
    }

    public Path findPath() {
        return findPathBy(PathType.DISTANCE);
    }

    public Path findPath(PathType pathType) {
        return findPathBy(pathType);
    }

    private Path findPathBy(PathType pathType) {
        List<Section> sections = sectionRepository.findAll();
        var factory = new ShortestPathFactory();
        ShortestPathStrategy shortestPathStrategy = factory.generateStrategy(ShortestPathType.DIJKSTRA, sections, pathType);
        return new Path(shortestPathStrategy);
    }
}
