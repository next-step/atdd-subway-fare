package nextstep.subway.unit.farepolicy;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import nextstep.subway.domain.farepolicy.DistanceFarePolicy;
import nextstep.subway.domain.farepolicy.DistanceFareRange;
import nextstep.subway.domain.farepolicy.FarePolicy;

@DisplayName("50km 초과의 요금 정책 테스트")
public class DistanceFarePolicyTest {
    @CsvSource({
        "50,0",
        "51,100",
        "59,200",
        "67,300"
    })
    @DisplayName("50km를 초과하면 8km당 100원이 부과된다.")
    @ParameterizedTest
    void calculateMoreThan50(int distance, int cost) {
        FarePolicy farePolicy = new DistanceFarePolicy(
            new DistanceFareRange(50, Integer.MAX_VALUE), 8, 100
        );

        assertThat(farePolicy.calculate(distance)).isEqualTo(cost);
    }

    @CsvSource({
        "10,0",
        "12,100",
        "16,200",
        "21,300"
    })
    @DisplayName("50km 까지는 10km를 초과하는 거리에 대해 5km당 100원의 요금이 부과 된다.")
    @ParameterizedTest
    void calculateUpTo50(int distance, int cost) {
        FarePolicy farePolicy = new DistanceFarePolicy(
            new DistanceFareRange(10, 50), 5, 100
        );
        assertThat(farePolicy.calculate(distance)).isEqualTo(cost);
    }
}
