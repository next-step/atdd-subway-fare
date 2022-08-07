package nextstep.subway.unit;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static nextstep.subway.domain.Fare.calculate;
import static org.assertj.core.api.Assertions.assertThat;

public class FareTest {

    @ParameterizedTest
    @CsvSource({
            "10, 1_250",
            "50, 2_250",
            "51, 1_950"
    })
    void 거리에_따라_요금을_계산한다(int distance, int totalFare) {
        // given
        int fare = calculate(distance);

        // then
        assertThat(fare).isEqualTo(totalFare);
    }
}
