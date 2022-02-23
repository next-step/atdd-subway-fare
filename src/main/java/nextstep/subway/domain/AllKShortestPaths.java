package nextstep.subway.domain;

import static nextstep.subway.utils.StringDateTimeConverter.convertDateTimeToString;
import static nextstep.subway.utils.StringDateTimeConverter.convertStringToDateTime;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.KShortestPaths;

public class AllKShortestPaths {

    private List<GraphPath<Station, SectionEdge>> paths;

    public AllKShortestPaths(KShortestPaths paths, Station source, Station target) {
        this.paths = paths.getPaths(source, target);
    }

    public ShortestPaths getShortestPathsFrom(String startTimeString) {
        return findShortest(paths, startTimeString);
    }

    private ShortestPaths findShortest(List<GraphPath<Station, SectionEdge>> paths, String startTimeString) {
        LocalDateTime startTime = convertStringToDateTime(startTimeString);

        Path shortestDistancePath = findShortestDistancePath(paths, startTime);
        Path shortestDurationPath = findShortestDurationPath(paths, startTime);

        return new ShortestPaths(shortestDurationPath, shortestDistancePath);
    }

    private Path findShortestDurationPath(List<GraphPath<Station, SectionEdge>> paths, LocalDateTime startTime) {
        PathsFinder pathsFinder = new PathsFinder(startTime);

        for (GraphPath<Station, SectionEdge> path : paths) {
            List<Section> sectionList = sectionsFromGraphPath(path);

            pathsFinder.processUsingArrivalTime(sectionList);
        }

        return Path.of(
            new Sections(pathsFinder.getShortestSectionList())
            , pathsFinder.totalDistance()
            , convertDateTimeToString(pathsFinder.getFastestArrivalTime())
        );
    }

    private Path findShortestDistancePath(List<GraphPath<Station, SectionEdge>> paths, LocalDateTime startTime) {
        int shortestFareDistance = Integer.MAX_VALUE;
        List<Section> sectionList;

        PathsFinder pathsFinder = new PathsFinder(startTime);

        for (GraphPath<Station, SectionEdge> path : paths) {
            sectionList = sectionsFromGraphPath(path);

            pathsFinder.processUsingDistance(sectionList);
        }

        return Path.of(
            new Sections(pathsFinder.getShortestSectionList())
            , shortestFareDistance
            , convertDateTimeToString(pathsFinder.getShortestDistanceArrivalTime())
        );
    }

    private List<Section> sectionsFromGraphPath(GraphPath<Station, SectionEdge> graphPath) {
        return graphPath.getEdgeList().stream()
            .map(SectionEdge::getSection)
            .collect(Collectors.toList());
    }
}
