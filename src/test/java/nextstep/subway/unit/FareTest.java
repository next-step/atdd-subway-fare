package nextstep.subway.unit;


import nextstep.subway.domain.Fare;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("요금 단위 테스트")
class FareTest {

    @DisplayName("기본 요금일때 테스트")
    @Test
    void defaultFare() {
        // given
        int distance = 10;

        // when
        Fare fare = new Fare(distance);

        // then
        assertThat(fare.getFare()).isEqualTo(1250);
    }

    @DisplayName("10km초과 ~ 50km까지(5km마다 100원씩 추가) 테스트")
    @ParameterizedTest
    @CsvSource(value = {"15:1350","20:1450","25:1550","30:1650","35:1750","40:1850","45:1950","50:2050"}, delimiter = ':')
    void additionalFare(int distance, int expectedFare) {
        // given

        // when
        Fare fare = new Fare(distance);

        // then
        assertThat(fare.getFare()).isEqualTo(expectedFare);
    }

    @DisplayName("50km초과 ~ 100km까지(8km마다 100원씩 추가) 테스트")
    @ParameterizedTest
    @CsvSource(value = {"51:2150","59:2250","66:2250","67:2350","75:2450","83:2550","90:2550","91:2650", "100:2750"}, delimiter = ':')
    void additionalFare2(int distance, int expectedFare) {
        // given

        // when
        Fare fare = new Fare(distance);

        // then
        assertThat(fare.getFare()).isEqualTo(expectedFare);
    }
}