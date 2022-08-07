package nextstep.subway.unit;

import nextstep.subway.domain.DiscountPolicy;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.assertj.core.api.Assertions.assertThat;

public class DiscountPolicyTest {

    @ParameterizedTest
    @CsvSource({
            "1, 1_250, 1_250",
            "6, 1_250, 450",
            "13, 2_250, 1_520",
            "20, 1_950, 1_950"
    })
    void 나이에_따라_요금을_할인한다(int age, int currentFare, int result) {
        // given
        int fare = DiscountPolicy.calculate(age,currentFare);

        // then
        assertThat(fare).isEqualTo(result);
    }
}
