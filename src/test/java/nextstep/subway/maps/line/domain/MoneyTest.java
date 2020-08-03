package nextstep.subway.maps.line.domain;

import static org.assertj.core.api.Assertions.*;

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
}
