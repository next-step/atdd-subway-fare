package nextstep.subway.unit;

import nextstep.subway.domain.fare.DistancePolicy;
import nextstep.subway.domain.fare.Fare;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.assertj.core.api.Assertions.assertThat;

class DistancePolicyTest {

    @ParameterizedTest
    @CsvSource({"10, 0", "11, 5", "51, 8"})
    void decide(int totalDistance, int distance) {
        // when
        DistancePolicy policy = DistancePolicy.decide(totalDistance);

        // then
        assertThat(policy.getDistance()).isEqualTo(distance);
    }

    @ParameterizedTest
    @CsvSource({"10, 0", "11, 100", "51, 600"})
    void calculateOverFare(int totalDistance, int expectedOverFare) {
        // given
        DistancePolicy policy = DistancePolicy.decide(totalDistance);

        // when
        int overFare = policy.calculateOverFare(totalDistance, Fare.BASIC_DISTANCE_OVER_FARE);

        // then
        assertThat(overFare).isEqualTo(expectedOverFare);
    }
}