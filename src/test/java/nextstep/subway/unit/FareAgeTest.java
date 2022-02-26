package nextstep.subway.unit;

import nextstep.subway.domain.FareAge;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class FareAgeTest {
    private static final int TEENAGER_AGE = 18;
    private static final int CHILD_AGE = 12;
    private static final int GENERAL_AGE = 30;
    private static final int GENERAL_FARE = 1_250;

    @DisplayName("연령별 할인에 포함되지 않는 나이")
    @Test
    void nonTargeted() {
        // given
        FareAge fareAge = FareAge.valueOf(GENERAL_AGE);

        // when
        int discountFare = fareAge.getFareAge(GENERAL_FARE);

        // then
        assertThat(discountFare).isEqualTo(GENERAL_FARE);
    }

    @DisplayName("청소년 할인")
    @Test
    void teenager() {
        // given
        FareAge fareAge = FareAge.valueOf(TEENAGER_AGE);

        // when
        int discountFare = fareAge.getFareAge(GENERAL_FARE);

        // then
        assertThat(discountFare).isEqualTo(720);
    }

    @DisplayName("어린이 할인")
    @Test
    void child() {
        // given
        FareAge fareAge = FareAge.valueOf(CHILD_AGE);

        // when
        int discountFare = fareAge.getFareAge(GENERAL_FARE);

        // then
        assertThat(discountFare).isEqualTo(450);
    }
}