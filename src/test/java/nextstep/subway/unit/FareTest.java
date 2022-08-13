package nextstep.subway.unit;

import nextstep.subway.domain.fare.DiscountPolicy;
import nextstep.subway.domain.fare.Fare;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class FareTest {
    @Test
    void calculatorTest() {
        // then
        assertThat(Fare.calculator(7)).isEqualTo(1250);
        //기본 1250 + (11~15 : 100) + (16 : 100)
        assertThat(Fare.calculator(16)).isEqualTo(1450);
        //기본 1250 + (11~50 : 800) + (51~58 : 100) + (59 : 100)
        assertThat(Fare.calculator(59)).isEqualTo(2250);

    }

    @Test
    void calculatorTestWithDiscount() {

        // 정상요금 2250, 할인요금 1520
        assertThat(DiscountPolicy.calculator(17, Fare.calculator(59))).isEqualTo(1520);
        // 정상요금 2250, 할인요금 950
        assertThat(DiscountPolicy.calculator(8, Fare.calculator(59))).isEqualTo(950);


    }

}
