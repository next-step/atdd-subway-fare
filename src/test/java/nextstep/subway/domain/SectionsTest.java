package nextstep.subway.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class SectionsTest {
    @DisplayName("최대 추가 요금을 조회한다.")
    @Test
    void getMaxExtraCharge() {
        // given
        Station 교대역 = new Station(1L, "교대역");
        Station 강남역 = new Station(2L, "강남역");
        Station 양재역 = new Station(3L, "양재역");
        Line 이호선 = new Line("2호선", "green", 100);
        Line 신분당선 = new Line("신분당선", "red", 900);
        List<Section> sections = new ArrayList<>();
        sections.add(new Section(이호선, 교대역, 강남역, 10, 1));
        sections.add(new Section(신분당선, 강남역, 양재역, 10, 3));

        // when
        int maxExtraCharge = new Sections(sections).getMaxExtraCharge();

        // then
        assertThat(maxExtraCharge).isEqualTo(900);
    }
}
