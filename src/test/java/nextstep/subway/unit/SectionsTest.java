package nextstep.subway.unit;

import nextstep.subway.domain.Fare;
import nextstep.subway.domain.Line;
import nextstep.subway.domain.Section;
import nextstep.subway.domain.Sections;
import nextstep.subway.domain.Station;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static nextstep.subway.acceptance.LineSteps.구간_생성;
import static nextstep.subway.acceptance.LineSteps.노선_생성;
import static nextstep.subway.acceptance.StationSteps.역_생성;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("구간들 관련 기능")
class SectionsTest {

    private Line 이호선;
    private Line 신분당선;
    private Line 삼호선;
    private Section 이호선_구간;
    private Section 신분당선_구간;
    private Section 삼호선_구간1;
    private Section 삼호선_구간2;
    private Station 강남역;
    private Station 역삼역;
    private Station 삼성역;
    private Station 양재역;

    @BeforeEach
    void setUp() {
        강남역 = 역_생성(1L, "강남역");
        역삼역 = 역_생성(2L, "역삼역");
        삼성역 = 역_생성(3L, "삼성역");
        양재역 = 역_생성(4L, "양재역");

        이호선 = 노선_생성(1L, "2호선", "green", BigDecimal.valueOf(900));
        이호선_구간 = 구간_생성(1L, 이호선, 강남역, 역삼역, 5, 10);
        신분당선 = 노선_생성(2L, "신분당선", "green", BigDecimal.valueOf(600));
        신분당선_구간 = 구간_생성(2L, 신분당선, 역삼역, 삼성역, 10, 20);
        삼호선 = 노선_생성(3L, "삼호선", "green", BigDecimal.valueOf(600));
        삼호선_구간1 = 구간_생성(3L, 삼호선, 강남역, 양재역, 3, 6);
        삼호선_구간2 = 구간_생성(4L, 삼호선, 양재역, 삼성역, 2, 4);
    }

    @DisplayName("추가요금이 제일 높은 구간을 조회한다.")
    @Test
    void getMaxExtraFare() {
        final Sections sections = new Sections(Lists.newArrayList(이호선_구간, 신분당선_구간, 삼호선_구간1, 삼호선_구간2));
        final Fare actual = sections.getMaxExtraFare();

        assertThat(actual).isEqualTo(Fare.from(900));
    }
}
