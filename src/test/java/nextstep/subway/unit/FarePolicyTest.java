package nextstep.subway.unit;

import nextstep.subway.domain.fare.distance.DistanceFarePolicy;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("거리별 추가 요금")
public class FarePolicyTest {

    @DisplayName("구간 거리 10km이하")
    @ParameterizedTest
    @ValueSource(ints = {9, 10})
    void findFare_구간거리_10km이하(int distance) {
        DistanceFarePolicy distanceFarePolicy = DistanceFarePolicy.create(distance);
        assertThat(distanceFarePolicy.calculateFare()).isEqualTo(1250);
    }

    @DisplayName("구간 거리 11KM ~ 50KM 요금 조회")
    @ParameterizedTest
    @ValueSource(ints = {11, 15})
    void findFare_구간거리_10km이상_50km이하(int distance) {
        DistanceFarePolicy farePolicy = DistanceFarePolicy.create(distance);
        assertThat(farePolicy.calculateFare()).isEqualTo(1350);
    }

    @DisplayName("구간 거리 50km 이상 요금 조회")
    @Test
    @ParameterizedTest
    @ValueSource(ints = {59, 66})
    void findFare_50km이상() {
        DistanceFarePolicy distanceFarePolicy = DistanceFarePolicy.create(66);
        assertThat(distanceFarePolicy.calculateFare()).isEqualTo(2250);
    }

}

