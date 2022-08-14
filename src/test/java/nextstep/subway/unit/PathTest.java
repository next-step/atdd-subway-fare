package nextstep.subway.unit;

import static org.assertj.core.api.Assertions.assertThat;

import nextstep.subway.domain.Line;
import nextstep.subway.domain.path.Path;
import nextstep.subway.domain.Sections;
import nextstep.subway.domain.Station;
import org.junit.jupiter.api.Test;

public class PathTest {

  @Test
  void 구간_조회_총_시간() {
    Station 강남역 = new Station("강남역");
    Station 역삼역 = new Station("역삼역");
    Station 삼성역 = new Station("삼성역");
    Line line = new Line("2호선", "green");

    line.addSection(강남역, 역삼역, 10, 10);
    line.addSection(역삼역, 삼성역, 5, 5);

    Path path = new Path(new Sections(line.getSections()), 15);

    assertThat(path.extractDuration()).isEqualTo(15);
  }

  @Test
  void 구간_길이_10KM_이내_요금_조회() {
    Station 강남역 = new Station("강남역");
    Station 역삼역 = new Station("역삼역");
    Station 삼성역 = new Station("삼성역");
    Line line = new Line("2호선", "green");

    line.addSection(강남역, 역삼역, 4, 10);
    line.addSection(역삼역, 삼성역, 5, 5);

    Path path = new Path(new Sections(line.getSections()), 9);

    assertThat(path.extractFare()).isEqualTo(1250);
  }

  @Test
  void 구간_길이_10KM_초과_50KM_이하_요금_조회() {
    Station 강남역 = new Station("강남역");
    Station 역삼역 = new Station("역삼역");
    Station 삼성역 = new Station("삼성역");
    Line line = new Line("2호선", "green");

    line.addSection(강남역, 역삼역, 10, 10);
    line.addSection(역삼역, 삼성역, 15, 5);

    Path path = new Path(new Sections(line.getSections()), 25);

    assertThat(path.extractFare()).isEqualTo(1550);
  }

  @Test
  void 구간_길이_50KM_초과_요금_조회() {
    Station 강남역 = new Station("강남역");
    Station 역삼역 = new Station("역삼역");
    Station 삼성역 = new Station("삼성역");
    Line line = new Line("2호선", "green");

    line.addSection(강남역, 역삼역, 20, 10);
    line.addSection(역삼역, 삼성역, 42, 5);

    Path path = new Path(new Sections(line.getSections()), 62);

    assertThat(path.extractFare()).isEqualTo(2350);
  }
}
