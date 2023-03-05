package nextstep.subway.unit;

import nextstep.subway.domain.FarePolicy;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.assertj.core.api.Assertions.assertThat;

public class FarePolicyTest {

    @ParameterizedTest
    @CsvSource({
            "9,1_250",
            "10,1_250",
            "11,1_350",

            "49,2_050",
            "50,2_050",
            "51,2_150",
    })
    void calculate(int distance, int fare) {
        assertThat(FarePolicy.calculate(distance)).isEqualTo(fare);
    }
}
