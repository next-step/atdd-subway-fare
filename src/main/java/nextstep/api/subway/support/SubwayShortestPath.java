package nextstep.api.subway.support;

import java.util.List;
import java.util.stream.Collectors;

import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.WeightedMultigraph;

import nextstep.api.SubwayException;
import nextstep.api.subway.domain.line.Section;
import nextstep.api.subway.domain.path.PathSelection;
import nextstep.api.subway.domain.station.Station;

public class SubwayShortestPath {
    private final GraphPath<Station, SectionEdge> path;

    private SubwayShortestPath(final DijkstraShortestPath<Station, SectionEdge> dijkstraShortestPath,
                               final Station source, final Station target) {
        this.path = dijkstraShortestPath.getPath(source, target);

        if (path == null) {
            throw new SubwayException(String.format(
                    "출발역부터 도착역까지의 경로를 조회할 수 없습니다: 출발역id=%d, 도착역id=%d", source.getId(), target.getId()));
        }
    }

    public List<Station> getStation() {
        return path.getVertexList();
    }

    public List<Section> getSections() {
        return path.getEdgeList().stream()
                .map(SectionEdge::getSection)
                .collect(Collectors.toUnmodifiableList());
    }

    public long getTotalDistance() {
        return getSections().stream()
                .mapToLong(Section::getDistance)
                .sum();
    }

    public long getTotalDuration() {
        return getSections().stream().mapToLong(Section::getDistance).sum();
    }

    public static Builder builder(final List<Station> stations, final List<Section> sections) {
        return new Builder(stations, sections);
    }

    public static class Builder {
        private final List<Station> stations;
        private final List<Section> sections;
        private Station source;
        private Station target;

        public Builder(final List<Station> stations, final List<Section> sections) {
            this.stations = stations;
            this.sections = sections;
        }

        public Builder source(final Station source) {
            this.source = source;
            return this;
        }

        public Builder target(final Station target) {
            this.target = target;
            return this;
        }

        public SubwayShortestPath buildOf(final PathSelection type) {
            return new SubwayShortestPath(makeGraph(stations, sections, type), source, target);
        }

        private DijkstraShortestPath<Station, SectionEdge> makeGraph(final List<Station> stations,
                                                                     final List<Section> sections,
                                                                     final PathSelection type) {
            final var graph = new WeightedMultigraph<Station, SectionEdge>(SectionEdge.class);

            registerVertexes(graph, stations);
            registerEdges(graph, sections, type);

            return new DijkstraShortestPath<>(graph);
        }

        private void registerVertexes(final WeightedMultigraph<Station, SectionEdge> graph,
                                      final List<Station> stations) {
            for (Station station : stations) {
                graph.addVertex(station);
            }
        }

        private void registerEdges(final WeightedMultigraph<Station, SectionEdge> graph,
                                   final List<Section> sections, final PathSelection type) {
            for (final var section : sections) {
                final var upStation = section.getUpStation();
                final var downStation = section.getDownStation();
                final var sectionEdge = SectionEdge.of(section);

                graph.addEdge(upStation, downStation, sectionEdge);
                graph.setEdgeWeight(sectionEdge, type.getValueOf(section));
            }
        }
    }
}
