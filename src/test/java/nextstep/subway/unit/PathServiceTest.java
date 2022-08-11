package nextstep.subway.unit;

import static org.assertj.core.api.Assertions.assertThat;

import nextstep.subway.applicaion.PathService;
import nextstep.subway.applicaion.dto.PathResponse;
import nextstep.subway.domain.Line;
import nextstep.subway.domain.LineRepository;
import nextstep.subway.domain.path.PathType;
import nextstep.subway.domain.Station;
import nextstep.subway.domain.StationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
public class PathServiceTest {

  @Autowired
  private LineRepository lineRepository;

  @Autowired
  private StationRepository stationRepository;

  @Autowired
  private PathService pathService;

  private Station 교대역;
  private Station 강남역;
  private Station 양재역;
  private Station 남부터미널역;
  private Line 신분당선;
  private Line 이호선;
  private Line 삼호선;

  @BeforeEach
  void setUp() {
    교대역 = stationRepository.save(createStation(10L, "교대역"));
    강남역 = stationRepository.save(createStation(20L, "강남역"));
    양재역 = stationRepository.save(createStation(30L, "양재역"));
    남부터미널역 = stationRepository.save(createStation(40L, "남부터미널역"));

    신분당선 = lineRepository.save(new Line("신분당선", "red"));
    이호선 = lineRepository.save(new Line("2호선", "red"));
    삼호선 = lineRepository.save(new Line("3호선", "red"));

    신분당선.addSection(강남역, 양재역, 3, 2);
    이호선.addSection(교대역, 강남역, 3, 2);
    삼호선.addSection(교대역, 남부터미널역, 5, 4);
    삼호선.addSection(남부터미널역, 양재역, 5, 4);
  }

  @Test
  void 두_역_최단_거리_조회() {
    PathResponse result = pathService.findPath(교대역.getId(), 양재역.getId(), PathType.DISTANCE);

    assertThat(result.getStations().size()).isEqualTo(3);
    assertThat(result.getDistance()).isEqualTo(6);
    assertThat(result.getDuration()).isEqualTo(4);
  }

  @Test
  void 두_역_최소_시간_조회() {
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
