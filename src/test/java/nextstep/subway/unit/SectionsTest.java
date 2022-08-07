package nextstep.subway.unit;

import static org.assertj.core.api.Assertions.assertThat;
import nextstep.subway.domain.Line;
import nextstep.subway.domain.Sections;
import nextstep.subway.domain.Station;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

class SectionsTest {

    Station 교대역;
    Station 강남역;
    Station 삼성역;
    Line 이호선;

    @Test
    @DisplayName("총 소요 시간")
    void totalDuration() {
        //given
        교대역 = createStation(1L, "교대역");
        강남역 = createStation(2L, "강남역");
        삼성역 = createStation(3L, "삼성역");

        이호선 = new Line("2호선", "red");

        이호선.addSection(교대역, 강남역, 3, 3);
        이호선.addSection(강남역, 삼성역, 5, 5);

        //when
        Sections sections = new Sections(이호선.getSections());
        final int totalDuration = sections.totalDuration();

        //then
        assertThat(totalDuration).isEqualTo(8);
    }

    private Station createStation(long id, String name) {
        Station station = new Station(name);
        ReflectionTestUtils.setField(station, "id", id);

        return station;
    }

}