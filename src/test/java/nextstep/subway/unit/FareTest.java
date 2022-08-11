package nextstep.subway.unit;

import nextstep.subway.domain.Fare;
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

}
