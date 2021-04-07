package nextstep.subway.path.domain;

import nextstep.subway.path.domain.fare.FareCalculator;
import nextstep.subway.path.domain.fare.policy.DefaultEqualTo10Policy;
import nextstep.subway.path.domain.fare.policy.Over10EqualTo50Policy;
import nextstep.subway.path.domain.fare.policy.Over50Policy;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.assertj.core.api.Assertions.assertThat;

public class FareCalculatorTest {

    private FareCalculator fareCalculator = new FareCalculator();

    @BeforeEach
    void setUp() {
        // given
        fareCalculator.addPolicy(new DefaultEqualTo10Policy());
        fareCalculator.addPolicy(new Over10EqualTo50Policy());
        fareCalculator.addPolicy(new Over50Policy());
    }

    @ParameterizedTest
    @DisplayName("기본운임(10㎞ 이내) : 기본운임 1,250원")
    @CsvSource({"0, 1250", "5, 1250", "10, 1250"})
    void defaultPolicy(int distance, int expectedFare) {
        // when
        int totalFare = fareCalculator.calculate(distance);

        // then
        assertThat(totalFare).isEqualTo(expectedFare);
    }

    @ParameterizedTest
    @DisplayName("이용 거리초과 시 추가운임 부과. 10km초과∼50km까지(5km마다 100원)")
    @CsvSource({"11, 1350", "50, 2050"})
    void over10EqualTo50Policy(int distance, int expectedFare) {
        // when
        int totalFare = fareCalculator.calculate(distance);

        // then
        assertThat(totalFare).isEqualTo(expectedFare);
    }

    @ParameterizedTest
    @DisplayName("이용 거리초과 시 추가운임 부과. 50km초과 시 (8km마다 100원)")
    @CsvSource({"51, 2150", "59, 2250"})
    void over50Policy(int distance, int expectedFare) {
        // when
        int totalFare = fareCalculator.calculate(distance);

        // then
        assertThat(totalFare).isEqualTo(expectedFare);
    }
}
