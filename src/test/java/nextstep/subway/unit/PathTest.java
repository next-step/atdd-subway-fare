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
    private Station 마천역;
    private Line 분당선;
    private Line 삼호선;
    private Line 오호선;
    private Path 분당선_구간;
    private Path 삼호선_구간;
    private Path 삼호선_오호선_구간;

    /**
     *   수서역 ─── *분당선* ─── 복정역 ─── *분당선* ─── 가천대역
     *     │
     *  *3호선* (추가 요금: 900원)
     *     │
     *   오금역
     *     │
     *  *5호선* (추가 요금: 1,200원)
     *     │
     *   마천역
     */
    @BeforeEach
    void setUp() {
        수서역 = new Station("수서역");
        복정역 = new Station("복정역");
        가천대역 = new Station("가천대역");
        오금역 = new Station("오금역");
        마천역 = new Station("마천역");
        분당선 = new Line("분당선", "yellow");
        삼호선 = new Line("3호선", "orange", 900);
        오호선 = new Line("5호선", "purple", 1200);

        분당선_구간 = 지하철_경로_생성(
            new Section(분당선, 수서역, 복정역, 10, 5),
            new Section(분당선, 복정역, 가천대역, 8, 3)
        );

        삼호선_구간 = 지하철_경로_생성(
            new Section(삼호선, 수서역, 오금역, 5, 4)
        );

        삼호선_오호선_구간 = 지하철_경로_생성(
            new Section(삼호선, 수서역, 오금역, 5, 4),
            new Section(오호선, 오금역, 마천역, 4, 2)
        );
    }

    private Path 지하철_경로_생성(Section... sections) {
        return new Path(new Sections(List.of(sections)));
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
    void extractFareWithExtraFareLine() {
        // when & then
        assertThat(삼호선_구간.extractFare()).isEqualTo(2_150);
    }

    @DisplayName("추가 요금이 있는 여러 노선이 포함된 지하철 구간일 경우, 추가 요금은 추가 요금 중 가장 높은 금액으로 적용한다.")
    @Test
    void extraFareWithExtraFareLines() {
        // when & then
        assertThat(삼호선_오호선_구간.extractFare()).isEqualTo(2_450);
    }
}
