package nextstep.subway.unit;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import nextstep.subway.domain.Line;
import nextstep.subway.domain.Path;
import nextstep.subway.domain.PathDurationFinder;
import nextstep.subway.domain.PathFinder;
import nextstep.subway.domain.Station;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

public class PathDurationFinderTest {

  private Station 교대역;
  private Station 강남역;
  private Station 양재역;
  private Station 남부터미널역;
  private Line 신분당선;
  private Line 이호선;
  private Line 삼호선;

  @BeforeEach
  void setUp() {
    교대역 = createStation(1L, "교대역");
    강남역 = createStation(2L, "강남역");
    양재역 = createStation(3L, "양재역");
    남부터미널역 = createStation(4L, "남부터미널역");

    신분당선 = new Line("신분당선", "red");
    이호선 = new Line("2호선", "red");
    삼호선 = new Line("3호선", "red");

    신분당선.addSection(강남역, 양재역, 3, 3);
    이호선.addSection(교대역, 강남역, 3, 3);
    삼호선.addSection(교대역, 남부터미널역, 5, 5);
    삼호선.addSection(남부터미널역, 양재역, 5, 5);
  }

  @Test
  void findPath() {
    // given
    List<Line> lines = Lists.newArrayList(신분당선, 이호선, 삼호선);
    PathFinder pathFinder = new PathDurationFinder(lines);

    // when
    Path path = pathFinder.findPath(교대역, 양재역);

    // then
    assertThat(path.getStations()).containsExactlyElementsOf(Lists.newArrayList(교대역, 강남역, 양재역));
    assertThat(path.extractDistance()).isEqualTo(6);
    assertThat(path.extractDuration()).isEqualTo(6);
  }

  @Test
  void findPathOppositely() {
    // given
    List<Line> lines = Lists.newArrayList(신분당선, 이호선, 삼호선);
    PathFinder pathFinder = new PathDurationFinder(lines);

    // when
    Path path = pathFinder.findPath(양재역, 교대역);

    // then
    assertThat(path.getStations()).containsExactlyElementsOf(Lists.newArrayList(양재역, 강남역, 교대역));
    assertThat(path.extractDistance()).isEqualTo(6);
    assertThat(path.extractDuration()).isEqualTo(6);
  }

  private Station createStation(long id, String name) {
    Station station = new Station(name);
    ReflectionTestUtils.setField(station, "id", id);

    return station;
  }
}
