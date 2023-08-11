package nextstep.subway.section.domain;

import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Sections 단위 테스트")
class SectionsTest {
    @DisplayName("10km 미만 거리에서는 기본요금 1,250원이 계산된다.")
    @ParameterizedTest(name = "거리={0} + {1}")
    @CsvSource({"1,2", "3,4", "4,5"})
    void feeUnder10(int dist1, int dist2) {
        Station 강남역 = new Station(1L, "강남역");
        Station 남부터미널역 = new Station(2L, "남부터미널역");
        Station 양재역 = new Station(3L, "양재역");

        Section section1 = new Section(강남역, 남부터미널역, dist1, 5);
        Section section2 = new Section(남부터미널역, 양재역, dist2, 5);

        Sections sections = new Sections(List.of(section1, section2));

        int fee = sections.calculateFee();
        assertThat(fee).isEqualTo(1250);
    }
}