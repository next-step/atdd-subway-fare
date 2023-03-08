package nextstep.subway.domain;

import nextstep.subway.domain.fare.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;

class AgeFareStrategyTest {

    @ParameterizedTest
    @ValueSource(ints = {13, 18})
    void 청소년_요금_정책(int age) {
        AgeFareStrategy strategy = new TeenagerStrategy();

        assertThat(strategy.match(age)).isTrue();
        assertThat(createCalculator(strategy)).isEqualTo((int) ((1350 - 350) * 0.8));
    }

    @ParameterizedTest
    @ValueSource(ints = {6, 12})
    void 어린이_요금_정책(int age) {
        AgeFareStrategy strategy = new ChildrenCalculator();
        assertThat(strategy.match(age)).isTrue();
        assertThat(createCalculator(strategy)).isEqualTo((int) ((1350 - 350) * 0.5));
    }

    @Test
    void 성인_요금_정책() {
        AgeFareStrategy strategy = new AdultStrategy();
        assertThat(strategy.match(20)).isTrue();
        assertThat(createCalculator(strategy)).isEqualTo(1350);
    }

    @Test
    void 무료_요금_정책() {
        AgeFareStrategy strategy = new FreeStrategy();
        assertThat(strategy.match(5)).isTrue();
        assertThat(createCalculator(strategy)).isEqualTo(0);
    }

    private int createCalculator(AgeFareStrategy strategy) {
        return new FareCalculator(strategy).calculateFare(1350);
    }
}
