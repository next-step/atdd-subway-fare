package nextstep.subway.unit;

import static org.assertj.core.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import nextstep.subway.domain.Line;
import nextstep.subway.domain.Path;
import nextstep.subway.domain.Section;
import nextstep.subway.domain.Sections;
import nextstep.subway.domain.Station;

class PathTest {

    private Station 수서역;
    private Station 복정역;
    private Station 가천대역;
    private Station 오금역;
    private Line 분당선;
    private Line 삼호선;
    private Path 분당선_구간;
    private Path 삼호선_구간;

    /**
     *   수서역 ───── 복정역 ───── 가천대역
     *     │
     *     │
     *   오금역
     */
    @BeforeEach
    void setUp() {
        수서역 = new Station("수서역");
        복정역 = new Station("복정역");
        가천대역 = new Station("가천대역");
        오금역 = new Station("오금역");
        분당선 = new Line("분당선", "yellow");
        삼호선 = new Line("삼호선", "orange", 900);

        분당선_구간 = new Path(new Sections(List.of(
            new Section(분당선, 수서역, 복정역, 10, 5),
            new Section(분당선, 복정역, 가천대역, 8, 3)
        )));

        삼호선_구간 = new Path(new Sections(List.of(
            new Section(삼호선, 수서역, 오금역, 10, 4)
        )));
    }

    @DisplayName("지하철 구간의 총 거리를 구한다.")
    @Test
    void extractDistance() {
        // when & then
        assertThat(분당선_구간.extractDistance()).isEqualTo(18);
    }

    @DisplayName("지하철 구간의 총 소요 시간을 구한다.")
    @Test
    void extractDuration() {
        // when & then
        assertThat(분당선_구간.extractDuration()).isEqualTo(8);
    }

    @DisplayName("지하철 구간의 요금을 구한다.")
    @Test
    void extractFare() {
        // when & then
        assertThat(분당선_구간.extractFare()).isEqualTo(1_450);
    }

    @DisplayName("추가 요금이 있는 노선이 포함된 지하철 구간의 요금을 구한다.")
    @Test
    void extractFareWithExtraFare() {
        // when & then
        assertThat(삼호선_구간.extractFare()).isEqualTo(2_150);
    }
}
