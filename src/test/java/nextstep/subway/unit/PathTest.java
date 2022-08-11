package nextstep.subway.unit;

import static org.assertj.core.api.Assertions.assertThat;

import nextstep.subway.domain.Line;
import nextstep.subway.domain.Path;
import nextstep.subway.domain.Sections;
import nextstep.subway.domain.Station;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

public class PathTest {

    Station 교대역;
    Station 강남역;
    Station 삼성역;
    Station 남부터미널역;

    Line 이호선;

    @BeforeEach
    void setUp() {
        //given
        교대역 = createStation(1L, "교대역");
        강남역 = createStation(2L, "강남역");
        남부터미널역 = createStation(3L, "남부터미널역");
        삼성역 = createStation(4L, "삼성역");

        이호선 = new Line("2호선", "red");
    }

    private Station createStation(long id, String name) {
        Station station = new Station(name);
        ReflectionTestUtils.setField(station, "id", id);

        return station;
    }

    @Test
    @DisplayName("10km 이하 기본 요금 테스트")
    void basicCalculatePrice() {
        // given
        이호선.addSection(교대역, 강남역, 5, 5);
        이호선.addSection(강남역, 삼성역, 5, 10);
        Sections 구간들 = new Sections(이호선.getSections());

        // when
        Path path = new Path(구간들);
        int result = path.calculatePrice();

        // then
        assertThat(result).isEqualTo(1250);
    }

    @Test
    @DisplayName("10km 초과 요금 계산 테스트")
    void over10CalculatePrice() {
        // given
        이호선.addSection(교대역, 강남역, 10, 5);
        이호선.addSection(강남역, 삼성역, 10, 10);
        Sections 구간들 = new Sections(이호선.getSections());

        // when
        Path path = new Path(구간들);
        int result = path.calculatePrice();

        // then
        assertThat(result).isEqualTo(1450);
    }

    @Test
    @DisplayName("50km 이상 요금 테스트")
    void over50CalculatePrice() {
        // given
        이호선.addSection(교대역, 강남역, 30, 5);
        이호선.addSection(강남역, 삼성역, 50, 10);
        Sections 구간들 = new Sections(이호선.getSections());

        // when
        Path path = new Path(구간들);
        int result = path.calculatePrice();

        // then
        assertThat(result).isEqualTo(2450);
    }

}
