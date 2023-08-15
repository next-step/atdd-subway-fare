package nextstep.api.subway.support;

import java.util.List;
import java.util.stream.Collectors;

import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.WeightedMultigraph;

import nextstep.api.SubwayException;
import nextstep.api.subway.domain.line.Line;
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

    public static Builder builder(final List<Station> stations, final List<Line> lines) {
        return new Builder(stations, lines);
    }

    public static class Builder {
        private final WeightedMultigraph<Station, SectionEdge> graph;
        private final List<Station> stations;
        private final List<Line> lines;
        private Station source;
        private Station target;

        public Builder(final List<Station> stations, final List<Line> lines) {
            this.graph = new WeightedMultigraph<>(SectionEdge.class);
            this.stations = stations;
            this.lines = lines;
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
            return new SubwayShortestPath(makeGraph(type), source, target);
        }

        private DijkstraShortestPath<Station, SectionEdge> makeGraph(final PathSelection type) {
            registerVertexes();
            registerEdges(type);

            return new DijkstraShortestPath<>(graph);
        }

        private void registerVertexes() {
            for (Station station : stations) {
                graph.addVertex(station);
            }
        }

        private void registerEdges(final PathSelection type) {
            for (final var line : lines) {
                registerEdgePerLine(line, type);
            }
        }

        private void registerEdgePerLine(final Line line, final PathSelection type) {
            for (final var section : line.getSections()) {
                final var upStation = section.getUpStation();
                final var downStation = section.getDownStation();
                final var sectionEdge = SectionEdge.from(line, section);

                graph.addEdge(upStation, downStation, sectionEdge);
                graph.setEdgeWeight(sectionEdge, type.getValueOf(section));
            }
        }
    }
}
