package nextstep.subway.unit;

import nextstep.subway.domain.Line;
import nextstep.subway.domain.Section;
import nextstep.subway.domain.Sections;
import nextstep.subway.domain.Station;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class SectionsTest {

    private Sections sections;

    @BeforeEach
    void setUp() {
        Line line = new Line("2호선", "green");
        Station 사당역 = new Station("사당역");
        Station 방배역 = new Station("방배역");
        Station 논현역 = new Station("논현역");

        List<Section> sections = new ArrayList<>();
        sections.add(new Section(line, 사당역, 방배역, 5, 10));
        sections.add(new Section(line, 사당역, 논현역, 5, 10));
        this.sections = new Sections(sections);
    }

    @Test
    @DisplayName("구간 추가요금이 없으면 0원이 반환된다.")
    void getSurchargeTest() {
        assertThat(sections.extractAdditionalFare()).isZero();
    }

    @Test
    @DisplayName("구간 요금이 2건 이상이면 가장 큰 추가 요금이 반환된다.")
    void getSurchargeMultiTest() {
        // given
        Line 신분당선 = new Line("신분당선", "red", 1200);
        Station 강남역 = new Station("강남역");
        Station 양재역 = new Station("양재역");

        Line 삼호선 = new Line("3호선", "orange", 900);
        Station 매봉역 = new Station("매봉역");

        sections.add(new Section(신분당선, 강남역, 양재역, 5, 10));
        sections.add(new Section(삼호선, 양재역, 매봉역, 5, 10));

        // when, then
        assertThat(sections.extractAdditionalFare()).isEqualTo(1200);
    }
}
