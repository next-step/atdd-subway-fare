package nextstep.subway.unit;

import nextstep.subway.domain.AgeFarePolicy;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.assertj.core.api.Assertions.assertThat;

class AgeFarePolicyTest {

    /**
     * 어린이: 6세 이상~ 13세 미만   ; 50% 할인
     * 청소년: 13세 이상 ~ 19세 미만 ; 20% 할인
     */
    @ParameterizedTest
    @CsvSource({
            "1_000, 5, 1_000",
            "1_000, 6, 500",
            "1_000, 7, 500",

            "1_000, 12, 500",
            "1_000, 13, 800",
            "1_000, 13, 800",

            "1_000, 18, 800",
            "1_000, 19, 1_000",
            "1_000, 20, 1_000",
    })
    void calculateTotalFare(int fare, int age, int discountedFare) {
        assertThat(AgeFarePolicy.calculate(fare, age)).isEqualTo(discountedFare);
    }
}
