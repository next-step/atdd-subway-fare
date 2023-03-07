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
        AgeFareStrategy ds = new TeenagerStrategy();
        assertThat(ds.match(age)).isTrue();
        assertThat(ds.calculateFare(1350)).isEqualTo((int) ((1350 - 350) * 0.8));
    }

    @ParameterizedTest
    @ValueSource(ints = {6, 12})
    void 어린이_요금_정책(int age) {
        AgeFareStrategy ds = new ChildrenStrategy();
        assertThat(ds.match(age)).isTrue();
        assertThat(ds.calculateFare(1350)).isEqualTo((int) ((1350 - 350) * 0.5));
    }

    @Test
    void 성인_요금_정책() {
        AgeFareStrategy ds = new AdultStrategy();
        assertThat(ds.match(20)).isTrue();
        assertThat(ds.calculateFare(1350)).isEqualTo(1350);
    }

    @Test
    void 무료_요금_정책() {
        AgeFareStrategy ds = new FreeStrategy();
        assertThat(ds.match(5)).isTrue();
        assertThat(ds.calculateFare(1350)).isEqualTo(0);
    }
}
