package nextstep.subway.maps.line.domain;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class MoneyTest {

    @Test
    @DisplayName("돈은 음수일 수 없다")
    void 돈은_음수가_아니다() {
        assertThatThrownBy(
            () -> Money.drawNewMoney(-1)
        ).isInstanceOf(IllegalArgumentException.class).hasMessage("money cannot be less then zero.");
    }

    @Test
    @DisplayName("돈을 생성한다.")
    void 새로운_돈을_생성한다() {
        // given
        Money money = Money.drawNewMoney(1000);

        // when
        assertThat(money).isNotNull();
    }

    @DisplayName("같은 숫자를 갖는 돈은 같은 것으로 취급한다.")
    @Test
    void 같은_값은_서로_같다() {
        assertAll(
            () -> assertThat(Money.drawNewMoney(1000)).isEqualTo(Money.drawNewMoney(1000)),
            () -> assertThat(Money.NO_VALUE()).isEqualTo(Money.NO_VALUE())
        );
    }

    @DisplayName("돈의 숫자로 서로 대소비교를 할 수 있다.")
    @Test
    void 돈의_숫자로_대소비교가_가능하다() {
        assertAll(
            () -> assertThat(Money.drawNewMoney(1000).compareTo(Money.NO_VALUE())).isPositive(),
            () -> assertThat(Money.drawNewMoney(1000).compareTo(Money.drawNewMoney(1000))).isZero(),
            () -> assertThat(Money.NO_VALUE().compareTo(Money.drawNewMoney(1000))).isNegative()
        );
    }
}
