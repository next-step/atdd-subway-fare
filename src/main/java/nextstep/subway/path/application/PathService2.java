package nextstep.subway.path.application;

import lombok.RequiredArgsConstructor;
import nextstep.subway.path.application.dto.PathRequest2;
import nextstep.subway.path.domain.Path2;
import nextstep.subway.path.domain.SubwayGraph2;
import nextstep.subway.station.application.StationReader;
import nextstep.subway.station.domain.Station;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PathService2 {
  private final GraphService2 graphService;
  private final StationReader stationReader;

  public Path2 findPath(PathRequest2 request) {
    Station source = stationReader.readById(request.getSource());
    Station target = stationReader.readById(request.getTarget());
    SubwayGraph2 graph = graphService.loadGraph(request.getType());
    return graph.getShortestPath(source, target);
  }
}
