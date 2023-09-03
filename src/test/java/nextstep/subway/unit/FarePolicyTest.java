package nextstep.subway.unit;

import nextstep.subway.path.domain.Path;
import nextstep.subway.path.domain.policy.fare.*;
import nextstep.subway.unit.mockobj.MockPath;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class FarePolicyTest {
    private DistanceFarePolicy distanceFarePolicy;

    @BeforeEach
    void setUp() {
        List<DistanceFareRule> distanceFareRules = List.of(
                new ShortDistanceFareRule(),
                new MiddleDistanceFareRule(),
                new LongDistanceFareRule()
        );

        distanceFarePolicy = new DistanceFarePolicy(distanceFareRules);
    }

    @DisplayName("10km 이하 거리에서는 기본요금 1,250원이 계산된다.")
    @ParameterizedTest(name = "거리={0}")
    @ValueSource(ints = {1, 5, 10})
    void feeUnder10(int distance) {
        // given
        Path path = new MockPath(distance);

        // when
        int fee = distanceFarePolicy.calculateFare(path);

        // then
        assertThat(fee).isEqualTo(1250);
    }

    @DisplayName("10km 초과 ~ 50km 이하 거리에서는 기본요금에 5km마다 추가 100원의 요금이 계산된다.")
    @ParameterizedTest(name = "거리={0}")
    @CsvSource({"14,1350", "16,1450", "19,1450", "21,1550", "50,2050"})
    void feeOver10Under50(int distance, int expectedFee) {
        // given
        Path path = new MockPath(distance);

        // when
        int actualFee = distanceFarePolicy.calculateFare(path);

        // then
        assertThat(actualFee).isEqualTo(expectedFee);
    }

    @DisplayName("50km 초과 거리에서는 기본요금에 8km마다 추가 100원의 요금이 계산된다.")
    @ParameterizedTest(name = "거리={0}")
    @CsvSource({"51,2150", "58,2150", "59,2250", "66,2250"})
    void feeOver50(int distance, int expectedFee) {
        // given
        Path path = new MockPath(distance);

        // when
        int actualFee = distanceFarePolicy.calculateFare(path);

        // then
        assertThat(actualFee).isEqualTo(expectedFee);
    }
}