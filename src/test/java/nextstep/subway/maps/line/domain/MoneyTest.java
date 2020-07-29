package nextstep.subway.maps.line.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class MoneyTest {

    @Test
    @DisplayName("돈은 음수일 수 없다")
    void noNegativeAmount() {
        assertThatThrownBy(() -> Money.wons(-1))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("돈을 생성한다.")
    void createMoney() {
        Money wons = Money.wons(1000);

        assertThat(wons).isNotNull();
    }

    @Test
    @DisplayName("같은 양의 돈은 같은 값으로 인식한다.")
    void equals() {
        assertThat(Money.wons(1000)).isEqualTo(Money.wons(1000));
    }

    @Test
    @DisplayName("돈의 양으로 Money 객체의 값 비교가 가능하다")
    void compare() {
        assertThat(Money.wons(1000).compareTo(Money.ZERO)).isPositive();
        assertThat(Money.wons(1000).compareTo(Money.wons(1000))).isZero();
        assertThat(Money.ZERO.compareTo(Money.wons(1000))).isNegative();
    }
}