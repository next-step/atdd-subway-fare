package nextstep.subway.unit;

import nextstep.subway.domain.Line;
import nextstep.subway.domain.Sections;
import nextstep.subway.domain.Station;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("구간들 (Sections)")
class SectionsTest {

    private Station 강남역;
    private Station 역삼역;
    private Station 삼성역;
    private Line 이호선;

    @BeforeEach
    void setUp() {
        강남역 = new Station("강남역");
        역삼역 = new Station("역삼역");
        삼성역 = new Station("삼성역");
        이호선 = new Line("2호선", "green");
    }

    @DisplayName("구간의 모든 소요시간의 합을 구한다.(새로 연장된)")
    @Test
    void totalDurationByNewSection() {
        // given
        이호선.addSection(강남역, 역삼역, 10, 10);
        이호선.addSection(역삼역, 삼성역, 5, 5);

        final Sections sections = new Sections(이호선.getSections());

        // when and then
        assertThat(sections.totalDuration()).isEqualTo(15);
    }

    @DisplayName("구간의 모든 소요시간의 합을 구한다.(중간에 추가된)")
    @Test
    void totalDurationByMiddleSection() {
        // given
        이호선.addSection(강남역, 역삼역, 10, 10);
        이호선.addSection(강남역, 삼성역, 5, 5);

        final Sections sections = new Sections(이호선.getSections());

        // when and then
        assertThat(sections.totalDuration()).isEqualTo(10);
    }

    @DisplayName("구간의 모든 소요시간의 합을 구한다.(앞에 추가된)")
    @Test
    void totalDurationByAfterSection() {
        // given
        이호선.addSection(역삼역, 삼성역, 5, 5);
        이호선.addSection(강남역, 역삼역, 10, 10);

        final Sections sections = new Sections(이호선.getSections());

        // when and then
        assertThat(sections.totalDuration()).isEqualTo(15);
    }
}
