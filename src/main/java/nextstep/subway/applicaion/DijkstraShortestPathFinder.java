package nextstep.subway.applicaion;

import nextstep.common.exception.NoPathConnectedException;
import nextstep.common.exception.NoRegisterStationException;
import nextstep.common.exception.SameStationException;
import nextstep.subway.domain.Line;
import nextstep.subway.domain.Path;
import nextstep.subway.domain.SearchType;
import nextstep.subway.domain.Section;
import nextstep.subway.domain.SectionEdge;
import nextstep.subway.domain.Sections;
import nextstep.subway.domain.Station;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.SimpleDirectedWeightedGraph;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

import static nextstep.common.error.SubwayError.NO_FIND_SAME_SOURCE_TARGET_STATION;
import static nextstep.common.error.SubwayError.NO_PATH_CONNECTED;
import static nextstep.common.error.SubwayError.NO_REGISTER_LINE_STATION;

@Component
public class DijkstraShortestPathFinder implements ShortestPathFinder {

    final SimpleDirectedWeightedGraph<Station, SectionEdge> graph = new SimpleDirectedWeightedGraph<>(SectionEdge.class);
    final DijkstraShortestPath<Station, SectionEdge> dijkstraShortestPath = new DijkstraShortestPath<>(this.graph);

    @Override
    public Path findPath(final List<Line> lines, final Station source, final Station target, final SearchType searchType) {
        validateSameStation(source, target);
        addVertexFromLine(lines);
        addEdgeByLine(lines, searchType);
        addEdgeByLineOppositely(lines, searchType);
        return Path.from(new Sections(findShortestSections(source, target)));
    }

    private void addEdgeByLineOppositely(final List<Line> lines, final SearchType searchType) {
        lines.stream()
                .flatMap(line -> line.getSections().stream())
                .map(section -> new Section(section.getLine(), section.getDownStation(), section.getUpStation(), section.getDistance(), section.getDuration()))
                .forEach(section -> configEdge(section, searchType));
    }

    private void addEdgeByLine(final List<Line> lines, final SearchType searchType) {
        lines.stream()
                .flatMap(line -> line.getSections().stream())
                .forEach(section -> configEdge(section, searchType));
    }

    private void addVertexFromLine(final List<Line> lines) {
        lines.stream()
                .flatMap(station -> station.getStations().stream())
                .distinct()
                .collect(Collectors.toList())
                .forEach(this.graph::addVertex);
    }

    private void configEdge(final Section section, final SearchType searchType) {
        final SectionEdge sectionEdge = SectionEdge.of(section);
        this.graph.addEdge(section.getUpStation(), section.getDownStation(), sectionEdge);
        if (searchType == SearchType.DISTANCE) {
            this.graph.setEdgeWeight(sectionEdge, section.getDistance());
        } else {
            this.graph.setEdgeWeight(sectionEdge, section.getDuration());
        }
    }

    private List<Section> findShortestSections(final Station source, final Station target) {
        try {
            final GraphPath<Station, SectionEdge> result = this.dijkstraShortestPath.getPath(source, target);
            validateNotConnected(result);
            return result.getEdgeList()
                    .stream()
                    .map(SectionEdge::getSection)
                    .collect(Collectors.toList());
        } catch (IllegalArgumentException ie) {
            throw new NoRegisterStationException(NO_REGISTER_LINE_STATION);
        }
    }

    private void validateNotConnected(final GraphPath<Station, SectionEdge> graphPath) {
        if (graphPath == null) {
            throw new NoPathConnectedException(NO_PATH_CONNECTED);
        }
    }

    private void validateSameStation(final Station sourceStation, final Station targetStation) {
        if (sourceStation.equals(targetStation)) {
            throw new SameStationException(NO_FIND_SAME_SOURCE_TARGET_STATION);
        }
    }
}
