package nextstep.subway.domain;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("구간 목록 관련 기능")
class SectionsTest {

    @DisplayName("구간 목록에 노선 중 가장 높은 요금을 반환한다.")
    @Test
    void extractHighestAdditionalFare() {
        Line 이호선 = new Line("2호선", "red");
        Line 삼호선 = new Line("3호선", "red", 300);
        Line 사호선 = new Line("4호선", "red", 900);
        Station 강남역 = new Station("강남역");
        Station 양재역 = new Station("양재역");
        Station 정자역 = new Station("정자역");
        Station 선릉역 = new Station("선릉역");

        Sections sections = new Sections(List.of(
                new Section(이호선, 강남역, 양재역, 10, 10),
                new Section(삼호선, 양재역, 정자역, 10, 10),
                new Section(사호선, 정자역, 선릉역, 10, 10)
        ));

        assertThat(sections.extractHighestAdditionalFare()).isEqualTo(900);
    }
}
