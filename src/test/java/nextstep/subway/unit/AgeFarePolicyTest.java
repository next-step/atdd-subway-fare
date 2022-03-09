package nextstep.subway.unit;

import nextstep.subway.domain.AgeFarePolicy;
import nextstep.subway.domain.FarePolicy;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;

class AgeFarePolicyTest {
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
        FarePolicy policy = new AgeFarePolicy();

        // when
        int discountFare = policy.fare(GENERAL_AGE, GENERAL_FARE, null);

        // then
        assertThat(discountFare).isEqualTo(GENERAL_FARE);
    }

    @DisplayName("청소년 할인")
    @ParameterizedTest
    @ValueSource(ints = {TEENAGER_AGE_BEGIN, TEENAGER_AGE_END})
    void teenager(int age) {
        // given
        FarePolicy policy = new AgeFarePolicy();

        // when
        int discountFare = policy.fare(age, GENERAL_FARE, null);

        // then
        assertThat(discountFare).isEqualTo(720);
    }

    @DisplayName("어린이 할인")
    @ParameterizedTest
    @ValueSource(ints = {CHILD_AGE_BEGIN, CHILD_AGE_END})
    void child(int age) {
        // given
        FarePolicy policy = new AgeFarePolicy();

        // when
        int discountFare = policy.fare(age, GENERAL_FARE, null);

        // then
        assertThat(discountFare).isEqualTo(450);
    }
}