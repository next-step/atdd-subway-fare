package nextstep.subway.unit;

import nextstep.subway.path.domain.fare.DistanceFarePolicies;
import nextstep.subway.path.domain.fare.LongDistanceFarePolicy;
import nextstep.subway.path.domain.fare.MiddleDistanceFarePolicy;
import nextstep.subway.path.domain.fare.ShortDistanceFarePolicy;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class DistanceFarePoliciesTest {
    private DistanceFarePolicies distanceFarePolicies;

    @BeforeEach
    void setUp() {
        distanceFarePolicies = new DistanceFarePolicies(
                List.of(
                        new ShortDistanceFarePolicy(),
                        new MiddleDistanceFarePolicy(),
                        new LongDistanceFarePolicy()
                )
        );
    }

    @DisplayName("10km 이하 거리에서는 기본요금 1,250원이 계산된다.")
    @ParameterizedTest(name = "거리={0}")
    @ValueSource(ints = {1, 5, 10})
    void feeUnder10(int distance) {
        // when
        int fee = distanceFarePolicies.calculateFare(distance);

        // then
        assertThat(fee).isEqualTo(1250);
    }

    @DisplayName("10km 초과 ~ 50km 이하 거리에서는 기본요금에 5km마다 추가 100원의 요금이 계산된다.")
    @ParameterizedTest(name = "거리={0}")
    @CsvSource({"14,1350", "16,1450", "19,1450", "21,1550", "50,2050"})
    void feeOver10Under50(int distance, int expectedFee) {
        // when
        int actualFee = distanceFarePolicies.calculateFare(distance);

        // then
        assertThat(actualFee).isEqualTo(expectedFee);
    }

    @DisplayName("50km 초과 거리에서는 기본요금에 8km마다 추가 100원의 요금이 계산된다.")
    @ParameterizedTest(name = "거리={0}")
    @CsvSource({"51,2150", "58,2150", "59,2250", "66,2250"})
    void feeOver50(int distance, int expectedFee) {
        // when
        int actualFee = distanceFarePolicies.calculateFare(distance);

        // then
        assertThat(actualFee).isEqualTo(expectedFee);
    }
}