package nextstep.subway.path.domain;

import nextstep.subway.line.domain.LineFare;
import nextstep.subway.path.application.DistanceProportionFareCalculator;
import nextstep.subway.path.application.FareCalculator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("운임 요금 계산기 테스트")
public class DistanceProportionFareCalculatorTest {

    private final FareCalculator fareCalculator = new DistanceProportionFareCalculator(LineFare.ADULT);

    @DisplayName("기본운임(10㎞ 이내) : 기본운임 1,250원")
    @ValueSource(ints = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10})
    @ParameterizedTest
    void calculateDefaultFareTest(int distance) {
        assertThat(LineFare.ADULT.getFare())
            .isEqualTo(fareCalculator.calculateFare(distance));
    }

    @Nested
    @DisplayName("이용 거리초과 시 추가운임 부과")
    class 초과_요금_계산 {

        @DisplayName("10km초과 ∼ 50km까지(5km마다 100원)")
        @CsvSource({"15,1350", "20,1450", "25,1550", "30,1650", "35,1750", "40,1850", "45,1950", "50,2050" })
        @ParameterizedTest
        void calculateOver10FareTest(int distance, int expected) {
            assertThat(expected)
                .isEqualTo(fareCalculator.calculateFare(distance));
        }

        @DisplayName("50km초과 시 (8km마다 100원)")
        @CsvSource({"58,2150", "66,2250", "72,2350", "80,2450" })
        @ParameterizedTest
        void calculateOver50FareTest(int distance, int expected) {
            assertThat(expected)
                .isEqualTo(fareCalculator.calculateFare(distance));
        }

    }
}
