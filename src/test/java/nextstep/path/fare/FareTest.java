package nextstep.path.fare;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.assertj.core.api.Assertions.assertThat;


class FareTest {

    @DisplayName("요금 계산")
    @ParameterizedTest
    @CsvSource({"10, 1250", "11, 1350", "15, 1350", "16, 1450", "50, 2050", "51, 2150", "58, 2150", "59, 2250"})
    void calculate(int distance, int expected) {
        int fare = Fare.calculate(distance);
        assertThat(fare).isEqualTo(expected);
    }

}