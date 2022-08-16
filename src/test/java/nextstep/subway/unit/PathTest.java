package nextstep.subway.unit;

import static org.assertj.core.api.Assertions.assertThat;

import nextstep.subway.domain.Line;
import nextstep.subway.domain.path.Path;
import nextstep.subway.domain.Sections;
import nextstep.subway.domain.Station;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class PathTest {

  private Station 강남역;
  private Station 역삼역;
  private Station 삼성역;

  private Line 이호선;

  @BeforeEach
  void setup() {
    강남역 = new Station("강남역");
    역삼역 = new Station("역삼역");
    삼성역 = new Station("삼성역");

    이호선 = new Line("2호선", "green");
  }

  @Test
  void 구간_조회_총_시간() {
    이호선.addSection(강남역, 역삼역, 10, 10);
    이호선.addSection(역삼역, 삼성역, 5, 5);

    Path path = new Path(new Sections(이호선.getSections()), 15);

    assertThat(path.extractDuration()).isEqualTo(15);
  }

  @Test
  void 구간_길이_10KM_이내_요금_조회() {
    이호선.addSection(강남역, 역삼역, 4, 10);
    이호선.addSection(역삼역, 삼성역, 5, 5);

    Path path = new Path(new Sections(이호선.getSections()), 9);

    assertThat(path.extractFare()).isEqualTo(1_250);
  }

  @Test
  void 구간_길이_10KM_초과_50KM_이하_요금_조회() {
    이호선.addSection(강남역, 역삼역, 10, 10);
    이호선.addSection(역삼역, 삼성역, 15, 5);

    Path path = new Path(new Sections(이호선.getSections()), 25);

    assertThat(path.extractFare()).isEqualTo(1_550);
  }

  @Test
  void 구간_길이_50KM_초과_요금_조회() {
    이호선.addSection(강남역, 역삼역, 20, 10);
    이호선.addSection(역삼역, 삼성역, 42, 5);

    Path path = new Path(new Sections(이호선.getSections()), 62);

    assertThat(path.extractFare()).isEqualTo(2_250);
  }
}
