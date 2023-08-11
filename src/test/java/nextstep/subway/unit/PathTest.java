package nextstep.subway.unit;

import nextstep.subway.path.domain.Path;
import nextstep.subway.section.domain.Section;
import nextstep.subway.section.domain.Sections;
import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Path 단위 테스트")
class PathTest {
    private Station 강남역;
    private Station 양재역;

    @BeforeEach
    void setUp() {
        강남역 = new Station(1L, "강남역");
        양재역 = new Station(2L, "양재역");
    }

    @DisplayName("10km 이하 거리에서는 기본요금 1,250원이 계산된다.")
    @ParameterizedTest(name = "거리={0}")
    @ValueSource(ints = {1, 5, 10})
    void feeUnder10(int distance) {
        // given
        Sections sections = new Sections(List.of(new Section(강남역, 양재역, distance, 5)));
        Path path = new Path(sections);

        // when
        int fee = path.calculateFare();

        // then
        assertThat(fee).isEqualTo(1250);
    }

    @DisplayName("10km 초과 ~ 50km 이하 거리에서는 기본요금에 5km마다 추가 100원의 요금이 계산된다.")
    @ParameterizedTest(name = "거리={0}")
    @CsvSource({"15,1350", "20,1450", "50,2050"})
    void feeOver10Under50(int distance, int expectedFee) {
        // given
        Sections sections = new Sections(List.of(new Section(강남역, 양재역, distance, 5)));
        Path path = new Path(sections);

        // when
        int actualFee = path.calculateFare();

        // then
        assertThat(actualFee).isEqualTo(expectedFee);
    }

    @DisplayName("50km 초과 거리에서는 기본요금에 8km마다 추가 100원의 요금이 계산된다.")
    @ParameterizedTest(name = "거리={0}")
    @CsvSource({"51,2150", "58,2150", "66,2250"})
    void feeOver50(int distance, int expectedFee) {
        // given
        Sections sections = new Sections(List.of(new Section(강남역, 양재역, distance, 5)));
        Path path = new Path(sections);

        // when
        int actualFee = path.calculateFare();

        // then
        assertThat(actualFee).isEqualTo(expectedFee);
    }
}