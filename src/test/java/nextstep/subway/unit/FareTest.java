package nextstep.subway.unit;

import nextstep.subway.domain.fare.DistancePolicy;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.assertj.core.api.Assertions.assertThat;

class FareTest {

    @CsvSource({"10, 1250", "16, 1450", "59, 1950"})
    @ParameterizedTest
    void 거리별_요금_계산(int distance, int expectedFare) {
        assertThat(DistancePolicy.calculate(distance)).isEqualTo(expectedFare);
    }
}
