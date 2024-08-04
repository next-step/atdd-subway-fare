package nextstep.subway.path.domain;

import java.util.List;
import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.LineSection;
import nextstep.subway.station.domain.Station;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.WeightedMultigraph;

public class SubwayGraph2 {
  private static final String STATION_NOT_FOUND = "구간의 상/하행역이 존재하지 않습니다.";
  private static final double EPSILON = 10e-7;

  private final WeightedMultigraph<Station, LineSectionEdge2> graph;
  private final PathType type;

  public SubwayGraph2(WeightedMultigraph<Station, LineSectionEdge2> graph, PathType type) {
    this.graph = graph;
    this.type = type;
  }

  public SubwayGraph2(PathType type) {
    this(new WeightedMultigraph<>(LineSectionEdge2.class), type);
  }

  public void addStation(Station station) {
    graph.addVertex(station);
  }

  public void addLineSection(LineSection lineSection) {
    Station upStation = lineSection.getUpStation();
    Station downStation = lineSection.getDownStation();
    validate(upStation, downStation);

    LineSectionEdge2 edge = LineSectionEdge2.of(lineSection);
    graph.addEdge(upStation, downStation, edge);
    graph.setEdgeWeight(edge, type.getEdgeWeight(lineSection));
  }

  private void validate(Station upStation, Station downStation) {
    if (!graph.containsVertex(upStation) || !graph.containsVertex(downStation)) {
      throw new IllegalArgumentException(STATION_NOT_FOUND);
    }
  }

  public void addLine(Line line) {
    line.getStations().forEach(this::addStation);
    line.getLineSections().getSections().forEach(this::addLineSection);
  }

  public Path2 getShortestPath(Station source, Station target) {
    validate(source, target);

    DijkstraShortestPath<Station, LineSectionEdge2> shortestPath =
        new DijkstraShortestPath<>(graph);

    GraphPath<Station, LineSectionEdge2> path = shortestPath.getPath(source, target);
    if (path == null) {
      return Path2.empty();
    }

    List<LineSectionEdge2> edges = path.getEdgeList();
    long totalDistance = edges.stream().mapToLong(e -> e.getLineSection().getDistance()).sum();
    long totalDuration = edges.stream().mapToLong(e -> e.getLineSection().getDuration()).sum();

    return Path2.of(path.getVertexList(), totalDistance, totalDuration);
  }

  public boolean isSame(SubwayGraph2 that) {
    if (!graph.vertexSet().equals(that.graph.vertexSet())) {
      return false;
    }
    if (graph.edgeSet().size() != that.graph.edgeSet().size()) {
      return false;
    }
    for (LineSectionEdge2 edge : graph.edgeSet()) {
      Station source = graph.getEdgeSource(edge);
      Station target = graph.getEdgeTarget(edge);
      LineSectionEdge2 thatEdge = that.graph.getEdge(source, target);
      if (thatEdge == null) {
        return false;
      }
      if (Math.abs(graph.getEdgeWeight(edge) - that.graph.getEdgeWeight(thatEdge)) > EPSILON) {
        return false;
      }
    }
    return true;
  }
}
