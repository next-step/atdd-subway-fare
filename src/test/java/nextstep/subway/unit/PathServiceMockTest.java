package nextstep.subway.unit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import nextstep.subway.applicaion.LineService;
import nextstep.subway.applicaion.PathService;
import nextstep.subway.applicaion.StationService;
import nextstep.subway.applicaion.dto.PathResponse;
import nextstep.subway.domain.Line;
import nextstep.subway.domain.PathType;
import nextstep.subway.domain.Station;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

@ExtendWith(MockitoExtension.class)
public class PathServiceMockTest {

  @Mock
  private LineService lineService;

  @Mock
  private StationService stationService;

  @InjectMocks
  private PathService pathService;

  private Station 교대역;
  private Station 강남역;
  private Station 양재역;
  private Station 남부터미널역;
  private Line 신분당선;
  private Line 이호선;
  private Line 삼호선;

  private List<Line> lines = new ArrayList<>();

  @BeforeEach
  void setUp() {
    교대역 = createStation(10L, "교대역");
    강남역 = createStation(20L, "강남역");
    양재역 = createStation(30L, "양재역");
    남부터미널역 = createStation(40L, "남부터미널역");

    신분당선 = new Line("신분당선", "red");
    이호선 = new Line("2호선", "red");
    삼호선 = new Line("3호선", "red");

    신분당선.addSection(강남역, 양재역, 3, 2);
    이호선.addSection(교대역, 강남역, 3, 2);
    삼호선.addSection(교대역, 남부터미널역, 5, 4);
    삼호선.addSection(남부터미널역, 양재역, 5, 4);

    lines.add(신분당선);
    lines.add(이호선);
    lines.add(삼호선);
  }

  @Test
  void 두_역_최단_거리_조회() {
    when(stationService.findById(10L)).thenReturn(교대역);
    when(stationService.findById(30L)).thenReturn(양재역);
    when(lineService.findLines()).thenReturn(lines);

    PathResponse result = pathService.findPath(교대역.getId(), 양재역.getId(), PathType.DISTANCE);

    assertThat(result.getStations().size()).isEqualTo(3);
    assertThat(result.getDistance()).isEqualTo(6);
    assertThat(result.getDuration()).isEqualTo(4);
  }

  @Test
  void 두_역_최소_시간_조회() {
    when(stationService.findById(10L)).thenReturn(교대역);
    when(stationService.findById(30L)).thenReturn(양재역);
    when(lineService.findLines()).thenReturn(lines);

    PathResponse result = pathService.findPath(교대역.getId(), 양재역.getId(), PathType.DURATION);

    assertThat(result.getStations().size()).isEqualTo(3);
    assertThat(result.getDistance()).isEqualTo(6);
    assertThat(result.getDuration()).isEqualTo(4);
  }

  private Station createStation(long id, String name) {
    Station station = new Station(name);
    ReflectionTestUtils.setField(station, "id", id);

    return station;
  }
}
