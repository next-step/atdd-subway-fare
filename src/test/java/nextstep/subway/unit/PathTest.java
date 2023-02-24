package nextstep.subway.unit;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import nextstep.subway.domain.Line;
import nextstep.subway.domain.Path;
import nextstep.subway.domain.Sections;
import nextstep.subway.domain.Station;

class PathTest {

    private Station 수서역;
    private Station 복정역;
    private Station 가천대역;
    private Line 분당선;
    private Path path;

    /**
     *   수서역 ───── 복정역 ───── 가천대역
     */
    @BeforeEach
    void setUp() {
        수서역 = new Station("수서역");
        복정역 = new Station("복정역");
        가천대역 = new Station("가천대역");
        분당선 = new Line("분당선", "yellow");

        분당선.addSection(수서역, 복정역, 10, 5);
        분당선.addSection(복정역, 가천대역, 8, 3);

        path = new Path(new Sections(분당선.getSections()));
    }

    @DisplayName("지하철 구간의 총 거리를 구한다.")
    @Test
    void extractDistance() {
        // when & then
        assertThat(path.extractDistance()).isEqualTo(18);
    }

    @DisplayName("지하철 구간의 총 소요 시간을 구한다.")
    @Test
    void extractDuration() {
        // when & then
        assertThat(path.extractDuration()).isEqualTo(8);
    }

    @DisplayName("지하철 구간의 요금을 구한다.")
    @Test
    void extractFare() {
        // when & then
        assertThat(path.extractFare()).isEqualTo(1_450);
    }
}
