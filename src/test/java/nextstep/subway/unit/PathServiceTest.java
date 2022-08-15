package nextstep.subway.unit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import nextstep.common.exception.CustomException;
import nextstep.subway.applicaion.LineService;
import nextstep.subway.applicaion.PathService;
import nextstep.subway.applicaion.dto.PathResponse;
import nextstep.subway.applicaion.dto.SectionRequest;
import nextstep.subway.domain.Line;
import nextstep.subway.domain.LineRepository;
import nextstep.subway.domain.Station;
import nextstep.subway.domain.StationRepository;
import nextstep.subway.domain.path.PathType;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class PathServiceTest {

  @Autowired
  private LineRepository lineRepository;

  @Autowired
  private LineService lineService;

  @Autowired
  private StationRepository stationRepository;

  @Autowired
  private PathService pathService;

  private Long 교대역;
  private Long 강남역;
  private Long 양재역;
  private Long 남부터미널역;
  private Long 도곡역;
  private Long 대치역;
  private Long 신분당선;
  private Long 이호선;
  private Long 삼호선;

  @BeforeEach
  void setUp() {
    교대역 = stationRepository.save(new Station("교대역")).getId();
    강남역 = stationRepository.save(new Station("강남역")).getId();
    양재역 = stationRepository.save(new Station("양재역")).getId();
    남부터미널역 = stationRepository.save(new Station("남부터미널역")).getId();
    도곡역 = stationRepository.save(new Station("도곡역")).getId();
    대치역 = stationRepository.save(new Station("대치역")).getId();

    신분당선 = lineRepository.save(new Line("신분당선", "red")).getId();
    이호선 = lineRepository.save(new Line("2호선", "red")).getId();
    삼호선 = lineRepository.save(new Line("3호선", "red")).getId();

    lineService.addSection(신분당선, new SectionRequest(강남역, 양재역, 3, 3));
    lineService.addSection(이호선, new SectionRequest(교대역, 강남역, 3, 2));
    lineService.addSection(삼호선, new SectionRequest(교대역, 남부터미널역, 15, 4));
    lineService.addSection(삼호선, new SectionRequest(남부터미널역, 양재역, 58, 4));
    lineService.addSection(삼호선, new SectionRequest(양재역, 도곡역, 10, 4));
    lineService.addSection(삼호선, new SectionRequest(도곡역, 대치역, 46, 4));
  }

  @Test
  void 두_역_최단_거리_조회() {
    PathResponse result = pathService.findPath(교대역, 양재역, PathType.DISTANCE);

    경로_조회_정보_비교(result, 3, 6, 5, 1_250);
  }

  @Test
  void 두_역_최소_시간_조회() {
    PathResponse result = pathService.findPath(교대역, 양재역, PathType.DURATION);

    경로_조회_정보_비교(result, 3, 6, 5, 1_250);
  }

  @Test
  void 같은역_조회_에러() {
    assertThatThrownBy(() -> pathService.findPath(교대역, 교대역, PathType.DURATION)).isInstanceOf(CustomException.class);
  }

  @Test
  void 두_역_10KM_이내_최단_거리_경로_조회() {
    PathResponse result = pathService.findPath(교대역, 양재역, PathType.DISTANCE);

    경로_조회_정보_비교(result, 3, 6, 5, 1_250);
  }

  @Test
  void 두_역_10KM_초과_50KM_이하_최단_거리_경로_조회() {
    PathResponse result = pathService.findPath(남부터미널역, 강남역, PathType.DISTANCE);

    경로_조회_정보_비교(result, 3, 18, 6, 1_450);
  }

  @Test
  void 두_역_경로_50KM_초과_최단_거리_경로_조회() {
    PathResponse result = pathService.findPath(양재역, 대치역, PathType.DISTANCE);

    경로_조회_정보_비교(result, 3, 56, 8, 2_150);
  }

  @Test
  void 두_역_10KM_이내_최소_시간_경로_조회() {
    PathResponse result = pathService.findPath(교대역, 양재역, PathType.DURATION);

    경로_조회_정보_비교(result, 3, 6, 5, 1_250);
  }

  @Test
  void 두_역_10KM_초과_50KM_이하_최소_시간_경로_조회() {
    PathResponse result = pathService.findPath(남부터미널역, 강남역, PathType.DURATION);

    경로_조회_정보_비교(result, 3, 18, 6, 1_450);
  }

  @Test
  void 두_역_경로_50KM_초과_최소_시간_경로_조회() {
    PathResponse result = pathService.findPath(남부터미널역, 양재역, PathType.DURATION);

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

  @AfterEach
  void after() {
    lineRepository.deleteById(신분당선);
    lineRepository.deleteById(이호선);
    lineRepository.deleteById(삼호선);

    stationRepository.deleteById(교대역);
    stationRepository.deleteById(강남역);
    stationRepository.deleteById(양재역);
    stationRepository.deleteById(남부터미널역);
  }
}
