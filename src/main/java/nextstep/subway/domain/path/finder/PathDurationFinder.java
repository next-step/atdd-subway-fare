package nextstep.subway.domain.path.finder;

import java.util.List;
import nextstep.subway.domain.Line;
import nextstep.subway.domain.Section;
import nextstep.subway.domain.SectionEdge;
import nextstep.subway.domain.Station;
import org.jgrapht.graph.SimpleDirectedWeightedGraph;

public class PathDurationFinder extends PathFinderImpl {

  public PathDurationFinder(List<Line> lines) {
    super(lines);
  }

  @Override
  // 지하철 역의 연결 정보(간선)을 등록
  void addStationEdge(SimpleDirectedWeightedGraph<Station, SectionEdge> graph) {
    lines.stream()
        .flatMap(it -> it.getSections().stream())
        .forEach(it -> {
          SectionEdge sectionEdge = SectionEdge.of(it);
          graph.addEdge(it.getUpStation(), it.getDownStation(), sectionEdge);
          graph.setEdgeWeight(sectionEdge, it.getDuration());
        });
  }

  @Override
  // 지하철 역의 연결 정보(간선)을 등록
  void addStationEdgeOpposite(SimpleDirectedWeightedGraph<Station, SectionEdge> graph) {
    lines.stream()
        .flatMap(it -> it.getSections().stream())
        .map(it -> new Section(it.getLine(), it.getDownStation(), it.getUpStation(), it.getDistance(), it.getDuration()))
        .forEach(it -> {
          SectionEdge sectionEdge = SectionEdge.of(it);
          graph.addEdge(it.getUpStation(), it.getDownStation(), sectionEdge);
          graph.setEdgeWeight(sectionEdge, it.getDuration());
        });
  }
}
