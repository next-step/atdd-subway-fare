package nextstep.subway.domain;

import nextstep.subway.domain.strategy.ShortestPathStrategies;
import nextstep.subway.domain.strategy.ShortestPathStrategy;
import nextstep.subway.repository.SectionRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class PathFinder {

    private final SectionRepository sectionRepository;

    public PathFinder(SectionRepository sectionRepository) {
        this.sectionRepository = sectionRepository;
    }

    public Path findPath() {
        return findPathBy(PathType.of(""));
    }

    public List<Path> findPaths() {
        return PathType.listOf().stream()
                .map(this::findPathBy)
                .collect(Collectors.toList());
    }

    private Path findPathBy(PathType pathType) {
        List<Section> sections = sectionRepository.findAll();
        var factory = new ShortestPathStrategies();
        ShortestPathStrategy shortestPathStrategy = factory.generateStrategy(ShortestPathType.DIJKSTRA, sections, pathType);
        return new Path(shortestPathStrategy, pathType);
    }
}
