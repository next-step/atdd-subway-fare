package nextstep.subway.unit.path.application;

import static nextstep.Fixtures.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

import nextstep.subway.line.domain.LineSection;
import nextstep.subway.path.application.GraphService2;
import nextstep.subway.path.application.PathService2;
import nextstep.subway.path.application.dto.PathRequest2;
import nextstep.subway.path.domain.*;
import nextstep.subway.station.application.StationReader;
import nextstep.subway.station.domain.Station;
import org.jgrapht.graph.WeightedMultigraph;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
@SuppressWarnings("NonAsciiCharacters")
@DisplayName("경로 조회 서비스 단위 테스트")
class PathServiceTest2 {
  @Mock GraphService2 graphService;
  @Mock StationReader stationReader;
  @InjectMocks PathService2 pathService;

  private Station 교대역 = 교대역();
  private Station 강남역 = 강남역();
  private Station 남부터미널역 = 남부터미널역();
  private Station 양재역 = 양재역();
  private LineSection 교대_강남_구간 = 교대_강남_구간();
  private LineSection 강남_양재_구간 = 강남_양재_구간();
  private LineSection 교대_남부터미널_구간 = 교대_남부터미널_구간();
  private LineSection 남부터미널_양재_구간 = 남부터미널_양재_구간();

  @DisplayName("최단 거리 경로를 조회한다.")
  @Test
  void findPathDistance() {
    PathType type = PathType.DISTANCE;
    given(graphService.loadGraph(type))
        .willReturn(
            new SubwayGraph2(
                WeightedMultigraph.<Station, LineSectionEdge2>builder(LineSectionEdge2.class)
                    .addVertices(교대역, 강남역, 남부터미널역, 양재역)
                    .addEdge(교대역, 강남역, LineSectionEdge2.of(교대_강남_구간), 교대_강남_구간.getDistance())
                    .addEdge(강남역, 양재역, LineSectionEdge2.of(강남_양재_구간), 강남_양재_구간.getDistance())
                    .addEdge(
                        교대역, 남부터미널역, LineSectionEdge2.of(교대_남부터미널_구간), 교대_남부터미널_구간.getDistance())
                    .addEdge(
                        남부터미널역, 양재역, LineSectionEdge2.of(남부터미널_양재_구간), 남부터미널_양재_구간.getDistance())
                    .build(),
                type));
    given(stationReader.readById(교대역.getId())).willReturn(교대역);
    given(stationReader.readById(양재역.getId())).willReturn(양재역);

    PathRequest2 request = PathRequest2.of(교대역.getId(), 양재역.getId(), type);
    Path2 path = pathService.findPath(request);

    assertThat(path.getStations()).containsExactly(교대역, 남부터미널역, 양재역);
    assertThat(path.getTotalDistance())
        .isEqualTo(교대_남부터미널_구간.getDistance() + 남부터미널_양재_구간.getDistance());
    assertThat(path.getTotalDuration())
        .isEqualTo(교대_남부터미널_구간.getDuration() + 남부터미널_양재_구간.getDuration());
  }

  @DisplayName("최단 소요시간 경로를 조회한다.")
  @Test
  void findPathDuration() {
    PathType type = PathType.DURATION;
    given(graphService.loadGraph(type))
        .willReturn(
            new SubwayGraph2(
                WeightedMultigraph.<Station, LineSectionEdge2>builder(LineSectionEdge2.class)
                    .addVertices(교대역, 강남역, 남부터미널역, 양재역)
                    .addEdge(교대역, 강남역, LineSectionEdge2.of(교대_강남_구간), 교대_강남_구간.getDuration())
                    .addEdge(강남역, 양재역, LineSectionEdge2.of(강남_양재_구간), 강남_양재_구간.getDuration())
                    .addEdge(
                        교대역, 남부터미널역, LineSectionEdge2.of(교대_남부터미널_구간), 교대_남부터미널_구간.getDuration())
                    .addEdge(
                        남부터미널역, 양재역, LineSectionEdge2.of(남부터미널_양재_구간), 남부터미널_양재_구간.getDuration())
                    .build(),
                type));
    given(stationReader.readById(교대역.getId())).willReturn(교대역);
    given(stationReader.readById(양재역.getId())).willReturn(양재역);

    PathRequest2 request = PathRequest2.of(교대역.getId(), 양재역.getId(), type);
    Path2 path = pathService.findPath(request);

    assertThat(path.getStations()).containsExactly(교대역, 강남역, 양재역);
    assertThat(path.getTotalDistance()).isEqualTo(교대_강남_구간.getDistance() + 강남_양재_구간.getDistance());
    assertThat(path.getTotalDuration()).isEqualTo(교대_강남_구간.getDuration() + 강남_양재_구간.getDuration());
  }
}
