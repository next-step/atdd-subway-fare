package nextstep.path.fare;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

class FareTest {

    @DisplayName("요금이 0보다 적을 경우 에러를 반환한다.")
    @Test
    void validateNegativeNumber() {
        assertThatThrownBy(() -> Fare.of(-100))
                .isInstanceOf(NegativeNumberException.class)
                .hasMessageContaining("요금은 0보다 적을 수 없습니다");
    }

    @DisplayName("요금 더하기")
    @Test
    void plus() {
        Fare 이백원 = Fare.of(200);
        Fare 삼백원 = Fare.of(300);
        Fare 오백원 = Fare.of(500);
        Assertions.assertThat(이백원.plus(삼백원)).isEqualTo(오백원);
    }

}