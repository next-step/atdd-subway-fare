package nextstep.subway.unit;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static nextstep.subway.domain.Fare.calculate;
import static org.assertj.core.api.Assertions.assertThat;

public class FareTest {

    @ParameterizedTest
    @CsvSource({
            "10, 500, 13, 1_120",
            "50, 1_000, 20, 3_250",
            "80, 0, 6, 950"
    })
    void 요금을_계산한다(int distance, int surcharge, int userAge, int totalFare) {
        // given
        int fare = calculate(distance, surcharge, userAge);

        // then
        assertThat(fare).isEqualTo(totalFare);
    }

    @ParameterizedTest
    @CsvSource({
            "10, 1_250",
            "50, 2_250",
            "51, 1_950"
    })
    void 거리에_따라_요금을_계산한다(int distance, int totalFare) {
        // given
        int fare = calculate(distance, 0, 0);

        // then
        assertThat(fare).isEqualTo(totalFare);
    }

    @ParameterizedTest
    @CsvSource({
            "10, 450",
            "13, 720",
            "20, 1_250"
    })
    void 나이에_따라_요금을_계산한다(int userAge, int totalFare) {
        // given
        int fare = calculate(10, 0, userAge);

        // then
        assertThat(fare).isEqualTo(totalFare);
    }

    @ParameterizedTest
    @CsvSource({
            "1_000, 2_250",
            "500, 1_750",
            "0, 1_250"
    })
    void 추가요금에_따라_요금을_계산한다(int surcharge, int totalFare) {
        // given
        int fare = calculate(10, surcharge, 0);

        // then
        assertThat(fare).isEqualTo(totalFare);
    }
}
