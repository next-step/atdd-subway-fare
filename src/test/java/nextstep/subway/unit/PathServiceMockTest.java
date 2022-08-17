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

    신분당선 = new Line("신분당선", "red", 1000);
    이호선 = new Line("2호선", "red", 0);
    삼호선 = new Line("3호선", "red", 500);

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

    PathResponse result = pathService.findPath(교대역_id, 양재역_id, PathType.DISTANCE, 30);

    경로_조회_정보_비교(result, 3, 6, 5, 2_250);
  }

  @Test
  void 두_역_최소_시간_조회() {
    when(stationService.findById(교대역_id)).thenReturn(교대역);
    when(stationService.findById(양재역_id)).thenReturn(양재역);
    when(lineService.findLines()).thenReturn(lines);

    PathResponse result = pathService.findPath(교대역_id, 양재역_id, PathType.DURATION, 30);

    경로_조회_정보_비교(result, 3, 6, 5, 2_250);
  }

  @Test
  void 같은역_조회_에러() {
    when(stationService.findById(교대역_id)).thenReturn(교대역);

    assertThatThrownBy(() -> pathService.findPath(교대역_id, 교대역_id, PathType.DURATION, 30)).isInstanceOf(CustomException.class);
  }

  @Test
  void 두_역_10KM_이내_최단_거리_경로_조회() {
    when(stationService.findById(교대역_id)).thenReturn(교대역);
    when(stationService.findById(양재역_id)).thenReturn(양재역);
    when(lineService.findLines()).thenReturn(lines);

    PathResponse result = pathService.findPath(교대역_id, 양재역_id, PathType.DISTANCE, 30);

    경로_조회_정보_비교(result, 3, 6, 5, 2_250);
  }

  @Test
  void 두_역_10KM_초과_50KM_이하_최단_거리_경로_조회() {
    when(stationService.findById(남부터미널역_id)).thenReturn(남부터미널역);
    when(stationService.findById(강남역_id)).thenReturn(강남역);
    when(lineService.findLines()).thenReturn(lines);

    PathResponse result = pathService.findPath(남부터미널역_id, 강남역_id, PathType.DISTANCE, 30);

    경로_조회_정보_비교(result, 3, 18, 6, 1_950);
  }

  @Test
  void 두_역_경로_50KM_초과_최단_거리_경로_조회() {
    when(stationService.findById(양재역_id)).thenReturn(양재역);
    when(stationService.findById(대치역_id)).thenReturn(대치역);
    when(lineService.findLines()).thenReturn(lines);

    PathResponse result = pathService.findPath(양재역_id, 대치역_id, PathType.DISTANCE, 30);

    경로_조회_정보_비교(result, 3, 56, 8, 2_650);
  }

  @Test
  void 두_역_10KM_이내_최소_시간_경로_조회() {
    when(stationService.findById(교대역_id)).thenReturn(교대역);
    when(stationService.findById(양재역_id)).thenReturn(양재역);
    when(lineService.findLines()).thenReturn(lines);

    PathResponse result = pathService.findPath(교대역_id, 양재역_id, PathType.DURATION, 30);

    경로_조회_정보_비교(result, 3, 6, 5, 2_250);
  }

  @Test
  void 두_역_10KM_초과_50KM_이하_최소_시간_경로_조회() {
    when(stationService.findById(남부터미널역_id)).thenReturn(남부터미널역);
    when(stationService.findById(강남역_id)).thenReturn(강남역);
    when(lineService.findLines()).thenReturn(lines);

    PathResponse result = pathService.findPath(남부터미널역_id, 강남역_id, PathType.DURATION, 30);

    경로_조회_정보_비교(result, 3, 18, 6, 1_950);
  }

  @Test
  void 두_역_경로_50KM_초과_최소_시간_경로_조회() {
    when(stationService.findById(남부터미널역_id)).thenReturn(남부터미널역);
    when(stationService.findById(양재역_id)).thenReturn(양재역);
    when(lineService.findLines()).thenReturn(lines);

    PathResponse result = pathService.findPath(남부터미널역_id, 양재역_id, PathType.DURATION, 30);

    경로_조회_정보_비교(result, 2, 58, 4, 2_650);
  }

  @Test
  void 어린이_두_역_10KM_이내_최단_거리_경로_조회() {
    when(stationService.findById(교대역_id)).thenReturn(교대역);
    when(stationService.findById(양재역_id)).thenReturn(양재역);
    when(lineService.findLines()).thenReturn(lines);

    PathResponse result = pathService.findPath(교대역_id, 양재역_id, PathType.DISTANCE, 12);

    경로_조회_정보_비교(result, 3, 6, 5, 950);
  }

  @Test
  void 어린이_두_역_10KM_초과_50KM_이하_최단_거리_경로_조회() {
    when(stationService.findById(남부터미널역_id)).thenReturn(남부터미널역);
    when(stationService.findById(강남역_id)).thenReturn(강남역);
    when(lineService.findLines()).thenReturn(lines);

    PathResponse result = pathService.findPath(남부터미널역_id, 강남역_id, PathType.DISTANCE, 12);

    경로_조회_정보_비교(result, 3, 18, 6, 800);
  }

  @Test
  void 어린이_두_역_경로_50KM_초과_최단_거리_경로_조회() {
    when(stationService.findById(양재역_id)).thenReturn(양재역);
    when(stationService.findById(대치역_id)).thenReturn(대치역);
    when(lineService.findLines()).thenReturn(lines);

    PathResponse result = pathService.findPath(양재역_id, 대치역_id, PathType.DISTANCE, 12);

    경로_조회_정보_비교(result, 3, 56, 8, 1_150);
  }

  @Test
  void 청소년_두_역_10KM_이내_최단_거리_경로_조회() {
    when(stationService.findById(교대역_id)).thenReturn(교대역);
    when(stationService.findById(양재역_id)).thenReturn(양재역);
    when(lineService.findLines()).thenReturn(lines);

    PathResponse result = pathService.findPath(교대역_id, 양재역_id, PathType.DISTANCE, 18);

    경로_조회_정보_비교(result, 3, 6, 5, 1_520);
  }

  @Test
  void 청소년_두_역_10KM_초과_50KM_이하_최단_거리_경로_조회() {
    when(stationService.findById(남부터미널역_id)).thenReturn(남부터미널역);
    when(stationService.findById(강남역_id)).thenReturn(강남역);
    when(lineService.findLines()).thenReturn(lines);

    PathResponse result = pathService.findPath(남부터미널역_id, 강남역_id, PathType.DISTANCE, 18);

    경로_조회_정보_비교(result, 3, 18, 6, 1_280);
  }

  @Test
  void 청소년_두_역_경로_50KM_초과_최단_거리_경로_조회() {
    when(stationService.findById(양재역_id)).thenReturn(양재역);
    when(stationService.findById(대치역_id)).thenReturn(대치역);
    when(lineService.findLines()).thenReturn(lines);

    PathResponse result = pathService.findPath(양재역_id, 대치역_id, PathType.DISTANCE, 18);

    경로_조회_정보_비교(result, 3, 56, 8, 1_840);
  }

  @Test
  void 나이_음수_에러() {
    assertThatThrownBy(() -> pathService.findPath(교대역_id, 교대역_id, PathType.DURATION, -1)).isInstanceOf(CustomException.class);
  }

  private void 경로_조회_정보_비교(PathResponse result, int 지하쳘역_갯수, int 거리, int 시간, int 요금) {
    assertAll(
        () -> assertThat(result.getStations().size()).isEqualTo(지하쳘역_갯수),
        () -> assertThat(result.getDistance()).isEqualTo(거리),
        () -> assertThat(result.getDuration()).isEqualTo(시간),
        () -> assertThat(result.getFare()).isEqualTo(요금)
    );
  }

  @Test
  void 두_역_10KM_이내_최단_거리_경로_조회() {
    when(stationService.findById(교대역_id)).thenReturn(교대역);
    when(stationService.findById(양재역_id)).thenReturn(양재역);
    when(lineService.findLines()).thenReturn(lines);

    PathResponse result = pathService.findPath(교대역_id, 양재역_id, PathType.DISTANCE);

    경로_조회_정보_비교(result, 3, 6, 5, 1_250);
  }

  @Test
  void 두_역_10KM_초과_50KM_이하_최단_거리_경로_조회() {
    when(stationService.findById(남부터미널역_id)).thenReturn(남부터미널역);
    when(stationService.findById(강남역_id)).thenReturn(강남역);
    when(lineService.findLines()).thenReturn(lines);

    PathResponse result = pathService.findPath(남부터미널역_id, 강남역_id, PathType.DISTANCE);

    경로_조회_정보_비교(result, 3, 18, 6, 1_450);
  }

  @Test
  void 두_역_경로_50KM_초과_최단_거리_경로_조회() {
    when(stationService.findById(양재역_id)).thenReturn(양재역);
    when(stationService.findById(대치역_id)).thenReturn(대치역);
    when(lineService.findLines()).thenReturn(lines);

    PathResponse result = pathService.findPath(양재역_id, 대치역_id, PathType.DISTANCE);

    경로_조회_정보_비교(result, 3, 56, 8, 2_150);
  }

  @Test
  void 두_역_10KM_이내_최소_시간_경로_조회() {
    when(stationService.findById(교대역_id)).thenReturn(교대역);
    when(stationService.findById(양재역_id)).thenReturn(양재역);
    when(lineService.findLines()).thenReturn(lines);

    PathResponse result = pathService.findPath(교대역_id, 양재역_id, PathType.DURATION);

    경로_조회_정보_비교(result, 3, 6, 5, 1_250);
  }

  @Test
  void 두_역_10KM_초과_50KM_이하_최소_시간_경로_조회() {
    when(stationService.findById(남부터미널역_id)).thenReturn(남부터미널역);
    when(stationService.findById(강남역_id)).thenReturn(강남역);
    when(lineService.findLines()).thenReturn(lines);

    PathResponse result = pathService.findPath(남부터미널역_id, 강남역_id, PathType.DURATION);

    경로_조회_정보_비교(result, 3, 18, 6, 1_450);
  }

  @Test
  void 두_역_경로_50KM_초과_최소_시간_경로_조회() {
    when(stationService.findById(남부터미널역_id)).thenReturn(남부터미널역);
    when(stationService.findById(양재역_id)).thenReturn(양재역);
    when(lineService.findLines()).thenReturn(lines);

    PathResponse result = pathService.findPath(남부터미널역_id, 양재역_id, PathType.DURATION);

    경로_조회_정보_비교(result, 2, 58, 4, 2_150);
  }

  private void 경로_조회_정보_비교(PathResponse result, int 지하쳘역_갯수, int 거리, int 시간, int 요금) {
    assertAll(
        () -> assertThat(result.getStations().size()).isEqualTo(지하쳘역_갯수),
        () -> assertThat(result.getDistance()).isEqualTo(거리),
        () -> assertThat(result.getDuration()).isEqualTo(시간),
        () -> assertThat(result.getFare()).isEqualTo(요금)
    );
  }
}
