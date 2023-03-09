package nextstep.subway.domain;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

@DisplayName("요금 정책 관련 기능")
class FarePolicyTest {

    private Line line;
    private Station 강남역;
    private Station 양재역;

    @BeforeEach
    void setUp() {
        this.line = new Line("2호선", "green");
        강남역 = new Station("강남역");
        양재역 = new Station("양재역");
    }

    @DisplayName("거리 기준으로 요금을 계산한다.")
    @ParameterizedTest
    @CsvSource(
            value = {"1:1250", "10:1250", "11:1350", "50:2050", "51:2150", "58:2150", "59:2250", "66:2250", "67:2350"},
            delimiter = ':'
    )
    void calculatorOverFareByDistance(int distance, int fare) {
        Sections sections = new Sections(List.of(new Section(line, 강남역, 양재역, distance, 10)));
        FarePolicy farePolicy = new DistanceFarePolicy(new BasicDistanceFareFormula());
        Path path = new Path(sections, farePolicy);

        assertThat(farePolicy.apply(path)).isEqualTo(fare);
    }
}
