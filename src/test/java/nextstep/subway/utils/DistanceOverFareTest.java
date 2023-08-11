package nextstep.subway.utils;

import static org.assertj.core.api.Assertions.assertThat;

import nextstep.subway.domain.farechain.overfare.DistanceOverFare;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class DistanceOverFareTest {

    @ParameterizedTest
    @ValueSource(ints = {1, 10})
    @DisplayName("10km 까지는 거리에따른 추가요금이 없음")
    void defaultFare(int distance) {

        DistanceOverFare distanceOverFare = new DistanceOverFare(distance);
        assertThat(distanceOverFare.getOverDistanceFare()).isZero();
    }

    @ParameterizedTest
    @ValueSource(ints = {11, 14, 15})
    @DisplayName("10km 부터는 5km 당 100원 (1번 부과)")
    void calFareUnder50_1(int distance) {

        DistanceOverFare distanceOverFare = new DistanceOverFare(distance);
        assertThat(distanceOverFare.getOverDistanceFare()).isEqualTo(100);
    }

    @ParameterizedTest
    @ValueSource(ints = {16, 19})
    @DisplayName("10km 부터는 5km 당 100원 (2번 부과)")
    void calFareUnder50_2(int distance) {

        DistanceOverFare distanceOverFare = new DistanceOverFare(distance);
        assertThat(distanceOverFare.getOverDistanceFare()).isEqualTo(200);
    }

    @Test
    @DisplayName("50km 이면 8번 부과")
    void calFareWhen50km() {

        DistanceOverFare distanceOverFare = new DistanceOverFare(50);
        assertThat(distanceOverFare.getOverDistanceFare()).isEqualTo(800);
    }

    @ParameterizedTest
    @ValueSource(ints = {51,58})
    @DisplayName("50km 초과시 8km 마다 100원 (1번 부과)")
    void calFareWhen50km(int distance) {

        DistanceOverFare distanceOverFare = new DistanceOverFare(distance);
        assertThat(distanceOverFare.getOverDistanceFare()).isEqualTo(800 + 100);
    }
}