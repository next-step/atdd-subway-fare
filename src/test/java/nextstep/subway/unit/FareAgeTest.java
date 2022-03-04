package nextstep.subway.unit;

import nextstep.subway.domain.FareAgeEnum;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;

class FareAgeTest {
    private static final int TEENAGER_AGE_BEGIN = 13;
    private static final int TEENAGER_AGE_END = 18;
    private static final int CHILD_AGE_BEGIN = 6;
    private static final int CHILD_AGE_END = 12;
    private static final int GENERAL_AGE = 19;
    private static final int GENERAL_FARE = 1_250;

    @DisplayName("연령별 할인에 포함되지 않는 나이")
    @Test
    void nonTargeted() {
        // given
        FareAgeEnum fareAge = FareAgeEnum.valueOf(GENERAL_AGE);

        // when
        int discountFare = fareAge.getFareAge(GENERAL_FARE);

        // then
        assertThat(discountFare).isEqualTo(GENERAL_FARE);
    }

    @DisplayName("청소년 할인")
    @ParameterizedTest
    @ValueSource(ints = {TEENAGER_AGE_BEGIN, TEENAGER_AGE_END})
    void teenager(int age) {
        // given
        FareAgeEnum fareAge = FareAgeEnum.valueOf(age);

        // when
        int discountFare = fareAge.getFareAge(GENERAL_FARE);

        // then
        assertThat(discountFare).isEqualTo(720);
    }

    @DisplayName("어린이 할인")
    @ParameterizedTest
    @ValueSource(ints = {CHILD_AGE_BEGIN, CHILD_AGE_END})
    void child(int age) {
        // given
        FareAgeEnum fareAge = FareAgeEnum.valueOf(age);

        // when
        int discountFare = fareAge.getFareAge(GENERAL_FARE);

        // then
        assertThat(discountFare).isEqualTo(450);
    }
}