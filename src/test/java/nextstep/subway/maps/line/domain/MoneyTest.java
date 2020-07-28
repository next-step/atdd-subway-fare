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
}