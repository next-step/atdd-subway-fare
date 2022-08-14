package nextstep.subway.unit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import nextstep.common.exception.CustomException;
import nextstep.subway.applicaion.LineService;
import nextstep.subway.applicaion.PathService;
import nextstep.subway.applicaion.StationService;
import nextstep.subway.applicaion.dto.PathResponse;
import nextstep.subway.domain.Line;
import nextstep.subway.domain.Station;
import nextstep.subway.domain.path.PathType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class PathServiceMockTest {

  @Mock
  private LineService lineService;

  @Mock
  private StationService stationService;

  @InjectMocks
  private PathService pathService;

  private static Long 교대역_id = 10L;
  private static Long 강남역_id = 20L;
  private static Long 양재역_id = 30L;
  private static Long 남부터미널역_id = 40L;
  private static Long 도곡역_id = 50L;
  private static Long 대치역_id = 60L;

  private Station 교대역;
  private Station 강남역;
  private Station 양재역;
  private Station 남부터미널역;
  private Station 도곡역;
  private Station 대치역;
  private Line 신분당선;
  private Line 이호선;
  private Line 삼호선;

  private List<Line> lines = new ArrayList<>();

  @BeforeEach
  void setUp() {
    교대역 = new Station("교대역");
    강남역 = new Station("강남역");
    양재역 = new Station("양재역");
    남부터미널역 = new Station("남부터미널역");
    도곡역 = new Station("도곡역");
    대치역 = new Station("대치역");

    신분당선 = new Line("신분당선", "red");
    이호선 = new Line("2호선", "red");
    삼호선 = new Line("3호선", "red");

    신분당선.addSection(강남역, 양재역, 3, 3);
    이호선.addSection(교대역, 강남역, 3, 2);
    삼호선.addSection(교대역, 남부터미널역, 15, 4);
    삼호선.addSection(남부터미널역, 양재역, 58, 4);
    삼호선.addSection(양재역, 도곡역, 10, 4);
    삼호선.addSection(도곡역, 대치역, 46, 4);

    lines.add(신분당선);
    lines.add(이호선);
    lines.add(삼호선);
  }

  @Test
  void 두_역_최단_거리_조회() {
    when(stationService.findById(교대역_id)).thenReturn(교대역);
    when(stationService.findById(양재역_id)).thenReturn(양재역);
    when(lineService.findLines()).thenReturn(lines);

    PathResponse result = pathService.findPath(교대역_id, 양재역_id, PathType.DISTANCE);

    assertAll(
        () -> assertThat(result.getStations().size()).isEqualTo(3),
        () -> assertThat(result.getDistance()).isEqualTo(6),
        () -> assertThat(result.getDuration()).isEqualTo(5)
    );
  }

  @Test
  void 두_역_최소_시간_조회() {
    when(stationService.findById(교대역_id)).thenReturn(교대역);
    when(stationService.findById(양재역_id)).thenReturn(양재역);
    when(lineService.findLines()).thenReturn(lines);

    PathResponse result = pathService.findPath(교대역_id, 양재역_id, PathType.DURATION);

    assertAll(
        () -> assertThat(result.getStations().size()).isEqualTo(3),
        () -> assertThat(result.getDistance()).isEqualTo(6),
        () -> assertThat(result.getDuration()).isEqualTo(5)
    );
  }

  @Test
  void 같은역_조회_에러() {
    when(stationService.findById(교대역_id)).thenReturn(교대역);

    assertThatThrownBy(() -> pathService.findPath(교대역_id, 교대역_id, PathType.DURATION)).isInstanceOf(CustomException.class);
  }

  @Test
  void 두_역_10KM_이내_최단_거리_경로_조회() {
    when(stationService.findById(교대역_id)).thenReturn(교대역);
    when(stationService.findById(양재역_id)).thenReturn(양재역);
    when(lineService.findLines()).thenReturn(lines);

    PathResponse result = pathService.findPath(교대역_id, 양재역_id, PathType.DISTANCE);

    assertAll(
        () -> assertThat(result.getStations().size()).isEqualTo(3),
        () -> assertThat(result.getDistance()).isEqualTo(6),
        () -> assertThat(result.getDuration()).isEqualTo(5),
        () -> assertThat(result.getFare()).isEqualTo(1250)
    );
  }

  @Test
  void 두_역_10KM_초과_50KM_이하_최단_거리_경로_조회() {
    when(stationService.findById(남부터미널역_id)).thenReturn(남부터미널역);
    when(stationService.findById(강남역_id)).thenReturn(강남역);
    when(lineService.findLines()).thenReturn(lines);

    PathResponse result = pathService.findPath(남부터미널역_id, 강남역_id, PathType.DISTANCE);

    assertAll(
        () -> assertThat(result.getStations().size()).isEqualTo(3),
        () -> assertThat(result.getDistance()).isEqualTo(18),
        () -> assertThat(result.getDuration()).isEqualTo(6),
        () -> assertThat(result.getFare()).isEqualTo(1450)
    );
  }

  @Test
  void 두_역_경로_50KM_초과_최단_거리_경로_조회() {
    when(stationService.findById(양재역_id)).thenReturn(양재역);
    when(stationService.findById(대치역_id)).thenReturn(대치역);
    when(lineService.findLines()).thenReturn(lines);

    PathResponse result = pathService.findPath(양재역_id, 대치역_id, PathType.DISTANCE);

    assertAll(
        () -> assertThat(result.getStations().size()).isEqualTo(3),
        () -> assertThat(result.getDistance()).isEqualTo(56),
        () -> assertThat(result.getDuration()).isEqualTo(8),
        () -> assertThat(result.getFare()).isEqualTo(2150)
    );
  }

  @Test
  void 두_역_10KM_이내_최소_시간_경로_조회() {
    when(stationService.findById(교대역_id)).thenReturn(교대역);
    when(stationService.findById(양재역_id)).thenReturn(양재역);
    when(lineService.findLines()).thenReturn(lines);

    PathResponse result = pathService.findPath(교대역_id, 양재역_id, PathType.DURATION);

    assertAll(
        () -> assertThat(result.getStations().size()).isEqualTo(3),
        () -> assertThat(result.getDistance()).isEqualTo(6),
        () -> assertThat(result.getDuration()).isEqualTo(5),
        () -> assertThat(result.getFare()).isEqualTo(1250)
    );
  }

  @Test
  void 두_역_10KM_초과_50KM_이하_최소_시간_경로_조회() {
    when(stationService.findById(남부터미널역_id)).thenReturn(남부터미널역);
    when(stationService.findById(강남역_id)).thenReturn(강남역);
    when(lineService.findLines()).thenReturn(lines);

    PathResponse result = pathService.findPath(남부터미널역_id, 강남역_id, PathType.DURATION);

    assertAll(
        () -> assertThat(result.getStations().size()).isEqualTo(3),
        () -> assertThat(result.getDistance()).isEqualTo(18),
        () -> assertThat(result.getDuration()).isEqualTo(6),
        () -> assertThat(result.getFare()).isEqualTo(1450)
    );
  }

  @Test
  void 두_역_경로_50KM_초과_최소_시간_경로_조회() {
    when(stationService.findById(남부터미널역_id)).thenReturn(남부터미널역);
    when(stationService.findById(양재역_id)).thenReturn(양재역);
    when(lineService.findLines()).thenReturn(lines);

    PathResponse result = pathService.findPath(남부터미널역_id, 양재역_id, PathType.DURATION);

    assertAll(
        () -> assertThat(result.getStations().size()).isEqualTo(2),
        () -> assertThat(result.getDistance()).isEqualTo(58),
        () -> assertThat(result.getDuration()).isEqualTo(4),
        () -> assertThat(result.getFare()).isEqualTo(2150)
    );
  }
}
