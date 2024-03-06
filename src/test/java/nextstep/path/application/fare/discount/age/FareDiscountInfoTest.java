package nextstep.path.application.fare.discount.age;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

class FareDiscountInfoTest {

    @Test
    @DisplayName("공제금액과, 공제율을 이용해 할인된 금액을 반환받을 수 있다")
    void fareDiscountInfoTest() {
        final FareDiscountInfo fareDiscountInfo = FareDiscountInfo.of(100, 0.1);

        assertThat(fareDiscountInfo.applyDiscount(1100)).isEqualTo(1000);
    }

    @Test
    @DisplayName("공제금액은 음수가 될 수 없다.")
    void negativeDeductionAmountTest() {
        assertThatThrownBy(() -> FareDiscountInfo.of(-1, 0.1))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("공제율은 1보다 크거나 0보다 작을 수 없다.")
    void validateDiscountRangeTest() {
        assertSoftly(softly -> {
            softly.assertThatThrownBy(() -> FareDiscountInfo.of(100, 1.1))
                    .isInstanceOf(IllegalArgumentException.class);
            softly.assertThatThrownBy(() -> FareDiscountInfo.of(100, -0.1))
                    .isInstanceOf(IllegalArgumentException.class);
        });
    }
}
