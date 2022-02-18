package nextstep.subway.unit;

import nextstep.subway.domain.Fare;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.assertj.core.api.Assertions.assertThat;

public class FareTest {

    @ParameterizedTest
    @CsvSource({"10, 1250", "20, 1450", "66, 1950"})
    void allocateFare(int totalDistance, int expectedFare) {
        // given
        Fare fare = new Fare(totalDistance);

        // when
        int actualFare = fare.impose();

        // then
        assertThat(actualFare).isEqualTo(expectedFare);
    }
}
