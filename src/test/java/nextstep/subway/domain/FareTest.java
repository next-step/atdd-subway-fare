package nextstep.subway.domain;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class FareTest {

    @Test
    @DisplayName("기본 요금")
    void standard() {
        assertFare(거리요금_계산(10), 1250);
    }

    @Test
    @DisplayName("10 킬로미터 이상 5km 마다 100원 + 기본요금")
    void over10Kilometers() {
        assertFare(거리요금_계산(11), 1350);
    }

    @Test
    @DisplayName("50 킬로미터 이상 8km 마다 100원 + 기본요금")
    void over50Kilometers() {
        assertFare(거리요금_계산(51), 1850);
    }

    private static void assertFare(final int actual, final int expected) {
        assertThat(actual).isEqualTo(expected);
    }

    private static int 거리요금_계산(final int distance) {
        return Fare.calculateOverFare(distance);
    }

}