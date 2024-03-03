package nextstep.subway.service;


import nextstep.subway.dto.path.PathEdge;
import nextstep.subway.dto.path.PathResponse;
import nextstep.subway.entity.Section;
import nextstep.subway.entity.Sections;
import nextstep.subway.entity.Station;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.WeightedMultigraph;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

public abstract class PathFinder {
    protected final WeightedMultigraph<Station, PathEdge> graph = new WeightedMultigraph<>(PathEdge.class);

    protected PathFinder(List<Sections> sectionsList) {
        setGraph(sectionsList);
    }

    private void setGraph(List<Sections> sectionsList) {
        for (Sections sections : sectionsList) {
            setGraphBySection(sections);
        }
    }

    private void setGraphBySection(Sections sections) {
        for (Section section : sections.getSections()) {
            addVertex(section);
            setEdgeWeight(section);
        }
    }

    private void addVertex(Section section) {
        graph.addVertex(section.getDownStation());
        graph.addVertex(section.getUpStation());
    }

    protected abstract void setEdgeWeight(Section section);

    public PathResponse getShortestPath(Station sourceStation, Station targetStation) {
        verifySameStation(sourceStation, targetStation);

        GraphPath<Station, PathEdge> path = getPath(sourceStation, targetStation);

        List<Station> shortestPath = path.getVertexList();
        Integer distance = getDistance(path);
        Integer duration = getDuration(path);

        return new PathResponse(shortestPath, distance, duration);
    }

    private void verifySameStation(Station sourceStation, Station targetStation) {
        if(Objects.equals(sourceStation, targetStation)) {
            throw new IllegalArgumentException("출발역과 도착역은 동일할 수 없다.");
        }
    }

    private GraphPath<Station, PathEdge> getPath(Station sourceStation, Station targetStation) {
        DijkstraShortestPath<Station, PathEdge> dijkstraShortestPath = new DijkstraShortestPath<>(graph);

        try {
            return Optional.ofNullable(dijkstraShortestPath.getPath(sourceStation, targetStation)).stream()
                .findAny()
                .orElseThrow();
        } catch (Exception e) {
            throw new IllegalArgumentException("출발역과 도착역이 연결되어 있어야 한다.");
        }
    }

    private Integer getDistance(GraphPath<Station, PathEdge> path) {
        return path.getEdgeList().stream()
            .mapToInt(PathEdge::getDistance)
            .sum();
    }

    private Integer getDuration(GraphPath<Station, PathEdge> path) {
        return path.getEdgeList().stream()
            .mapToInt(PathEdge::getDuration)
            .sum();
    }

    public boolean isConnectedStation(Station sourceStation, Station targetStation) {
        verifySameStation(sourceStation, targetStation);

        try {
            return Optional.ofNullable(DijkstraShortestPath.findPathBetween(graph, sourceStation, targetStation))
                .isPresent();
        } catch (Exception e) {
            return false;
        }
    }
}
