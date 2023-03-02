package nextstep.subway.domain;

import nextstep.subway.domain.exceptions.NegativeNumberException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class FareTest {

    @Test
    void 이용요금을_정상적으로_생성한다() {
        assertThat(Fare.of(100)).isNotNull();
    }

    @Test
    void 이용요금이_음수이거나_0인_경우_예외가_발생한다() {
        int value = -1;
        assertThatThrownBy(() -> Fare.of(value)).isInstanceOf(NegativeNumberException.class);
    }

    @Test
    void 이용요금에_이용요금을_더할_수_있다() {
        var five = Fare.of(5);
        var three = Fare.of(3);
        var eight = Fare.of(8);

        assertThat(five.plus(three)).isEqualTo(eight);
    }

    @Test
    void 이용요금에_이용요금을_뺄_수_있다() {
        var eight = Fare.of(8);
        var five = Fare.of(5);
        var three = Fare.of(3);

        assertThat(eight.minus(five)).isEqualTo(three);
    }

    @ParameterizedTest
    @CsvSource({
            "10, 10, 1",
            "20, 50, 10",
            "15, 100, 15"
    })
    void 이용요금의_퍼센트에_해당하는_요금을_구할_수_있다(int valueStr, int percent, int expectedStr) {
        // given
        Fare fare = Fare.of(valueStr);
        Fare expectedFare = Fare.of(expectedStr);

        // when
        Fare result = fare.ofPercent(percent);

        // then
        assertThat(result).isEqualTo(expectedFare);
    }
}
